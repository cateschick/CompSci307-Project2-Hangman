package hangman;

import javafx.scene.input.KeyCode;
import java.util.ArrayList;

public abstract class Guesser {
    public static String myGuess;
    public static StringBuilder myLettersLeftToGuess;
    public static ArrayList<String> possibleWords;

    public abstract void makeGuess();
    public abstract void setInput(KeyCode e);

    /**
     *
     * @return Whether or not the user has guessed
     */
    static boolean hasUserGuessed() {
        return (!(myGuess == null));
    }

    /**
     *
     * @return Whether the user's guess is a valid letter
     */
    static boolean validGuess() {
        return (myGuess.length() == 1 && HangmanGame.ALPHABET.contains(myGuess));
    }

    /**
     *
     * @param index location of guess within available letters
     * @return Whether the user has already guessed a particular letter
     */
    static boolean repeatedGuess(int index) {
        // Do not count repeated guess as a miss
        return (index < 0);
    }
}
