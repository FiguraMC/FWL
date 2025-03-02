package org.figuramc.fwl.gui.screens;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
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
import org.figuramc.fwl.gui.widgets.button.RadioButton;
import org.figuramc.fwl.gui.widgets.button.TextButton;
import org.figuramc.fwl.gui.widgets.button.radio_button.RadioButtonGroupHandler;
import org.figuramc.fwl.gui.widgets.containers.AbstractFWLContainerWidget;
import org.figuramc.fwl.gui.widgets.descriptors.Orientation;
import org.figuramc.fwl.gui.widgets.input.TextInput;
import org.figuramc.fwl.gui.widgets.input.handlers.IntegerInputHandler;
import org.figuramc.fwl.gui.widgets.misc.ContextMenu;
import org.figuramc.fwl.gui.widgets.misc.Label;
import org.figuramc.fwl.gui.widgets.scrollable.ScrollBar;
import org.figuramc.fwl.gui.widgets.scrollable.ScrollableArea;
import org.figuramc.fwl.gui.widgets.tabs.SideViewSwitcher;
import org.figuramc.fwl.gui.widgets.tabs.pages.PageEntry;
import org.figuramc.fwl.gui.widgets.tabs.pages.SimplePage;
import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.TextRenderer;
import org.figuramc.fwl.text.components.AbstractComponent;
import org.figuramc.fwl.text.components.LiteralComponent;
import org.figuramc.fwl.text.components.TranslatableComponent;
import org.figuramc.fwl.text.serialization.FWLSerializer;
import org.figuramc.fwl.utils.Rectangle;
import org.figuramc.fwl.utils.RenderUtils;
import org.figuramc.fwl.utils.Watch;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;

import static org.figuramc.fwl.FWL.fwl;
import static org.figuramc.fwl.text.FWLStyle.*;
import static org.figuramc.fwl.text.components.LiteralComponent.literal;
import static org.figuramc.fwl.text.components.TranslatableComponent.translation;
import static org.figuramc.fwl.text.effects.ConstantApplier.constant;
import static org.figuramc.fwl.text.effects.GradientApplier.*;
import static org.figuramc.fwl.text.effects.ShakeApplier.shake2;
import static org.figuramc.fwl.text.effects.WithValue.withValue;
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
                .addEntry(new ThemeSettingsPageEntry())
                .addEntry(new TestPageEntry());
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
        public AbstractComponent getTitle() {
            return translation("fwl.config.settings");
        }

        @Override
        public @Nullable AbstractComponent getTooltip() {
            return translation("fwl.config.settings.tooltip");
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
        public AbstractComponent getTitle() {
            return translation("fwl.config.theme_settings");
        }

        @Override
        public @Nullable AbstractComponent getTooltip() {
            return translation("fwl.config.theme_settings.tooltip");
        }

        @Override
        public void onClose() {
            page.cancelChanges();
        }
    }

    private static class TestPageEntry implements PageEntry {

        @Override
        public FWLWidget getPage(float width, float height) {
            return new TestPage(width, height);
        }

        @Override
        public AbstractComponent getTitle() {
            return literal("Test page");
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
        }

        private float createThemeDropdown(float x, float y) {
            FWLConfig config = fwl().config();
            FWLStyle labelStyle = FWLStyle.EMPTY.withTooltip(translation("fwl.config.settings.selected_theme.tooltip"));
            Label buttonLabel = new Label(x, y, translation("fwl.config.settings.selected_theme").setStyle(labelStyle));
            changeThemeButton =
                    new TextButton(buttonLabel.boundaries().right() + 10, y, 100, 20, translation(themeToTranslationString(config.selectedTheme())));
            changeThemeButton.setCallback(this::changeThemeCallback);
            addWidget(buttonLabel);
            addWidget(changeThemeButton);
            return 20;
        }

        private void changeThemeCallback(float x, float y, int button) {
            if (button == 0) {
                Rectangle boundaries = changeThemeButton.boundaries();
                ContextMenu menu = new ContextMenu(this, boundaries.left(), boundaries.bottom());
                FWLThemeRepository themes = fwl().themeRepository();
                themes.getRegisteredThemes().forEachRemaining(p -> {
                    menu.addEntry(new ThemeSwitchEntry(p.a(), changeThemeButton));
                });
                menu.setInteractionPriority(1000).setRenderPriority(1000);
                setFocused(menu);
                addWidget(menu);
            }
        }

        private static class ThemeSwitchEntry implements ContextMenu.Entry {
            private final ResourceLocation theme;
            private final TextButton parentButton;
            private final AbstractComponent text;

            private ThemeSwitchEntry(ResourceLocation theme, TextButton parentButton) {
                this.theme = theme;
                this.parentButton = parentButton;
                text = translation(themeToTranslationString(theme));
            }

            @Override
            public AbstractComponent text() {
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
        private static final TranslatableComponent RESET_BUTTON_COMPONENT = translation("fwl.config.theme_settings.reset");
        private static final TranslatableComponent RESET_BUTTON_COMPONENT_TOOLTIP = translation("fwl.config.theme_settings.reset.tooltip");
        private static final TranslatableComponent CANCEL_BUTTON_COMPONENT = translation("fwl.config.theme_settings.cancel");
        private static final TranslatableComponent CANCEL_BUTTON_COMPONENT_TOOLTIP = translation("fwl.config.theme_settings.cancel.tooltip");
        private static final TranslatableComponent SAVE_BUTTON_COMPONENT = translation("fwl.config.theme_settings.save");
        private static final TranslatableComponent SAVE_BUTTON_COMPONENT_TOOLTIP = translation("fwl.config.theme_settings.save.tooltip");
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

    private static class TestPage extends AbstractFWLContainerWidget implements Resizeable {
        private float width, height;

        private final Watch watch = new Watch();

        private TextButton button1, button2, contextTestButton;
        private Checkbox checkbox1, checkbox2, checkbox3, checkbox4;
        private RadioButton radio1, radio2, radio3, radio4;
        private ScrollBar scrollBar1, scrollBar2;
        private ScrollableArea area1;
        private TextInput input1;
        private IntegerInputHandler inputHandler1;
        private SideViewSwitcher sideViewSwitcher;

        public TestPage(float width, float height) {
            this.width = width;
            this.height = height;

            addWidget(button1 = new TextButton(10, 10, 125, 20, literal("Button")));
            addWidget(button2 = new TextButton(10, 35, 125, 20, literal("Disabled button")));

            addWidget(checkbox1 = new Checkbox(145, 15, 10, 10, false));
            addWidget(checkbox2 = new Checkbox(165, 15, 10, 10, true));
            addWidget(checkbox3 = new Checkbox(145, 40, 10, 10, false));
            addWidget(checkbox4 = new Checkbox(165, 40, 10, 10, true));

            addWidget(radio1 = new RadioButton(185, 15, 10, 10, false));
            addWidget(radio2 = new RadioButton(205, 15, 10, 10, true));
            addWidget(radio3 = new RadioButton(185, 40, 10, 10, false));
            addWidget(radio4 = new RadioButton(205, 40, 10, 10, true));

            addWidget(contextTestButton = new TextButton(225, 10, 125, 20, literal("Entry 1")).setCallback(
                    (x, y, b) -> {
                        Rectangle bounds = contextTestButton.boundaries();
                        ContextMenu menu = new ContextMenu(TestPage.this, bounds.left(), bounds.bottom())
                                .addEntry(new ContextMenu.StandardEntry(literal("Entry 1"), (m) -> {
                                    contextTestButton.setMessage(literal("Entry 1"));
                                    return false;
                                }))
                                .addEntry(new ContextMenu.StandardEntry(literal("Entry 2"), (m) -> {
                                    contextTestButton.setMessage(literal("Entry 2"));
                                    return false;
                                }))
                                .addEntry(new ContextMenu.StandardEntry(literal("Entry 3"), (m) -> {
                                    contextTestButton.setMessage(literal("Entry 3"));
                                    return false;
                                }))
                                .setInteractionPriority(100);
                        addWidget(menu);
                        setFocused(menu);
                    }
            ));
            addWidget(
                    input1 = new TextInput(225, 40, 125, 20, "128")
                            .setChangeCallback(inputHandler1 = new IntegerInputHandler().setValueConsumer(System.out::println))
                            .setTextBaker(inputHandler1)
            );

            addWidget(area1 = new ScrollableArea(10, 80, 100, 100));
            addWidget(scrollBar1 = new ScrollBar(110, 80, 10, 100, 100, 200, 0, Orientation.VERTICAL).setScrollStep(10));
            addWidget(scrollBar2 = new ScrollBar(10, 180, 100, 10, 100, 200, 0, Orientation.HORIZONTAL).setScrollStep(10));
            scrollBar1.setCallback(area1::setOffsetY);
            scrollBar2.setCallback(area1::setOffsetX);

            for (int i = 0; i < 20; i++) {
                float bX = 100 * (i % 2);
                float bY = 20 * (float)(i / 2);
                TextButton button = new TextButton(area1.x() + bX, area1.y() + bY, 100, 20, literal("Button %s".formatted(i)));
                area1.addWidget(button);
            }

            addWidget(sideViewSwitcher = new SideViewSwitcher(145, 70, 200, 100)
                    .addEntry(new SimplePage(literal("Page 1"), new TextButton(0, 0, 100, 20, literal("Button"))))
                    .addEntry(new SimplePage(literal("Page 2"), new Checkbox(0, 0, 10, 10, true)))
                    .addEntry(new SimplePage(literal("Page 3"), new TextInput(0, 0, 150, 20)))
                    .addEntry(new SimplePage(literal("Page 4"), null))
                    .setCollapsed(true)
            );

            button2.setEnabled(false);
            checkbox3.setEnabled(false);
            checkbox4.setEnabled(false);
            radio3.setEnabled(false);
            radio4.setEnabled(false);

            RadioButtonGroupHandler.createHandlersAndApply((i) -> System.out.printf("Current active radio button: %s%n", i), radio1, radio2);
        }

        @Override
        public Rectangle boundaries() {
            return new Rectangle(0, 0, width, height);
        }

        @Override
        public void resize(float width, float height) {
            this.width = width;
            this.height = height;
        }
    }
}
