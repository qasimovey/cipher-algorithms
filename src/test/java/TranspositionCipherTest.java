import cipher.TranspositionCipher;
import cipher.VigenereCipher;
import org.junit.jupiter.api.*;

public class TranspositionCipherTest {

    TranspositionCipher cipherUtil;

    @BeforeEach
    public void setUp(){
        cipherUtil = new TranspositionCipher(3,4);
    }

    @Test
    public void testEncryptedAndDecrypted(){
        //given
        String plainText="helloworld";
        String cipherText="holewdlo lr";
        //cipherUtil = new TranspositionCipher(3,4);

        //when
        String cipher = cipherUtil.encrypt(plainText);
        String result=cipherUtil.decrypt(cipher);

        //then
        Assertions.assertEquals(result,plainText);
        Assertions.assertEquals(cipher,cipherText);


    }

    @Test
    public void testEncryptMethod(){
        //given
        String plainText="helloworld";
        //cipherUtil = new TranspositionCipher(3,4);

        //when
        String cipherText = cipherUtil.encrypt(plainText);

        //then
        Assertions.assertEquals(cipherText,"holewdlo lr");

    }

    @Test
    public void testDecrypted(){
        //given
        String cipher="holewdlo lr";
        //cipherUtil = new TranspositionCipher(3,4);

        //when
        String result =cipherUtil.decrypt(cipher);

        //then
        Assertions.assertEquals(result,"helloworld");

    }
}
