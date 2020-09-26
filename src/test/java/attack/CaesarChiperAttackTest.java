package attack;

import cipher.CaesarCipher;
import cipher.attack.CaesarChiperAttack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CaesarChiperAttackTest {

    @Test
    public void testAttackMethod1(){
        //given
        String plainText="Hello Children";
        String cipheredText= CaesarCipher.encrypt(plainText,1);

        //when
        String result = CaesarChiperAttack.breakChiper(cipheredText);

        //when
        Assertions.assertEquals(result,plainText);
    }

    @Test
    public void testAttackMethod2(){
        //given
        String plainText="Hi guys, This is so secret message";
        String cipheredText= CaesarCipher.encrypt(plainText,1);

        //when
        String result = CaesarChiperAttack.breakChiper(cipheredText);

        //when
        Assertions.assertEquals(result,plainText);
    }

    @Test
    public void testAttackMethod3(){
        //given
        String plainText="Modern problems require modern solutions";
        String cipheredText= CaesarCipher.encrypt(plainText,1);

        //when
        String result = CaesarChiperAttack.breakChiper(cipheredText);

        //when
        Assertions.assertEquals(result,plainText);
    }
}
