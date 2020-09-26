package cipher;

public class Application {
    public static void main(String[] args) {
        System.out.println(CaesarCipher.encrypt("ABC",1));
        System.out.println(CaesarCipher.decrypt("BCD",1));
       //"AB".codePoints().forEach(System.out::print);
    }
}
