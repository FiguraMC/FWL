package org.figuramc.fwl.config;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.ResourceLocation;
import org.figuramc.fwl.FWL;

import java.lang.reflect.Type;

import static org.figuramc.fwl.utils.JsonUtils.stringOrDefault;

public class FWLConfig {
    @SerializedName("selected_theme")
    private ResourceLocation selectedTheme = FWL.DEFAULT_THEME;
    @SerializedName("super_secret_setting")
    private boolean superSecretSetting;

    public ResourceLocation selectedTheme() {
        return selectedTheme;
    }

    public FWLConfig setSelectedTheme(ResourceLocation selectedTheme) {
        this.selectedTheme = selectedTheme;
        return this;
    }

    public boolean superSecretSetting() {
        return superSecretSetting;
    }

    public FWLConfig setSuperSecretSetting(boolean superSecretSetting) {
        this.superSecretSetting = superSecretSetting;
        return this;
    }
}
