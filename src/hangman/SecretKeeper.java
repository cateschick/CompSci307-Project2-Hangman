package hangman;

import util.HangmanDictionary;
import java.util.ArrayList;

public abstract class SecretKeeper {
    public static String mySecretWord;
    public static ArrayList<String> possibleWords;

    /**
     *
     * @param wordLength length of secret word
     * @param dictionary dictionary of words
     * This function returns the proper getWord() method for the SecretKeeper Type
     */
    public abstract void getWord(int wordLength, HangmanDictionary dictionary);
}
