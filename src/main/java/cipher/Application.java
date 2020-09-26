package cipher;

import cipher.attack.CaesarCipherAttack;

public class Application {
    public static void main(String[] args) {

        System.out.println(CaesarCipher.encrypt("Hello children",1));
        System.out.println(CaesarCipherAttack.breakChiper("Ifmmp dijmesfo"));
       // System.out.println(Ca);

    }
}
