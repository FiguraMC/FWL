package org.figuramc.fwl.utils;

import net.minecraft.util.FormattedCharSequence;

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

    public static FormattedCharSequence substr(FormattedCharSequence sequence, int beginIndex, int endIndex) {
        return (sink) -> sequence.accept((index, style, codepoint) -> {
            if (index >= beginIndex) {
                if (index < endIndex) return sink.accept(index, style, codepoint);
                else return false;
            }
            else return true;
        });
    }
}
