package org.figuramc.fwl.gui.themes;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class FWLThemeRepository {
    private final HashMap<ResourceLocation, ThemeFactory> factories = new HashMap<>();

    public interface ThemeFactory {
        FWLTheme get(@Nullable JsonElement element);
    }
}
