package org.figuramc.fwl.gui.themes;

import net.minecraft.resources.ResourceLocation;
import org.figuramc.fwl.FWL;

public final class ThemeRepositoryAccess {
    private final String modId;
    private final FWLThemeRepository repository;

    public ThemeRepositoryAccess(String modId, FWLThemeRepository repository) {
        this.modId = modId;
        this.repository = repository;
    }

    public void registerTheme(String themeId, FWLThemeRepository.ThemeFactory factory) {
        repository.registerTheme(getResLoc( themeId), factory);
        FWL.fwl().LOGGER.info("Registered theme {} for mod {}", themeId, modId);
    }

    public void unregisterTheme(String themeId) {
        repository.unregisterTheme(getResLoc( themeId));
    }

    public ResourceLocation getResLoc(String path) {
        return new ResourceLocation(modId, path);
    }
}
