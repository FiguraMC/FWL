package org.figuramc.fwl.utils;

public class TextUtils {
    public static int findCursorJumpAfter(String str, int ind) {
        int len = str.length();
        if (ind >= len) return len;
        int i = ind + 1;
        for (; i < len; i++) {
            char c = str.charAt(i);
            if (Character.isWhitespace(c)) return i;
        }
        return i;
    }

    public static int findCursorJumpBefore(String str, int ind) {
        if (ind <= 0) return 0;
        ind = Math.min(str.length(), ind);
        int i = ind - 1;
        for (; i >= 0; i--) {
            char c = str.charAt(i);
            if (Character.isWhitespace(c)) return i;
        }
        return i;
    }
}
