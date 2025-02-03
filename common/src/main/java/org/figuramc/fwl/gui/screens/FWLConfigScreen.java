package org.figuramc.fwl.gui.screens;

import com.google.gson.JsonObject;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.figuramc.fwl.FWL;
import org.figuramc.fwl.gui.themes.FWLTheme;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.gui.widgets.Resizeable;
import org.figuramc.fwl.gui.widgets.Update;
import org.figuramc.fwl.gui.widgets.button.TextButton;
import org.figuramc.fwl.gui.widgets.containers.AbstractFWLContainerWidget;
import org.figuramc.fwl.gui.widgets.tabs.SideViewSwitcher;
import org.figuramc.fwl.gui.widgets.tabs.pages.PageEntry;
import org.figuramc.fwl.utils.Rectangle;
import org.jetbrains.annotations.Nullable;

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
                .addEntry(new ThemeSettingsPageEntry());
        addWidget(configSwitcher);
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

            this.theme = FWL.fwl().currentTheme();

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
                FWL.fwl().saveCurrentThemeConfig();
            }
        }

        private void cancelChanges() {
            theme.applyPreset(savedPreset);
        }
    }
}
