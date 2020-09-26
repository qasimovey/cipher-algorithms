import cipher.CaesarCipher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CaesarCiperTest {

    @Test
    @DisplayName("when valid input is given then works properly")
    public void when_validInput_then_works(){
        //given
        String plainText ="ABCD";
        int offset=1;

        //when
        String result=CaesarCipher.encrypt(plainText,offset);

        //then
        Assertions.assertEquals(result,"BCDE");
    }

    @Test
    @DisplayName("when invalid input - negative offset is given then exception")
    public void when_invalidInput_then_exception(){
        Assertions.assertThrows(IllegalArgumentException.class,()->CaesarCipher.encrypt("example",-1));
        Assertions.assertThrows(IllegalArgumentException.class,()->CaesarCipher.decrypt("example",-1));
    }

    @Test
    @DisplayName("when offset is out of range then do cycle over alphabet")
    public void when_offsetOutOfRange_then_DoCycle(){
        //given
        String plainText ="ABCD";
        int offset=CaesarCipher.letterCount + 1;

        //when
        String result=CaesarCipher.encrypt(plainText,offset);

        //then
        Assertions.assertEquals(result,"BCDE");
    }
}
