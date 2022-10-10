package youtube.java.locker;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Log
public class Utils {

    public static String toAlphabetic(int i) {
        if( i<0 ) {
            return "-"+toAlphabetic(-i-1);
        }

        int quotient = i/26;
        int remainder = i%26;
        char letter = (char)((int)'A' + remainder);
        if( quotient == 0 ) {
            return "" + letter;
        } else {
            return toAlphabetic(quotient-1) + letter;
        }
    }

    public static int toNumeric(String s) {
        if( s==null || s.length()==0 || isNumeric(s) ) {
            throw new IllegalArgumentException("Invalid input: [" + s + "] for toNumeric");
        }

        int result = 0;
        for (int i = 0; i < s.length(); i++) {
            result *= 26;
            result += s.charAt(i) - 'A' + 1;
        }
        return result;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static boolean isString(String str) {
        return str.matches("[a-zA-Z]+");  //match a number with optional '-' and decimal.
    }
}
