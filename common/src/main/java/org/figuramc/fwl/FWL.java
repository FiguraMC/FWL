package org.figuramc.fwl;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import org.figuramc.fwl.config.FWLConfig;
import org.figuramc.fwl.entries.ThemeRegistrar;
import org.figuramc.fwl.gui.themes.FWLTheme;
import org.figuramc.fwl.gui.themes.FWLThemeRepository;
import org.figuramc.fwl.gui.themes.ThemeRepositoryAccess;
import org.figuramc.fwl.utils.IOUtils;
import org.figuramc.fwl.utils.Pair;
import org.figuramc.fwl.utils.ResourceLocationSerializer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.Set;

public abstract class FWL {
    // ResLoc of the theme that will be used in case if config doesn't exist or for some reason theme field in it is empty or absent
    public static final ResourceLocation DEFAULT_THEME = new ResourceLocation("fwl", "breeze");
    private static final Path CONFIG_FOLDER_PATH = Path.of("config/fwl");

    private static FWL INSTANCE;

    private final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocationSerializer()).create();

    private final FWLThemeRepository themeRepository = new FWLThemeRepository();
    public final Logger LOGGER = LoggerFactory.getLogger("FWL");

    private FWLConfig config;

    private FWLTheme currentTheme;

    protected final void init() {
        if (INSTANCE != null) throw new IllegalStateException("FWL is already initialized");
        INSTANCE = this;

        config = readConfig();

        Set<Pair<String, ThemeRegistrar>> themeRegistrars = getThemeRegistrars();
        for (Pair<String, ThemeRegistrar> modIdAndRegistrar: themeRegistrars) {
            String modId = modIdAndRegistrar.a();
            ThemeRegistrar registrar = modIdAndRegistrar.b();
            ThemeRepositoryAccess access = new ThemeRepositoryAccess(modId, themeRepository);
            registrar.registerThemes(access);
        }

        setCurrentTheme(config.selectedTheme());
    }

    private boolean createConfigFolder() {
        File folder = CONFIG_FOLDER_PATH.toFile();
        return folder.mkdirs();
    }

    private boolean createConfigFolder(ResourceLocation resLoc) {
        File folder = CONFIG_FOLDER_PATH.resolve(resLoc.getNamespace()).toFile();
        return folder.mkdirs();
    }

    private FWLConfig readConfig() {
        Path configFilePath = CONFIG_FOLDER_PATH.resolve("config.json");
        File configFile = configFilePath.toFile();
        String configContents = IOUtils.readString(configFile);
        if (configContents != null) {
            try {
                return GSON.fromJson(configContents, FWLConfig.class);
            } catch (JsonSyntaxException ignored) {}
        }
        return new FWLConfig();
    }

    public void saveConfig() {
        Path configFilePath = CONFIG_FOLDER_PATH.resolve("config.json");
        File configFile = configFilePath.toFile();
        String configContents = GSON.toJson(config);
        createConfigFolder();
        IOUtils.writeString(configFile, configContents);
    }

    private Path getThemeConfig(ResourceLocation path) {
        return CONFIG_FOLDER_PATH.resolve("%s/%s.json".formatted(path.getNamespace(), path.getPath()));
    }

    private @Nullable JsonObject readThemeConfig(ResourceLocation path) {
        File configFile = getThemeConfig(path).toFile();
        String contents = IOUtils.readString(configFile);
        if (contents != null) {
            try {
                return GSON.fromJson(contents, JsonObject.class);
            }
            catch (JsonSyntaxException ignored) {}
        }
        return null;
    }

    public void saveCurrentThemeConfig() {
        createConfigFolder(config.selectedTheme());
        Path configPath = getThemeConfig(config.selectedTheme());
        File configFile = configPath.toFile();
        JsonObject preset = currentTheme.savePreset();
        String configContents = GSON.toJson(preset);
        if (!IOUtils.writeString(configFile, configContents)) {
            LOGGER.error("Error occurred while writing config file by path: {}", configPath);
        }
    }

    protected abstract Set<Pair<String, ThemeRegistrar>> getThemeRegistrars();

    public FWLThemeRepository themeRepository() {
        return themeRepository;
    }

    public static FWL fwl() {
        return INSTANCE;
    }

    public FWLTheme setCurrentTheme(ResourceLocation theme) {
        FWLThemeRepository.ThemeFactory themeFactory = themeRepository.getThemeFactory(theme);
        if (themeFactory != null) {
            config.setSelectedTheme(theme);
            currentTheme = themeFactory.get(readThemeConfig(theme));
            saveConfig();
            return currentTheme;
        }
        else throw new RuntimeException("Unable to find theme %s in registrars from mod %s".formatted(theme.getPath(), theme.getNamespace()));
    }

    public FWLTheme currentTheme() {
        return currentTheme;
    }

    public FWLConfig config() {
        return config;
    }
}
