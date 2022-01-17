import hangman.HangmanGame;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import util.HangmanDictionary;

/**
 * This class launches the Hangman game and plays once.
 *
 * @author Robert C. Duvall
 */
public class Main extends Application {
    // constants - JFX
    public static final String TITLE = "HangPerson";
    public static final int SIZE = 600;
    public static final Paint BACKGROUND = Color.THISTLE;
    public static final double SECOND_DELAY = 1;
    // constants - Game
    public static final String DICTIONARY = "lowerwords.txt";
    public static int NUM_LETTERS;
    public static int NUM_MISSES;

    /**
     *
     * @param s
     * This function sets the parameters for easy, medium, and hard levels
     */
    public void setLevelParams(String s) {
        switch (s) {
            case "EASY" -> {
                NUM_MISSES = 10;
                NUM_LETTERS = 4;
            }
            case "MEDIUM" -> {
                NUM_MISSES = 8;
                NUM_LETTERS = 6;
            }
            case "HARD" -> {
                NUM_MISSES = 6;
                NUM_LETTERS = 8;
            }
        }
    }
    /**
     * Organize display of game in a scene and start the game.
     */
    @Override
    public void start (Stage stage) {
        String GUESSER_TYPE = "INTERACTIVE";
        String SECRET_TYPE = "CLEVER";
        String LEVEL = "MEDIUM";
        setLevelParams(LEVEL);
        HangmanGame game = new HangmanGame(
                new HangmanDictionary(DICTIONARY), NUM_LETTERS, NUM_MISSES, GUESSER_TYPE, SECRET_TYPE);
        stage.setScene(game.setupDisplay(SIZE, SIZE, BACKGROUND));
        stage.setTitle(TITLE);
        stage.show();
        game.start(SECOND_DELAY);
    }
}
