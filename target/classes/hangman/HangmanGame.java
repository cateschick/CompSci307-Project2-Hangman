package hangman;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import util.DisplayWord;
import util.HangmanDictionary;

public class HangmanGame {
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static int myNumGuessesLeft;
    private static DisplayWord myDisplayWord;
    private int myGuessCounter = 1;
    private Group root;
    private Guesser myGuesser;
    private SecretKeeper myKeeper;
    private static int mySize;

    // End State Variables
    private final String WIN_TEXT = "You won the game!!!! :)";
    private final String LOSE_TEXT = "You lost the game :(";
    private final Paint WIN_SCREEN_COLOR = Color.GREEN;
    private final Paint LOSE_SCREEN_COLOR = Color.RED;
    private final Paint TEXT_COLOR = Color.WHITE;
    private final int TEXT_SIZE = 30;
    private final String TEXT_FONT = "Courier";
    private final double TEXT_OFFSET = 0.15;

    // JFX variables
    private Scene myScene;
    private Timeline myAnimation;
    private List<Text> mySecretWordDisplay;
    private List<Text> myLettersLeftToGuessDisplay;

    /**
     * Create Hangman game with the given dictionary of words to play a game with words
     * of the given length and giving the user the given number of chances.
     */
    public HangmanGame (HangmanDictionary dictionary, int wordLength, int numGuesses, String guesserType, String secretType) {
        myNumGuessesLeft = numGuesses;
        Guesser.myLettersLeftToGuess = new StringBuilder(ALPHABET);

        // Create the proper Guesser
        switch (guesserType) {
            case "SIMPLE" -> myGuesser = new simpleGuesser();
            case "INTERACTIVE" -> myGuesser = new interactiveGuesser();
            case "CLEVER" -> myGuesser = new cleverGuesser();
        }

        // Create the proper SecretKeeper
        switch (secretType) {
            case "SIMPLE" -> myKeeper = new simpleSecretKeeper();
            case "INTERACTIVE" -> myKeeper = new interactiveSecretKeeper();
            case "CLEVER" -> myKeeper = new cleverSecretKeeper();
        }

        // Get secret word & show display word
        myKeeper.getWord(wordLength, dictionary);
        myDisplayWord = new DisplayWord(SecretKeeper.mySecretWord);

        // Get a list of possible words
        List<String> words = dictionary.getWords(wordLength);
        Guesser.possibleWords = new ArrayList<>();
        Guesser.possibleWords.addAll(words);
    }

    /**
     * Start the game by animating the display of changes in the GUI every speed seconds.
     */
    public void start (double speed) {
        myAnimation = new Timeline();
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(speed), e -> playRound()));
        myAnimation.play();
    }

    /**
     * Create the game's "scene": what shapes will be in the game and their starting properties.
     */
    public Scene setupDisplay (int width, int height, Paint background) {
        root = new Group();
        // Set up displays
        setUpGuesserDisplay();
        setUpSecretDisplay();

        // Create Scene
        myScene = new Scene(root, width, height, background);
        mySize = width;

        // Allow input for interactive game
        myScene.setOnKeyPressed(e -> handleInput(e.getCode()));

        return myScene;
    }

    /**
     * This function sets up the display of letters for the Guesser to guess
     */
    private void setUpGuesserDisplay() {
        myLettersLeftToGuessDisplay = new ArrayList<>();
        for (int k = 0; k < ALPHABET.length(); k += 1) {
            Text displayLetter = new Text(50+20*k, 50, ALPHABET.substring(k, k+1));
            displayLetter.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            myLettersLeftToGuessDisplay.add(displayLetter);
            root.getChildren().add(displayLetter);
        }
    }

    /**
    This function sets up the secret word display, hiding letters in the word
    that have not been guessed yet
     */
    private void setUpSecretDisplay() {
        mySecretWordDisplay = new ArrayList<>();
        for (int k = 0; k < myDisplayWord.toString().length(); k += 1) {
            Text displayLetter = new Text(200+20*k, 500, myDisplayWord.toString().substring(k, k+1));
            displayLetter.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            mySecretWordDisplay.add(displayLetter);
            root.getChildren().add(displayLetter);
        }
    }

    /**
     * Play one round of the game.
     */
    public void playRound () {

        // Make a guess
        myGuesser.makeGuess();

        // Ensures guesses are valid & catches cases where:
        if (! Guesser.hasUserGuessed() || ! Guesser.validGuess()) {
            System.out.println("Please enter a single letter ...");
            return;
        }

        // Get index of guessed letter to ensure guess is not a repeated letter
        int index = Guesser.myLettersLeftToGuess.indexOf(Guesser.myGuess);
        if (Guesser.repeatedGuess(index)) {
            System.out.println("Please enter a letter you haven't guessed yet ...");
            return;
        }

        // Record guess
        Guesser.myLettersLeftToGuess.setCharAt(index, ' ');

        compareGuess();
        updateDisplay();

        // if Clever SecretKeeper is enabled, update SecretWord
        if(myKeeper.equals("CLEVER")) {
            cleverSecretKeeper.updateSecretword(Guesser.myGuess);
        }

        // Reset myGuess for next round
        Guesser.myGuess = null;

        // Check to see if user won/lost
        checkEndState();
    }

    /*
    This function compares the current guess to the secret word
    */
    private void compareGuess() {
        if (!SecretKeeper.mySecretWord.contains(Guesser.myGuess)) {
            myNumGuessesLeft -= 1;
            addLimb();
        } else {
            myDisplayWord.update(Guesser.myGuess.charAt(0), SecretKeeper.mySecretWord);
        }
    }

    /**
     * This function checks to see if the game has been won or lost
     */
    private void checkEndState() {
        // End State
        if ((myNumGuessesLeft == 0) || (myDisplayWord.equals(SecretKeeper.mySecretWord))) {
            myScene.setOnKeyPressed(null);
            addText(! (myNumGuessesLeft == 0));
            myAnimation.stop();
        }
    }

    /**
     * This function results in a message indicating whether the user won or lost the game
     */
    private void addText(boolean wonGame) {
        StringBuilder t = new StringBuilder();
        root.getChildren().clear();
        if (wonGame) {
            t.append(WIN_TEXT);
            myScene.setFill(WIN_SCREEN_COLOR);
        }
        else {
            t.append(LOSE_TEXT);
            myScene.setFill(LOSE_SCREEN_COLOR);
        }
        Text text = new Text(t.toString());
        text.setFont(Font.font (TEXT_FONT, TEXT_SIZE));
        text.setFill(TEXT_COLOR);
        text.setY(mySize/2);
        text.setX(mySize*TEXT_OFFSET);

        // Create Screen
        root.getChildren().add(text);
    }

    /**
     * This function updates the word display based on the user's guess
     */
    private void updateDisplay() {
        for (int k = 0; k < Guesser.myLettersLeftToGuess.length(); k += 1) {
            myLettersLeftToGuessDisplay.get(k).setText(Guesser.myLettersLeftToGuess.substring(k, k+1));
        }
        for (int k = 0; k < myDisplayWord.toString().length(); k += 1) {
            mySecretWordDisplay.get(k).setText(myDisplayWord.toString().substring(k, k+1));
        }
    }

    /**
     * This function adds a specific limb to the "hang man" based on the number of guess it is
     */
    private void addLimb() {
        // Differentiate between lefts and rights
        int coefficient = 1;
        if (myGuessCounter % 2 == 1) {
            coefficient = -1;
        }

        // Draw limbs
        if (myGuessCounter == 9 || myGuessCounter == 10) {
            drawFoot(coefficient);
        } else if (myGuessCounter == 7 || myGuessCounter == 8) {
            drawHand(coefficient);
        } else if (myGuessCounter == 5 || myGuessCounter == 6) {
            drawLeg(coefficient);
        } else if (myGuessCounter == 3 || myGuessCounter == 4) {
            drawArm(coefficient);
        } else if (myGuessCounter == 2) {
            drawBody();
        } else if (myGuessCounter == 1) {
            drawHead();
        }

        myGuessCounter++;
    }

    /**
     * This function draws a head on the "hanged man"
     */
    private void drawHead(){
        Circle head = new Circle(50);
        head.setCenterX(mySize/2);
        head.setCenterY(mySize/4);
        root.getChildren().add(head);
    }
    /**
     * This function draws a body on the "hanged man"
     */
    private void drawBody() {
        Rectangle body = new Rectangle(25, 150);
        body.setX(mySize/2 - 13);
        body.setY(mySize/4 + 50);
        root.getChildren().add(body);
    }
    /**
     * This function draws an arm on the "hanged man"
     */
    private void drawArm(int side) {
        Rectangle arm = new Rectangle(5, 100);
        arm.setY(mySize/3);
        arm.setX((mySize/2) + (side*40));
        arm.setRotate(side*45);
        root.getChildren().add(arm);
    }
    /**
     * This function draws a leg on the "hanged man"
     */
    private void drawLeg(int side){
        Rectangle leg = new Rectangle(5, 100);
        leg.setY(mySize/2 + 25);
        leg.setX(mySize/2 + (side*25) - 5);
        leg.setRotate(side*-45);
        root.getChildren().add(leg);
    }
    /**
     * This function draws a hand on the "hanged man"
     */
    private void drawHand(int side){
        Circle hand = new Circle(20);
        hand.setCenterY(mySize/3 + 20);
        hand.setCenterX((mySize/2) + (side*75));
        root.getChildren().add(hand);
    }
    /**
     * This function draws a foot on the "hanged man"
     */
    private void drawFoot(int side){
        Circle foot = new Circle(20);
        foot.setCenterY(mySize/2 + 120);
        foot.setCenterX((mySize/2) + (side*75));
        root.getChildren().add(foot);
    }

    private void handleInput(KeyCode e){
        myGuesser.setInput(e);
    }
}
