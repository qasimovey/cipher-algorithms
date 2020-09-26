package cipher;

import java.util.Arrays;
import java.util.stream.Stream;

public class CaesarCipher {
    public static final int letterCount=26;

    public static String encrypt(String message, int offset){
        if(offset < 0){
            throw new IllegalArgumentException("offset can`t be negative!");
        }

        return message.codePoints()
                    .mapToObj(letterCode->(char)CaesarCipher.applyRule(letterCode,offset))
                    .collect(StringBuffer::new,StringBuffer::append,StringBuffer::append)
                    .toString();

    }

    private static int applyRule(int letterCode,int offset){
        if(Character.isLowerCase(letterCode)){
            int originalAlphabetPosition = letterCode - Character.getNumericValue('a');
            int newAlphabetPosition = (originalAlphabetPosition + offset) % letterCount;
            int newCharacter = Character.getNumericValue('a') + newAlphabetPosition;
            return newCharacter;
        }
        else if (Character.isUpperCase(letterCode)){
            int originalAlphabetPosition = letterCode - Character.getNumericValue('A');
            int newAlphabetPosition = (originalAlphabetPosition + offset) % letterCount;
            int newCharacter = Character.getNumericValue('A') + newAlphabetPosition;
            return newCharacter;
        }
        else {
            return letterCode;
        }
    }
    public static String decrypt(String message,int offset){
        if(offset < 0) throw new IllegalArgumentException("offset can`t be negative!");
        return encrypt(message,letterCount-(offset%letterCount));
    }

}