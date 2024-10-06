package com.booyue.base.util;

/**
 * @author: wangxinhua
 * @date: 2019/9/20
 * @description :
 */
public class UnicodeUtil {
    private static final String TAG = "UnicodeUtil";

    public static String encoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        LoggerUtils.d(TAG,"unicodeBytes is: " + unicodeBytes);
        return unicodeBytes;
    }
}
