package org.figuramc.fwl.gui.themes;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import org.figuramc.fwl.utils.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;

public class FWLThemeRepository {
    private final HashMap<ResourceLocation, ThemeFactory> factories = new HashMap<>();

    public interface ThemeFactory {
        FWLTheme get(@Nullable JsonElement element);
    }

    void registerTheme(ResourceLocation location, ThemeFactory factory) {
        if (factories.containsKey(location)) throw new IllegalArgumentException("Theme factory with ID %s already exists".formatted(location));
    }

    void unregisterTheme(ResourceLocation location) {
        factories.remove(location);
    }

    public @Nullable ThemeFactory getThemeFactory(ResourceLocation location) {
        return factories.get(location);
    }

    public Iterator<Pair<ResourceLocation, ThemeFactory>> getRegisteredThemes() {
        return factories.entrySet().stream().map(entry -> new Pair<>(entry.getKey(), entry.getValue())).iterator();
    }
}
