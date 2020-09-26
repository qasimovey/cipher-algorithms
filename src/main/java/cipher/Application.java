package cipher;

public class Application {
    public static void main(String[] args) {

        String plainText="Hello This is secret".toUpperCase();
        String key="example".toUpperCase();
        System.out.println(VigenereCipher.decyrpt(plainText,key));

    }
}
