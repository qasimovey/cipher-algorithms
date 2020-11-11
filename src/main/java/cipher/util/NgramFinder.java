package cipher.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NgramFinder {


    private static List<String> splitEqually(String text, int size) {
        // Give the list the right capacity to start with.
        List<String> ret = new ArrayList<>((text.length() + size - 1) / size);

        for (int start = 0; start < text.length() -size; start += size) {
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        }
        return ret;
    }

    private static int countWord(String text,String word) {
        int count = 0;

        //iterate over text and counts how many times occur
        for (int i = 0; i < text.length() - word.length() ; i = i + word.length()) {
            if(word.equals(text.substring(i, i + word.length()))){
                count++;
            }
        }
        return count;
    }

    public static HashMap<String,Integer> countNgram(String text, int n){
        HashMap<String,Integer> map = new HashMap<>();
        List<String> stringList = splitEqually(text, n);

        for(String str : stringList){
            map.put(str,countWord(text,str));
        }
        return map;
    }


}
