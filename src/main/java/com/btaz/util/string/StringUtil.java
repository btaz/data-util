package com.btaz.util.string;

/**
 * User: msundell
 */
public class StringUtil {
    private StringUtil() {}

    /**
     * This method will pad a String with a character or string
     * @param repeat how many times the string should be repeated
     * @param str character or string to be repeated
     * @return padded {@code String}
     */
    public static String pad(int repeat, String str) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < repeat; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
