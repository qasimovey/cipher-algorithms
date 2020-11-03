package cipher;

import cipher.VigenereCipher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VigenereCipherTest {
    @Test
    public void testEncryptedAndDecrypted(){
        //given
        String plainText="Hello This is secret".toUpperCase();
        String key="example".toUpperCase();

        //when
        String cipher = VigenereCipher.encyrpt(plainText,key);
        String result=VigenereCipher.decyrpt(cipher,key);

        //then
        Assertions.assertEquals(result,plainText);

    }

    @Test
    public void testEncryptMethod(){
        //given
        String plainText="JAVATPOINT";
        String key="BEST";

        //when
        String cipher =VigenereCipher.encyrpt(plainText,key);

        //then
        Assertions.assertEquals(cipher,"KENTUTGBOX");

    }

    @Test
    public void testDecrypted(){
        //given
        String cipher="KENTUTGBOX";
        String key="BEST";

        //when
        String result =VigenereCipher.decyrpt(cipher,key);

        //then
        Assertions.assertEquals(result,"JAVATPOINT");

    }


}
