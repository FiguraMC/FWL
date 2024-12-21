package org.figuramc.fwl;

import org.figuramc.fwl.gui.themes.FWLBreeze;
import org.figuramc.fwl.gui.themes.FWLTheme;
import org.figuramc.fwl.gui.themes.FWLThemeRepository;

import java.util.Stack;

public class FWL {
    private static FWL INSTANCE;

    private FWLTheme currentTheme = new FWLBreeze();
    private final FWLThemeRepository themeRepository = new FWLThemeRepository();

    protected final void init() {
        if (INSTANCE != null) throw new IllegalStateException("FWL is already initialized");
        INSTANCE = this;
    }

    public FWLThemeRepository themeRepository() {
        return themeRepository;
    }

    public static FWL fwl() {
        return INSTANCE;
    }

    public <T extends FWLTheme> T setCurrentTheme(T theme) {
        currentTheme = theme;
        return theme;
    }

    public FWLTheme currentTheme() {
        return currentTheme;
    }
}
