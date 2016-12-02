package com.tg.tiny4j.commons.utils;

/**
 * Created by twogoods on 16/10/26.
 */
public class StringUtil {

    private static int charRange = 'a' - 'A';

    public static String firstCharUpperCase(String s) {
        char[] arr = s.toCharArray();
        if (arr[0] >= 'a' && arr[0] <= 'z') {
            arr[0] -= charRange;
        }
        return String.valueOf(arr);
    }

    public static String firstCharLowercase(String s) {
        char[] arr = s.toCharArray();
        if (arr[0] >= 'A' && arr[0] <= 'Z') {
            arr[0] += charRange;
        }
        return String.valueOf(arr);
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null || "".equals(obj)) {
            return true;
        }
        return false;
    }
}
