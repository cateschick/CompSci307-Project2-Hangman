package hangman;

import javafx.scene.input.KeyCode;

public class interactiveGuesser extends Guesser {
    private static KeyCode code;

    /**
     * This function sets myGuess to the value of the user's inputted letters
     */
    public void makeGuess() {
        myGuess = code.getChar().toLowerCase();
    }

    /**
     *
     * @param e
     * Since interactiveGuesser IS interactive, this method will set variable code to the inputted value
     */
    public void setInput(KeyCode e) {
        code = e;
    }

}
