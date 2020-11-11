package cipher;

import cipher.attack.CaesarCipherAttack;
import cipher.util.NgramFinder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.IntStream;

public class Application {
    public static void main(String[] args) {


        HashMap<String, Integer> happy_victory_day = NgramFinder.countNgram("happy victory day".replaceAll(" ",""), 2);
        System.out.println(happy_victory_day);


//        System.out.println(CaesarCipherAttack.breakChiper("SKKZ NKXK"));
//
//        System.out.println(CaesarCipher.encrypt("MEET HERE",6));
//        System.out.println(VigenereCipher.encyrpt("FOLLO WTHEY ELLOW BRICK ROAD", "OZ"));

        //map.computeIfPresent("key",(key,val)->map.put(val+1))
        //IntStream.rangeClosed(0,text.length())


        /*
        String plainText = null;
        String cipher = null ;
        String result = null;
        String attackResult = null;

        //Caesar Cipher Example
        System.out.println("---------------------------------------------");
        System.out.println("Caesar Cipher Example");
        plainText = "Hi guys, This is so secret message";
        cipher = CaesarCipher.encrypt(plainText,2);
        result = CaesarCipher.decrypt(cipher,2);
        attackResult = CaesarCipherAttack.breakChiper(cipher);

        System.out.println("Plain Text: " + plainText);
        System.out.println("Cipher Text: " + cipher);
        System.out.println("After Decryption : " + result);
        System.out.println("attackResult: " + attackResult);


        System.out.println("---------------------------------------------");
        System.out.println("Vigenere Cipher example");
        plainText = "CYBERSECURITY";
        cipher = VigenereCipher.encyrpt(plainText,"secret".toUpperCase());
        result = VigenereCipher.decyrpt(cipher,"secret".toUpperCase());
        System.out.println("Plain Text: " + plainText);
        System.out.println("Cipher Text: " + cipher);
        System.out.println("After Decryption : " + result);

        System.out.println("---------------------------------------------");
        System.out.println("Transposition Cipher example");
        // Transposition Cipher example
        plainText = "Karabakh is Azerbaijan";
        TranspositionCipher trCipher = new TranspositionCipher(8,8);
        cipher = trCipher.encrypt(plainText);
        result = trCipher.decrypt(cipher);

        System.out.println("Plain Text: " + plainText);
        System.out.println("Cipher Text: " + cipher);
        System.out.println("After Decryption : " + result);
        */

    }
}
