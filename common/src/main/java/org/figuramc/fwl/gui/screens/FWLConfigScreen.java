package org.figuramc.fwl.gui.screens;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.figuramc.fwl.config.FWLConfig;
import org.figuramc.fwl.gui.themes.FWLTheme;
import org.figuramc.fwl.gui.themes.FWLThemeRepository;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.gui.widgets.Resizeable;
import org.figuramc.fwl.gui.widgets.Update;
import org.figuramc.fwl.gui.widgets.button.Checkbox;
import org.figuramc.fwl.gui.widgets.button.TextButton;
import org.figuramc.fwl.gui.widgets.containers.AbstractFWLContainerWidget;
import org.figuramc.fwl.gui.widgets.containers.FWLContainerWidget;
import org.figuramc.fwl.gui.widgets.misc.ContextMenu;
import org.figuramc.fwl.gui.widgets.misc.Label;
import org.figuramc.fwl.gui.widgets.tabs.SideViewSwitcher;
import org.figuramc.fwl.gui.widgets.tabs.pages.PageEntry;
import org.figuramc.fwl.utils.Rectangle;
import org.jetbrains.annotations.Nullable;

import static org.figuramc.fwl.FWL.fwl;
import static org.figuramc.fwl.utils.TextUtils.themeToTranslationString;

public class FWLConfigScreen extends FWLScreen {
    private SideViewSwitcher configSwitcher;

    public FWLConfigScreen(Screen prevScreen) {
        super(Component.translatable("fwl.config_screen"), prevScreen);
    }

    public FWLConfigScreen() {
        this((Screen) null);
    }

    @Override
    protected void init() {
        super.init();
        configSwitcher = new SideViewSwitcher(0, 0, width, height)
                .setEntryHeight(16)
                .setExpandedWidth(100)
                .addEntry(new FWLSettingsPageEntry())
                .addEntry(new ThemeSettingsPageEntry());
        addWidget(configSwitcher);
    }

    private static class FWLSettingsPageEntry implements PageEntry {
        private FWLSettingsPage page;
        @Override
        public FWLWidget getPage(float width, float height) {
            page = new FWLSettingsPage(width, height);
            return page;
        }

        @Override
        public Component getTitle() {
            return Component.translatable("fwl.config.settings");
        }

        @Override
        public @Nullable Component getTooltip() {
            return Component.translatable("fwl.config.settings.tooltip");
        }
    }

    private static class ThemeSettingsPageEntry implements PageEntry {
        private ThemeSettingsPage page;
        @Override
        public FWLWidget getPage(float width, float height) {
            page = new ThemeSettingsPage(width, height);
            return page;
        }

        @Override
        public Component getTitle() {
            return Component.translatable("fwl.config.theme_settings");
        }

        @Override
        public @Nullable Component getTooltip() {
            return Component.translatable("fwl.config.theme_settings.tooltip");
        }

        @Override
        public void onClose() {
            page.cancelChanges();
        }
    }

    private static class FWLSettingsPage extends AbstractFWLContainerWidget implements Resizeable {
        private float width, height;

        private TextButton changeThemeButton;

        @Override
        public void resize(float width, float height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public Rectangle boundaries() {
            return new Rectangle(0, 0, width, height);
        }

        public FWLSettingsPage(float width, float height) {
            this.width = width;
            this.height = height;
            float currentY = 10;
            currentY += createThemeDropdown(10, currentY) + 10;
            currentY += createSuperSecretSettings(10, currentY) + 10;
        }

        private float createThemeDropdown(float x, float y) {
            FWLConfig config = fwl().config();
            Style labelStyle = Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("fwl.config.settings.selected_theme.tooltip")));
            Label buttonLabel = new Label(x, y, Component.translatable("fwl.config.settings.selected_theme").withStyle(labelStyle));
            changeThemeButton =
                    new TextButton(buttonLabel.boundaries().right() + 10, y, 100, 20, Component.translatable(themeToTranslationString(config.selectedTheme())));
            changeThemeButton.setCallback(this::changeThemeCallback);
            addWidget(buttonLabel);
            addWidget(changeThemeButton);
            return 20;
        }

        private float createSuperSecretSettings(float x, float y) {
            Style labelStyle = Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("fwl.config.settings.super_secret_toggle.tooltip")));
            Label toggleLabel = new Label(x, y, Component.translatable("fwl.config.settings.super_secret_toggle").withStyle(labelStyle));
            Checkbox checkbox = new Checkbox(toggleLabel.boundaries().right() + 10, y, 20, 20, false);
            addWidget(toggleLabel);
            addWidget(checkbox);
            return 20;
        }

        private void changeThemeCallback(float x, float y, int button) {
            if (button == 0) {
                ContextMenu menu = new ContextMenu(this, x, y);
                FWLThemeRepository themes = fwl().themeRepository();
                themes.getRegisteredThemes().forEachRemaining(p -> {
                    menu.addEntry(new ThemeSwitchEntry(p.a(), changeThemeButton));
                });
                menu.setInteractionPriority(1000).setRenderPriority(1000);
                addWidget(menu);
            }
        }

        private static class ThemeSwitchEntry implements ContextMenu.Entry {
            private final ResourceLocation theme;
            private final TextButton parentButton;
            private final Component text;

            private ThemeSwitchEntry(ResourceLocation theme, TextButton parentButton) {
                this.theme = theme;
                this.parentButton = parentButton;
                text = Component.translatable(themeToTranslationString(theme));
            }

            @Override
            public Component text() {
                return text;
            }

            @Override
            public boolean onInteraction(int modifiers) {
                fwl().setCurrentTheme(theme);
                fwl().saveConfig();
                parentButton.setMessage(text);
                return false;
            }
        }
    }

    private static class ThemeSettingsPage extends AbstractFWLContainerWidget implements Resizeable {
        private static final Component RESET_BUTTON_COMPONENT = Component.translatable("fwl.config.theme_settings.reset");
        private static final Component RESET_BUTTON_COMPONENT_TOOLTIP = Component.translatable("fwl.config.theme_settings.reset.tooltip");
        private static final Component CANCEL_BUTTON_COMPONENT = Component.translatable("fwl.config.theme_settings.cancel");
        private static final Component CANCEL_BUTTON_COMPONENT_TOOLTIP = Component.translatable("fwl.config.theme_settings.cancel.tooltip");
        private static final Component SAVE_BUTTON_COMPONENT = Component.translatable("fwl.config.theme_settings.save");
        private static final Component SAVE_BUTTON_COMPONENT_TOOLTIP = Component.translatable("fwl.config.theme_settings.save.tooltip");
        private final float BUTTONS_ROW_SIZE = 30;
        private float width, height;
        private final FWLWidget themeSettings;
        private final TextButton resetButton;
        private final TextButton cancelButton;
        private final TextButton saveButton;
        private final FWLTheme theme;
        private JsonObject savedPreset;

        public ThemeSettingsPage(float width, float height) {
            this.width = width;
            this.height = height;

            this.theme = fwl().currentTheme();

            this.savedPreset = theme.savePreset();

            float centerX = width / 2;
            float button1X = centerX - ((102 * 3) / 2f);
            float button2X = button1X + 102;
            float button3X = button2X + 102;
            float buttonY = (height - BUTTONS_ROW_SIZE) + 5;

            addWidget(themeSettings = theme.settingsRootWidget(width, height - BUTTONS_ROW_SIZE));
            addWidget(resetButton = new TextButton(button1X, buttonY, 100, 20, RESET_BUTTON_COMPONENT)
                    .setTooltip(RESET_BUTTON_COMPONENT_TOOLTIP).setCallback(this::resetButtonCallback));
            addWidget(cancelButton = new TextButton(button2X, buttonY, 100, 20, CANCEL_BUTTON_COMPONENT)
                    .setTooltip(CANCEL_BUTTON_COMPONENT_TOOLTIP).setCallback(this::cancelButtonCallback));
            addWidget(saveButton = new TextButton(button3X, buttonY, 100, 20, SAVE_BUTTON_COMPONENT)
                    .setTooltip(SAVE_BUTTON_COMPONENT_TOOLTIP).setCallback(this::saveButtonCallback));
        }

        @Override
        public Rectangle boundaries() {
            return new Rectangle(0, 0, width, height);
        }

        @Override
        public void resize(float width, float height) {
            this.width = width;
            this.height = height;
            float centerX = width / 2;
            float button1X = centerX - ((102 * 3) / 2f);
            float button2X = button1X + 102;
            float button3X = button2X + 102;
            float buttonY = (height - BUTTONS_ROW_SIZE) + 5;
            if (themeSettings instanceof Resizeable res) res.resize(width, height - BUTTONS_ROW_SIZE);
            resetButton.setX(button1X).setY(buttonY);
            cancelButton.setX(button2X).setY(buttonY);
            saveButton.setX(button3X).setY(buttonY);
        }

        private void resetButtonCallback(float x, float y, int button) {
            if (button == 0) {
                theme.applyPreset(null);
                if (themeSettings instanceof Update upd) upd.update();
            }
        }

        private void cancelButtonCallback(float x, float y, int button) {
            if (button == 0) {
                cancelChanges();
                if (themeSettings instanceof Update upd) upd.update();
            }
        }

        private void saveButtonCallback(float x, float y, int button) {
            if (button == 0) {
                savedPreset = theme.savePreset();
                fwl().saveCurrentThemeConfig();
            }
        }

        private void cancelChanges() {
            theme.applyPreset(savedPreset);
        }
    }
}
