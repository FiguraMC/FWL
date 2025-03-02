package org.figuramc.fwl.entries.fwl;

import org.figuramc.fwl.entries.ThemeRegistrar;
import org.figuramc.fwl.gui.themes.FWLAdwaita;
import org.figuramc.fwl.gui.themes.FWLBreeze;
import org.figuramc.fwl.gui.themes.ThemeRepositoryAccess;

public class StandardFWLThemeRegistrar implements ThemeRegistrar {
    @Override
    public void registerThemes(ThemeRepositoryAccess repositoryAccess) {
        repositoryAccess.registerTheme("breeze", FWLBreeze::new);
        repositoryAccess.registerTheme("adwaita", FWLAdwaita::new);
    }
}
