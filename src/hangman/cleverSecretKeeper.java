package hangman;

import util.HangmanDictionary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class cleverSecretKeeper extends SecretKeeper {

    /**
     *
     * @param wordLength length of secret word
     * @param dictionary dictionary of words
     *                   Clever Secret Keeper initially selects a random word
     */
    public void getWord(int wordLength, HangmanDictionary dictionary) {
        possibleWords = (ArrayList<String>) dictionary.getWords(wordLength);
        mySecretWord = dictionary.getRandomWord(wordLength).toLowerCase();
    }

    /*
 This function is used when clever secretkeeper is enabled;
 After every turn, it will update the secret word
  */
    public static void updateSecretword(String guessedLetter) {
        HashMap<ArrayList<Integer>, ArrayList<String>> patternMap = new HashMap<>();

        int max = 0;
        ArrayList<Integer> maxPattern = new ArrayList<>();

        for (String word: possibleWords) {
            char[] a = word.toCharArray();
            ArrayList<Integer> indexList = new ArrayList<>();
            for (char c : a) {
                int index = word.indexOf(c);
                // Make sure word is made up of only letters left / guessed letter
                if (String.valueOf(c).equals(guessedLetter)) {
                    indexList.add(index);
                }
            }
            // NEW PATTERN
            if (patternMap.get(indexList) == null) {
                patternMap.put(indexList, new ArrayList<>());
            } // ADD WORD
            else {
                ArrayList<String> wordList = patternMap.get(indexList);
                wordList.add(word);
                patternMap.put(indexList, wordList);
            }
//
            if (patternMap.get(indexList).size() >= max) {
                max = patternMap.get(indexList).size();
                maxPattern.clear();
                maxPattern.addAll(indexList);
            }
        }
        // Update possible words and select a random word
        possibleWords.clear();
        possibleWords = patternMap.get(maxPattern);
        mySecretWord = cleverSecretKeeper.getRandomWord();
        System.out.println(mySecretWord);
    }

    /*
Selects a random word from the altered word list in clever Secret Keeper mode
 */
    public static String getRandomWord() {
        Random r = new Random();
        return possibleWords.get(r.nextInt(possibleWords.size()));
    }
}
