package cipher;

import cipher.attack.CaesarChiperAttack;

public class Application {
    public static void main(String[] args) {

        System.out.println(CaesarCipher.encrypt("Hello children",1));
        System.out.println(CaesarChiperAttack.breakChiper("Ifmmp dijmesfo"));
       // System.out.println(Ca);

    }
}
