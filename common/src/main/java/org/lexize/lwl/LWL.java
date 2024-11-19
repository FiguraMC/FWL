package org.lexize.lwl;

import org.lexize.lwl.themes.LWLTheme;

import java.util.Arrays;
import java.util.Stack;

public class LWL {
    private static final Stack<LWLTheme> themeStack = new Stack<>();

    public static LWLTheme pushTheme(LWLTheme theme) {
        return themeStack.push(theme);
    }

    public static LWLTheme popTheme() {
        return themeStack.pop();
    }

    public static LWLTheme peekTheme() {
        return themeStack.peek();
    }
}
