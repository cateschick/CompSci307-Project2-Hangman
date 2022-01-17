package hangman;

import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import util.HangmanDictionary;

import java.util.Optional;

public class interactiveSecretKeeper extends SecretKeeper {

    /**
     *
     * @param wordLength length of secret word
     * @param dictionary dictionary of words
     *                   This function asks for a user's input to use as the secret word
     */
    public void getWord(int wordLength, HangmanDictionary dictionary) {
         mySecretWord = getInput(String.format("Please enter a secret word %d letters long", wordLength), wordLength);
    }

    /**
     *
     * @param prompt
     * @param numCharacters
     * @return This function returns input from a user for the stored secret word
     */
    public static String getInput(String prompt, int numCharacters) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText(prompt);
        // DO NOT USE IN GENERAL - this is a TERRIBLE solution from UX design, and we will learn better ways later
        Optional<String> answer = dialog.showAndWait();
        while (answer.isEmpty() || answer.get().length() != numCharacters) {
            answer = dialog.showAndWait();
        }
        return answer.get();
    }
}
