package cipher.attack;

import cipher.VigenereCipher;
import org.apache.commons.math3.stat.inference.ChiSquareTest;

import java.util.*;
import java.util.stream.IntStream;

public class VigenereCipherAttack {

    public static final int letterCount = 26;
    private static final double[] englishLettersProbabilities =
            {0.073, 0.009, 0.030, 0.044, 0.130, 0.028, 0.016, 0.035, 0.074,
                    0.002, 0.003, 0.035, 0.025, 0.078, 0.074, 0.027, 0.003,
                    0.077, 0.063, 0.093, 0.027, 0.013, 0.016, 0.005, 0.019, 0.001};

    private double[] expectedLettersFrequencies;


    //cipherText.toUpperCase
    public String breakCipherKey4(String cipherText){

        //produce key
        List<String> keyList = produceKey4();

        expectedLettersFrequencies = Arrays.stream(englishLettersProbabilities)
                .map(probability -> probability * cipherText.length())
                .toArray();

        final Map<String,Double> keyAndOffSet=new HashMap<>();

        keyList.parallelStream()
                .forEach((key) -> {
                    double chiSquare = calcChiSquare(cipherText.toUpperCase(), key);
                    keyAndOffSet.put(key,chiSquare);
                });

        System.out.println(keyAndOffSet);

        Map.Entry<String, Double> result = keyAndOffSet.entrySet()
                        .stream()
                        .sorted((key1, key2) -> keyAndOffSet.getOrDefault(key2,0d).compareTo(keyAndOffSet.getOrDefault(key1,0d)))
                        .findFirst().get();

        System.out.println(result);
        return result.getKey();//VigenereCipher.decyrpt(cipherText.toUpperCase(),result.get().getKey());

    }

    double calcChiSquare(String cipherText, String key) {
        String decyrptText = VigenereCipher.decyrpt(cipherText.toUpperCase(), key.toUpperCase());
        long[] lettersFrequencies = observedLettersFrequencies(decyrptText);
        double chiSquare = new ChiSquareTest().chiSquare(expectedLettersFrequencies, lettersFrequencies);
        //System.out.println("key is: " + key + " text is:" + decyrptText);

        return chiSquare;
    }

    List<String> produceKey4() {
        List<String> keyList = new LinkedList<>();

        for (char i = 'A'; i < 'Z'; i++) {
            for (char j = 'A'; j <'Z'; j++) {
                for (char k = 'A'; k < 'Z'; k++) {
                    for (char l = 'A'; l < 'Z'; l++) {
                        StringBuilder strBuilder = new StringBuilder();
                        strBuilder.append(i);
                        strBuilder.append(j);
                        strBuilder.append(k);
                        strBuilder.append(l);
                        //add key
                        keyList.add(strBuilder.toString());
                    }
                }
            }
        }
        return keyList;
    }



    /*
    Map<Character, Integer> getFrequencies(String text){
        Map<Character,Integer> frequencies = new HashMap<>();
        for (char ch : text.toCharArray())
            frequencies.put(ch, frequencies.getOrDefault(ch, 0) + 1);

        return frequencies;

        Map<Character, Long> frequency =
            str.chars()
               .mapToObj(c -> (char)c)
               .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    }*/

    private static long[] observedLettersFrequencies(String message) {
        return IntStream.rangeClosed('A', 'Z')
                .mapToLong(letter -> countLetter((char) letter, message))
                .toArray();
    }

    private static long countLetter(char letter, String message) {
        return message.chars()
                .filter(character -> character == letter)
                .count();
    }

}
