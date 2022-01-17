package hangman;

import util.HangmanDictionary;
import java.util.ArrayList;

public class simpleSecretKeeper extends SecretKeeper {

    /**
     * This function randomly selects a word of a given wordlength from a dictionary
     */
    public void getWord(int wordLength, HangmanDictionary dictionary) {
        possibleWords = (ArrayList<String>) dictionary.getWords(wordLength);
        mySecretWord = dictionary.getRandomWord(wordLength).toLowerCase();
    }
}
