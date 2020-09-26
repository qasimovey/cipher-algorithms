package cipher;

public class VigenereCipher {
    public static final int letterCount=26;

    public static String encyrpt(String plainText,String key){
        StringBuilder sBuilder=new StringBuilder();

        for (int i = 0; i < plainText.length(); i++) {
            int rowPos = key.charAt(i%key.length()) - 'A';
            int colPos = plainText.charAt(i) - 'A' ;
            //Ei = (Pi + Ki) mod 26
            int newSymbol= 'A' +( rowPos+colPos)%letterCount;
            sBuilder.append((char)newSymbol);
        }
        return sBuilder.toString();
    }
    public static String decyrpt(String chiperText,String key){
        StringBuilder sBuilder=new StringBuilder();

        for (int i = 0; i < chiperText.length(); i++) {
            int rowPos = key.charAt(i%key.length()) - 'A';
            int cellPos = chiperText.charAt(i) - 'A' ;

            //Di = (Ei - Ki) mod 26
            int newSymbol = 'A' + ((cellPos - rowPos)<0 ? cellPos - rowPos + letterCount:(cellPos - rowPos))%letterCount;
            sBuilder.append((char)newSymbol);
        }
        return sBuilder.toString();
    }
}
