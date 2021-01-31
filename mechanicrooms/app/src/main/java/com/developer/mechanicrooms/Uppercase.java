package com.developer.mechanicrooms;

public class Uppercase {
    public static String CovertFirstLetterInUppercase(String letter) {

    char[] charArray = letter.toCharArray();
    boolean foundSpace = true;

    for(int i = 0; i<charArray.length;i++)
    {
        // if the array element is a letter
        if (Character.isLetter(charArray[i])) {

            // check space is present before the letter
            if (foundSpace) {

                // change the letter into uppercase
                charArray[i] = Character.toUpperCase(charArray[i]);
                foundSpace = false;
            }
        } else {
            // if the new character is not character
            foundSpace = true;
        }
    }
        letter = String.valueOf(charArray);
        return letter;
    }

}
