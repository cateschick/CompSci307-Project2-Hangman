package hangman;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.HashMap;

public class cleverGuesser extends Guesser {
    private int myValue;

    /**
     * This function makes a clever guess based on the letter that appears in the most
     * reamining words
     */
    public void makeGuess() {
            HashMap<Character, ArrayList<String>> myMap = new HashMap<>();
            char[] letterArray = myLettersLeftToGuess.toString().trim().toCharArray();
            int max = 0;
            Character maxLetter = null;

            // list all possible options for each letter
            for(char letter: letterArray) {
                ArrayList<String> list = new ArrayList<>();

                myMap.put(letter, list);
                for(String word: possibleWords) {
                    if (word.contains(String.valueOf(letter))) {
                        list.add(word);
                        myMap.put(letter, list);
                        myValue++;
                    }
                }
                if (myValue >= max) {
                    max = myValue;
                    maxLetter = letter;
                    myGuess = String.valueOf(letter);
                }
            }
        possibleWords = myMap.get(maxLetter);
        }

    /**
     *
     * @param e
     * Since cleverGuesser is NOT interactive, this is an empty method
     */
    public void setInput(KeyCode e){
        }
    }

