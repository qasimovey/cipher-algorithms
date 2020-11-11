package cipher.attack;

import cipher.VigenereCipher;
import org.apache.commons.codec.binary.StringUtils;
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

    public static final char[] charSet = {'A','B','C','D','E','F','G','H','I','J',
                'K','L','M','N','O','P','Q','R','S','T','U',
                'V','W','X','Y','Z'};
    //cipherText.toUpperCase
    public String breakCipherKey(String cipherText,int keyLen){

        //produce key
        List<String> keyList = generateKeyList(keyLen);

        expectedLettersFrequencies = Arrays.stream(englishLettersProbabilities)
                .map(probability -> probability * cipherText.length())
                .toArray();

        final Map<String,Double> keyAndOffSet = new HashMap<>();

        //ignore parallelStream for now
        keyList.stream()
                .forEach((key) -> {
                    double chiSquare = calcChiSquare(cipherText.toUpperCase(), key);
                    keyAndOffSet.put(key,chiSquare);
                });

        //System.out.println(keyAndOffSet);

        Map.Entry<String, Double> result = keyAndOffSet.entrySet()
                        .stream()
                        .sorted((key1, key2) -> keyAndOffSet.getOrDefault(key2,0d).compareTo(keyAndOffSet.getOrDefault(key1,0d)))
                        .findFirst().get();

        System.out.println(result);
        return result.getKey();//VigenereCipher.decyrpt(cipherText.toUpperCase(),result.get().getKey());

    }

    double calcChiSquare(String cipherText, String key) {
        String decyrptText = VigenereCipher.decyrpt(cipherText.toUpperCase(), key);
        long[] lettersFrequencies = observedLettersFrequencies(decyrptText);
        double chiSquare = new ChiSquareTest().chiSquare(expectedLettersFrequencies, lettersFrequencies);
        //System.out.println("key is: " + key + " text is:" + decyrptText);

        return chiSquare;
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


    public  List<String> generateKeyList(int length) {
        List<String> keyList = new LinkedList<>();

//        StringBuilder collect = IntStream.range(65, 65+26)
//                .mapToObj((item) -> String.valueOf((char) item))
//                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);
//
//        char[] charSet = collect.toString().toCharArray();
        produceKey(charSet,length, "",keyList);

        //keyList.forEach(System.out::println);
        return keyList;
    }


    //recursive function -- call itself until key formed
    public void produceKey(char[] charSet, int k,String prefix ,List<String> keyList) {
        if(k == 0){ // means, key already formed
            //add key into list
            keyList.add(prefix);
            return;
        }

        for (int i = 0; i < letterCount; i++) {
            //new symbol added
            String newPrefix = prefix + charSet[i];

            produceKey(charSet,k-1,newPrefix,keyList);
        }
    }


}
