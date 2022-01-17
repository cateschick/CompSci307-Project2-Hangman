package hangman;

import javafx.scene.input.KeyCode;

public class simpleGuesser extends Guesser {
    private static int myIndex;
    private static final String LETTERS_ORDERED_BY_FREQUENCY = "etaoinshrldcumfpgwybvkxjqz";

    /**
     *
     * @param e
     * Since simpleGuesser is NOT interactive, this is an empty method
     */
    public void setInput(KeyCode e) {
    }

    /**
     * This function makes a simple guess based on letter frequency
     */
    public void makeGuess() {
        myGuess = "" + LETTERS_ORDERED_BY_FREQUENCY.charAt(myIndex++);
    }
}
