package cipher;

import cipher.attack.CaesarCipherAttack;
import cipher.attack.VigenereCipherAttack;

public class Application {
    public static void main(String[] args) {

        String cipher = "BCRRBCQORHKEPSLSLCWRWXXDESPEZMPYQWCEBCBOSFHCIZHSQWVHCBRWRVLNEGDRCKRRQS";
        var cipherTool = new VigenereCipherAttack();
        var resultKey = cipherTool.breakCipherKey4(cipher);

        System.out.println("key is: "+ resultKey);
        System.out.println("Original text is: "+ VigenereCipher.decyrpt(cipher,resultKey));

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
