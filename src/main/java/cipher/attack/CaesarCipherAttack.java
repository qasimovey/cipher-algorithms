package cipher.attack;

import cipher.CaesarCipher;
import org.apache.commons.math3.stat.inference.ChiSquareTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class CaesarCipherAttack {
    public static final int letterCount = 26;
    private static final double[] englishLettersProbabilities =
            {0.073, 0.009, 0.030, 0.044, 0.130, 0.028, 0.016, 0.035, 0.074,
            0.002, 0.003, 0.035, 0.025, 0.078, 0.074, 0.027, 0.003,
            0.077, 0.063, 0.093, 0.027, 0.013, 0.016, 0.005, 0.019, 0.001};

    public static String breakChiper(String message){
        double[] expectedLettersFrequencies = Arrays.stream(englishLettersProbabilities)
                .map(probability -> probability * message.length())
                .toArray();

        double[] chiSquares = new double[letterCount];
        Map<Integer,String> offsetAndPlainText=new HashMap<>();

        //apply each offset, then pick up the results
        for (int offset = 0; offset < chiSquares.length; offset++) {
            String decipheredMessage = CaesarCipher.decrypt(message, offset);
            System.out.printf("%s, %d ",decipheredMessage,offset);
            System.out.println();
            offsetAndPlainText.put(offset,decipheredMessage);
            long[] lettersFrequencies = observedLettersFrequencies(decipheredMessage);
            double chiSquare = new ChiSquareTest().chiSquare(expectedLettersFrequencies, lettersFrequencies);
            chiSquares[offset] = chiSquare;
        }
        int rightOffset=findCorrectOffset(chiSquares);

        return offsetAndPlainText.get(rightOffset);
    }
    private static int findCorrectOffset(double[] arr){
        int offset=0;
        double probability=arr[0];
        for (int indx = 0; indx < arr.length ; indx++) {
            if(probability > arr[indx]){
                offset=indx;
                probability=arr[indx];
            }
        }
        return offset;
    }
    private static long[] observedLettersFrequencies(String message) {
        return IntStream.rangeClosed('a', 'z')
                .mapToLong(letter -> countLetter((char) letter, message))
                .toArray();
    }

    private static long countLetter(char letter, String message) {
        return message.chars()
                .filter(character -> character == letter)
                .count();
    }
}
