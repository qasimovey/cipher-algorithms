package cipher;

import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static cipher.DESCipher.Helper.*;
public class DESCipher {

    //for creating subkeys
    private final String keyLeftPart[] = new String[17]; //16 + 1
    private final String keyRightPart[] = new String[17]; //16 + 1

    //storing subkeys that of length 48 bits
    private final String subkeys[] = new String[17]; //16 + 1

    //for storing msg parts in encrypted form
    private final String msgLeftPart[] = new String[17]; //16 + 1
    private final String msgRightPart[] = new String[17]; //16 + 1


    //start to execute
    private String doEncrypt(String plainText) throws Exception{

        System.out.println("ENCRYPTION STARTED ...");
        //splitting message into blocks of 64 bits , process, and then concatenate them :)
        //convert into binary, 0s and 1s, we will work on bits
        String msg2Binary = toBinary(plainText);
        //String msg2Binary = toBinary(hextext);

        System.out.printf("ORIGINAL MESSAGE : %s \n",plainText);
        System.out.printf("ORIGINAL MESSAGE IN HEX : %s \n",toHex(plainText));
        System.out.printf("ORIGINAL MESSAGE IN binary : %s \n",msg2Binary);

        StringBuilder strBuilder = new StringBuilder();

        // if length % 64 <> 0
        if (msg2Binary.length()%64 != 0){
            // add slack bits,namely 0s
            msg2Binary = msg2Binary.concat(repeatNtimes(msg2Binary.length()%64,"0"));
        }
        //splitting up message into  block of 64 bits
        for (int i = 0; i < msg2Binary.length() - 64 ; i = i + 64) {
            String cipherBlock = encodeBlock64Bits(msg2Binary.substring(i, i + 64));
            strBuilder.append(cipherBlock);
        }
        return strBuilder.toString();
    }

    public String encrypt(String plainText,String plainKey) throws Exception{
        System.out.println("DES STARTED ....");
        prepareKeySet(plainKey);

        String result = doEncrypt(plainText);

        System.out.println("DES FINISHED ....");
        return result;
    }

    //deal only encryption of block
    private String encodeBlock64Bits(String blockMessage) {
        StringBuilder strBuilder = new StringBuilder();

        System.out.printf("Before permutation block is %s and size %d \n",blockMessage,blockMessage.length());

        //do initial permutation,INPUT:64 BITS ,OUTPUT:64 BITS, just permutation
        for (int i = 0; i < IP.length; i++) {
            //first bits corresponds 0th in sequence of bits,that`s why '-1' involved
            strBuilder.append(blockMessage.charAt(IP[i] - 1));
        }

        System.out.printf("After permutation block is %s and size %d \n",strBuilder.toString(),strBuilder.length());

        //Next divide the permuted block IP(64bits) into a left half L0 of 32 bits, and a right half R0 of 32 bits.
        msgLeftPart[0] = strBuilder.substring(0,32);
        msgRightPart[0] =strBuilder.substring(32);
        System.out.printf("L0  : %s AND %d \n", msgLeftPart[0],msgLeftPart[0].length());
        System.out.printf("R0   : %s AND %d \n", msgRightPart[0],msgRightPart[0].length());

        for (int i = 1; i <= 16 ; i++) {
            //Ln = Rn-1
            msgLeftPart[i] = msgRightPart[i-1];
            //Rn = Ln-1 + f ( Rn-1, Kn )
            //msgRightPart[i] = msgLeftPart[i-1] + f(msgRightPart[i-1],subkeys[i]);
            msgRightPart[i] = doXor(msgLeftPart[i-1] , f(msgRightPart[i-1],subkeys[i]));

            System.out.printf("iter  L%d  : %s AND %d \n",i, msgLeftPart[i],msgLeftPart[i].length());
            System.out.printf("iter  R%d   : %s AND %d \n",i, msgRightPart[i],msgRightPart[i].length());
        }

        //At the end of the sixteenth round we have the blocks L16 and R16.
        // We then reverse the order of the two blocks into the 64-bit block
        //R16L16

        System.out.printf("L16 %s AND %d \n", msgLeftPart[16],msgLeftPart[16].length());
        System.out.printf("R16 %s AND %d \n", msgRightPart[16],msgRightPart[16].length());

        String result = doPermutation(msgRightPart[16] + msgLeftPart[16], IP_MATRIX_MSG);
        System.out.printf("AFTER ENCODING BLOCK %s \n", result);
        return result;

    }

    //xor bitwise operations implementation
    private String doXor(String text1,String text2) {
        StringBuilder strBuilder = new StringBuilder();

        if(text1.length() != text2.length())throw new IllegalStateException("size do not match");

        for (int i = 0; i < text1.length(); i++) {
            /* Basic XOR operations, modulus % 2
                1 1 0
                0 0 0
                1 0 1
                1 1 1
             */
            strBuilder.append(text1.charAt(i) == text2.charAt(i) ? "0" : "1");
        }
        return strBuilder.toString();
    }

    //process message of 32 bits with subkey of size 48
    private String f(String rMsgPart, String subkey) {
        //we first expand each block Rn-1 from 32 bits to 48 bits
        //upgraded from 32 -> 48 bits
        rMsgPart = doPermutation(rMsgPart,BIT_E); //input 32 bits, output 48 bits

        //do XOR Operation
        String xor_result = doXor(subkey,rMsgPart);

        StringBuilder strBuilder = new StringBuilder();
        //we have 8 groups where each group consisting of 6 bits
        // aim is to replace each group of 6 bits with the other group of 4 bits
        //Kn + E(Rn-1) =B1B2B3B4B5B6B7B8,

        //so we need to calculate
        //S1(B1)S2(B2)S3(B3)S4(B4)S5(B5)S6(B6)S7(B7)S8(B8)

        String S1B1 = method_s(xor_result.substring(0,6),S1);
        String S2B2 = method_s(xor_result.substring(6,6+6),S2);
        String S3B3 = method_s(xor_result.substring(12,12+6),S3);
        String S4B4 = method_s(xor_result.substring(18,18+6),S4);
        String S5B5 = method_s(xor_result.substring(24,24+6),S5);
        String S6B6 = method_s(xor_result.substring(30,30+6),S6);
        String S7B7 = method_s(xor_result.substring(36,36+6),S7);
        String S8B8 = method_s(xor_result.substring(42,42+6),S8);

        strBuilder.append(S1B1);
        strBuilder.append(S2B2);
        strBuilder.append(S3B3);
        strBuilder.append(S4B4);
        strBuilder.append(S5B5);
        strBuilder.append(S6B6);
        strBuilder.append(S7B7);
        strBuilder.append(S8B8);

        // strBuilder holds bits of size 32

        String msg32bits = strBuilder.toString();

        //just permutation
        String result = doPermutation(msg32bits,P_MATRIX_MSG);

        System.out.printf("function f is done, result :  %s \n",result);
        return result;
    }

    //convert six(6) bits to four(4) bits,
    private String method_s(String bitGroup, int[][] arr) {
        String s_row = String.valueOf(bitGroup.charAt(0)) + bitGroup.charAt(5);
        int row = Integer.parseInt(s_row,2);
        int col = Integer.parseInt(bitGroup.substring(1,5),2);
        String result = Integer.toBinaryString(arr[row][col]);

        return repeatNtimes(4- (result.length()%4),"0") + result;
        //return Integer.toBinaryString(arr[row][col]);
    }


    //ready-OK
    //CREATE 16 SUBKEYS THAT OF LENGTH 48 BITS LONG
    private void prepareKeySet (String keyPlain){

        System.out.println("CREATING 16 SUBKEYS THAT OF LENGTH 48 BITS LONG : PROCESSING");
        //String binaryKey= hex2Binary(keyhex);

        String binaryKey= toBinary(keyPlain);

        //adding trailing zeros to head of tail
        binaryKey= repeatNtimes(64 - binaryKey.length()%64,"0") + binaryKey;

        //do permutation, and get Kplus of 56 bits --effective bits in key, get rid off parity ones
        String keyPlus = doPermutation(binaryKey,PC_1);

        //Next, split this key into left and right halves
        keyLeftPart[0] = keyPlus.substring(0,28); //C0
        keyRightPart[0] = keyPlus.substring(28,56); //D0

        for (int iter = 1; iter <= 16; iter++) {
            keyLeftPart[iter] = doLeftRotation(keyLeftPart[iter-1],shifts_iter[iter]); //Cn
            keyRightPart[iter] = doLeftRotation(keyRightPart[iter-1],shifts_iter[iter]); //Dn
        }

        /*
        We now form the keys Kn, for 1<=n<=16,
        by applying the following permutation table to each of the concatenated pairs CnDn.
         Each pair has 56 bits, but PC-2 only uses 48 of these.
        */

        for (int i = 1; i <=16 ; i++) {
            //get subkey whose size is 48 bits,
            //after permutation, 56 bits -> 48bits
            subkeys[i] = doPermutation(keyLeftPart[i] + keyRightPart[i],PC_2);
        }
//        IntStream.rangeClosed(1,16)
//                .forEach((i)-> subkeys[i] = doPermutation(keyLeftPart[i] + keyRightPart[i],PC_2));

        IntStream.rangeClosed(1,16).forEach((i)-> System.out.printf("K[%2d] : %s \n",i,subkeys[i]));

        System.out.println("CREATING 16 SUBKEYS THAT OF LENGTH 48 BITS LONG : DONE ");
    }


    //ready -OK
    private String doLeftRotation(String key, int shifts){
        return key.substring(shifts) + key.substring(0,shifts);
    }

    //ready-OK
    private String doRightRotation(String key, int shifts){
        return key.substring(key.length()-shifts) + key.substring(0,key.length()-2);
    }

    //ready - OK
    private static String unHex(String hex) throws Exception{

//        StringBuilder stringBuilder = new StringBuilder();
//        for(int i = 0; i < hex.length();i+= 2)
//        {
//            String s = hex.substring(i, (i + 2));
//            int decimal = Integer.parseInt(s, 16);
//            stringBuilder.append((char) decimal);
//        }
//        return stringBuilder.toString();

        byte[] bytes = Hex.decodeHex(hex.toCharArray());
        return new String(bytes, StandardCharsets.UTF_8);
    }

    //ready - OK
    private static String toBinary(String text){

        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes)
        {
            int val = b;
            for (int i = 0; i < 8; i++)
            {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            //binary.append(' ');
        }
        return binary.toString();

        /*
        return  text.toCharArray()
                .mapToInt((i)->i)
                .mapToObj((ch)->Integer.toBinaryString(ch))
                .collect(StringBuilder::new,StringBuilder::append,StringBuilder::append).toString();*/

       /* StringBuilder sb = new StringBuilder();

        for (char character : text.toCharArray()) {
            sb.append(Integer.toBinaryString(character) + "\n");
        }

        return sb.toString();*/

    }

    //ready - OK
    private static String hex2Binary(String hex) {
        //return Integer.toBinaryString(Integer.parseInt(hex,16));
        //return new BigInteger(hex, 16).toString(2);
        hex = hex.replaceAll("0", "0000");
        hex = hex.replaceAll("1", "0001");
        hex = hex.replaceAll("2", "0010");
        hex = hex.replaceAll("3", "0011");
        hex = hex.replaceAll("4", "0100");
        hex = hex.replaceAll("5", "0101");
        hex = hex.replaceAll("6", "0110");
        hex = hex.replaceAll("7", "0111");
        hex = hex.replaceAll("8", "1000");
        hex = hex.replaceAll("9", "1001");
        hex = hex.replaceAll("A", "1010");
        hex = hex.replaceAll("B", "1011");
        hex = hex.replaceAll("C", "1100");
        hex = hex.replaceAll("D", "1101");
        hex = hex.replaceAll("E", "1110");
        hex = hex.replaceAll("F", "1111");
        return hex;
    }

    private String doPermutation(String bitSequence, int[] arr) {
        StringBuilder keyBuilder = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            keyBuilder.append(bitSequence.charAt(arr[i] - 1 ));
        }
        return keyBuilder.toString();
    }

    //ready - OK
    private static String repeatNtimes(int n,String str){
        if(n < 0) throw new IllegalArgumentException("argument cannot be negative");
        if (n == 0)return str;
        StringBuilder stringBuilder = new StringBuilder(n);
        IntStream.rangeClosed(1,n).forEach((i)->stringBuilder.append(str));
        return stringBuilder.toString();
    }

    //ready - OK
    private static String toHex(String text) throws Exception {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        //System.out.println( Hex.encodeHexString( bytes ) );
       // return String.format("%x", new BigInteger(1, text.getBytes("UTF-8")));
        return Hex.encodeHexString( bytes );
    }

    //holds matrices,constants
    static class Helper {
        //for preparing key
         static final int PC_1 [] =  {57,   49,    41,   33,    25,    17,    9,
                1,    58,    50,   42,    34,    26,   18,
                10,    2,    59,   51,    43,    35,   27,
                19,   11,     3,   60,    52,    44,   36,
                63,   55,    47,   39,    31,    23,   15,
                7,    62,    54,   46,    38,    30,   22,
                14,    6,    61,   53,    45,    37,   29,
                21,   13,     5,   28,    20,    12,    4 };

        //for preparing subkeys
         static final int PC_2 [] = {14,    17,   11,    24,     1,    5,
                3,     28,   15,     6,    21,   10,
                23,    19,   12,     4,    26,    8,
                16,     7,   27,    20,    13,    2,
                41,    52,   31,    37,    47,   55,
                30,    40,   51,    45,    33,   48,
                44,    49,   39,    56,    34,   53,
                46,    42,   50,    36,    29,   32 };

        //initial permutation for message
         static final int IP[] = { 58,    50,   42,    34,    26,   18,    10,    2,
                60,    52,   44,    36,    28,   20,    12,    4,
                62,    54,   46,    38,    30,   22,    14,    6,
                64,    56,   48,    40,    32,   24,    16,    8,
                57,    49,   41,    33,    25,   17,     9,    1,
                59,    51,   43,    35,    27,   19,    11,    3,
                61,    53,   45,    37,    29,   21,    13,    5,
                63,    55,   47,    39,    31,   23,    15,    7 };

        //upgrade on size of bits, 32 -> 48
         static final int BIT_E[] = {  32,     1,     2,     3,     4,      5,
                4,     5,     6,     7,     8,      9,
                8,     9,     10,    11,    12,     13,
                12,    13,     14,    15,    16,     17,
                16,    17,     18,    19,    20,     21,
                20,    21,     22,    23,    24,     25,
                24,    25,     26,    27,    28,     29,
                28,    29,     30,    31,    32,      1 };

        //message permutation of length 32, means after S_MATRIX applied
         static final int[] P_MATRIX_MSG = { 16,   7,  20,  21,
                29,  12,  28,  17,
                1,   15,  23,  26,
                5,   18,  31,  10,
                2,    8,  24,  14,
                32,  27,   3,   9,
                19,  13,  30,   6,
                22,  11,   4,  25 };

        //the last permutation for message
         static final int[] IP_MATRIX_MSG ={ 40,     8,   48,    16,    56,   24,    64,   32,
                39,     7,   47,    15,    55,   23,    63,   31,
                38,     6,   46,    14,    54,   22,    62,   30,
                37,     5,   45,    13,    53,   21,    61,   29,
                36,     4,   44,    12,    52,   20,    60,   28,
                35,     3,   43,    11,    51,   19,    59,   27,
                34,     2,   42,    10,    50,   18,    58,   26,
                33,     1,   41,     9,    49,   17,    57,   25 };


        //iterations used  in key generations
        static final int[] shifts_iter = {Integer.MIN_VALUE,1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};

        //conversion from six(6) bits group to four(4) bits, related to msg processing
        static final int[][] S1 =   { { 14,  4,  13,  1,   2, 15,  11,  8,   3, 10,   6, 12,   5,  9,   0,  7},
                { 0,  15,   7,  4,  14,  2,  13,  1,  10,  6,  12, 11,   9,  5,   3,  8},
                { 4,   1,  14,  8,  13,  6,   2, 11,  15, 12,   9,  7,   3, 10,   5,  0,},
                { 15, 12,   8,  2,   4,  9,   1,  7,   5, 11,   3, 14,  10,  0,   6, 13}} ;


        static final int[][] S2 =     {{ 15  ,1,   8, 14   ,6, 11,   3,  4  , 9,  7,   2, 13  ,12 , 0 ,  5, 10 },
                                {3, 13,   4 , 7 , 15,  2 ,  8, 14 , 12 , 0 ,  1 ,10 ,  6 , 9 , 11 , 5},
                                {0, 14,   7 ,11 , 10 , 4 , 13 , 1 ,  5 , 8 , 12 , 6  , 9 , 3  , 2 ,15},
                                {13,  8,  10,  1 ,  3, 15,   4 , 2 , 11,  6 ,  7, 12,   0 , 5 , 14 , 9 }};

       static final int[][] S3 =   { { 10,  0,   9 ,14 ,  6 , 3 , 15 , 5 ,  1, 13 , 12,  7 , 11,  4 ,  2,  8},
                               {13 , 7 ,  0 , 9 ,  3 , 4 ,  6, 10 ,  2 , 8 ,  5 ,14 , 12, 11 , 15 , 1},
                               {13 , 6 ,  4 , 9 ,  8, 15 ,  3 , 0 , 11 , 1  , 2, 12 ,  5, 10 , 14,  7},
                               {1 ,10 , 13 , 0 ,  6  ,9  , 8,  7  , 4, 15 , 14 , 3  ,11 , 5 ,  2, 12}};

        static final int[][] S4 = {{7 ,13 , 14 , 3 ,  0 , 6 ,  9 ,10 ,  1,  2,   8,  5,  11, 12 ,  4, 15},
                                {13  ,8  ,11,  5 ,  6 ,15   ,0,  3  , 4 , 7  , 2, 12 ,  1 ,10 , 14 , 9},
                                {10 , 6  , 9  ,0,  12, 11 ,  7, 13 , 15,  1 ,  3, 14  , 5,  2  , 8 , 4},
                                {3, 15 ,  0 , 6 , 10 , 1 , 13 , 8 ,  9 , 4  , 5 ,11 , 12,  7  , 2, 14}};

        static final int[][] S5={{ 2 ,12 ,  4 , 1  , 7, 10 , 11 , 6 ,  8  ,5   ,3 ,15 , 13 , 0 , 14 , 9},
                                {14 ,11,   2, 12 ,  4 , 7 , 13,  1  , 5 , 0,  15, 10  , 3 , 9 ,  8 , 6},
                                {4 , 2  , 1, 11 , 10, 13  , 7,  8,  15 , 9 , 12 , 5 ,  6 , 3 ,  0, 14},
                                {11 , 8  ,12 , 7  , 1, 14 ,  2, 13 ,  6 ,15  , 0,  9 , 10 , 4  , 5 , 3}};


        static final int[][] S6 = {{12 , 1 , 10 ,15  , 9 , 2  , 6 , 8  , 0 ,13 ,  3  ,4  ,14 , 7   ,5 ,11},
                                { 10, 15 ,  4 , 2 ,  7 ,12 ,  9 , 5,   6  ,1 , 13, 14  , 0 ,11   ,3 , 8},
                                {9, 14 , 15 , 5  , 2 , 8 , 12 , 3   ,7  ,0 ,  4 ,10 ,  1 ,13,  11 , 6},
                                {4 , 3  , 2, 12 ,  9  ,5  ,15 ,10,  11, 14,   1 , 7 ,  6  ,0 ,  8 ,13}};

        static final int[][] S7 = {{ 4, 11,   2 ,14 , 15 , 0 ,  8, 13 ,  3, 12 ,  9 , 7,   5 ,10 ,  6 , 1},
                                { 13 , 0 , 11 , 7 ,  4 , 9 ,  1 ,10 , 14,  3 ,  5, 12  , 2 ,15  , 8 , 6},
                                { 1 , 4 , 11, 13  ,12 , 3   ,7 ,14 , 10, 15   ,6,  8  , 0 , 5 ,  9 , 2},
                                {6 ,11 , 13 , 8 ,  1 , 4 , 10 , 7 ,  9 , 5  , 0 ,15 , 14 , 2 ,  3 ,12}};

        static final int[][]S8 = {{ 13 , 2  , 8  ,4 ,  6, 15 , 11,  1  ,10 , 9  , 3, 14 ,  5 , 0 , 12 , 7},
                                    {1, 15 , 13,  8,  10 , 3 ,  7 , 4 , 12 , 5,   6 ,11  , 0, 14  , 9 , 2},
                                    {7 ,11,   4 , 1 ,  9 ,12 , 14 , 2  , 0 , 6 , 10, 13 , 15,  3 ,  5,  8},
                                    {2 , 1 , 14 , 7 ,  4 ,10 ,  8, 13 , 15 ,12 ,  9 , 0,   3,  5,   6, 11 }};
        }
}
