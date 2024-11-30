package org.figuramc.fwl;

import org.figuramc.fwl.gui.themes.FWLTheme;

import java.util.Stack;

public class FWL {
    private static final Stack<FWLTheme> themeStack = new Stack<>();

    public static FWLTheme pushTheme(FWLTheme theme) {
        return themeStack.push(theme);
    }

    public static FWLTheme popTheme() {
        return themeStack.pop();
    }

    public static FWLTheme peekTheme() {
        return themeStack.peek();
    }
}
