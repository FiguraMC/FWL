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
import org.figuramc.fwl.gui.widgets.button.TextButton;
import org.figuramc.fwl.gui.widgets.containers.AbstractFWLContainerWidget;
import org.figuramc.fwl.gui.widgets.misc.ContextMenu;
import org.figuramc.fwl.gui.widgets.misc.Label;
import org.figuramc.fwl.gui.widgets.tabs.SideViewSwitcher;
import org.figuramc.fwl.gui.widgets.tabs.pages.PageEntry;
import org.figuramc.fwl.text.FomponentRenderer;
import org.figuramc.fwl.text.fomponents.BaseFomponent;
import org.figuramc.fwl.text.fomponents.LiteralFomponent;
import org.figuramc.fwl.utils.Rectangle;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.List;

import static org.figuramc.fwl.FWL.fwl;
import static org.figuramc.fwl.utils.TextUtils.themeToTranslationString;
import static org.figuramc.fwl.text.style.FomponentStyle.*;

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

    private static class TestPageEntry implements PageEntry {

        @Override
        public FWLWidget getPage(float width, float height) {
            return new TestPage(width, height);
        }

        @Override
        public Component getTitle() {
            return Component.literal("Test page");
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
            Style labelStyle = Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("fwl.config.settings.selected_theme.tooltip")));
            Label buttonLabel = new Label(x, y, Component.translatable("fwl.config.settings.selected_theme").withStyle(labelStyle));
            changeThemeButton =
                    new TextButton(buttonLabel.boundaries().right() + 10, y, 100, 20, Component.translatable(themeToTranslationString(config.selectedTheme())));
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

    private static class TestPage extends AbstractFWLContainerWidget implements Resizeable {
//        private final AbstractComponent component;
        private final BaseFomponent fomponent;
        private float width, height;

        public TestPage(float width, float height) {

            this.width = width;
            this.height = height;
//            LiteralComponent title = literal("Hi!\n").setStyle(style().withScale(2, 2).withShadowColor(0.0f, 0.0f, 0.0f, 1f));
//            LiteralComponent mainText = literal("\nThis is a text made for showcase of the features of FWL's custom components.\n")
//                    .setStyle(style().withScale(1, 1).withVerticalAlignment(0f));
//            title.append(mainText);
//
//            mainText.append(literal("Main purpose of custom components is extension of minecraft's text styling capabilities.\n"))
//                    .append(literal("Apart from default "))
//                    .append(literal("color", style().withColor(0.25f, 1.0f, 0.25f, 1))).append(literal(", "))
//                    .append(literal("bold", style().withBold(true))).append(literal(", "))
//                    .append(literal("italic", style().withItalic(true))).append(literal(", "))
//                    .append(literal("underline", style().withUnderlineColor(1, 1, 1, 1))).append(literal(", "))
//                    .append(literal("strikethrough", style().withStrikethroughColor(1, 1, 1, 1))).append(literal(", "))
//                    .append(literal("and, of course")).append(literal(", "))
//                    .append(literal("obfuscated", style().withObfuscated(true))).append(literal(",\n"))
//                    .append(literal("there's also "))
//                    .append(literal("scale", style().withScale(1.5f, 1.5f))).append(literal(", "))
//                    .append(literal("offset", style().withOffset(0f, 4f))).append(literal(", "))
//                    .append(literal("skew", style().withSkew(1f, 1f))).append(literal(", "))
//                    .append(literal("background", style().withBackgroundColor(1f, 1f, 0.5f, 0.5f))).append(literal(", "))
//                    .append(literal("and also some little things like ability to set the color of\n"))
//                    .append(literal("shadow", style().withShadowColor(0.5f, 0, 0, 1))).append(literal(", "))
//                    .append(literal("underline", style().withUnderlineColor(1f, 0.5f, 0.5f, 1))).append(literal(", and "))
//                    .append(literal("strikethrough", style().withStrikethroughColor(1f, 0.5f, 0.5f, 1))).append(literal(".\n"))
//                    .append(literal("Oh and also there's text outline :3.", style().withOutlineColor(1f, 1f, 0.5f, 0.15f)))
//            ;

            LiteralFomponent mainText = new LiteralFomponent(
                    "This is a text made for showcase of the features of FWL's custom components.\n",
                    empty().setScale(VEC2_ONE).setVerticalAlignment(ZERO),
                    List.of(
                            new LiteralFomponent("Main purpose of custom components is extension of minecraft's text styling capabilities.\n", empty(), List.of()),
                            new LiteralFomponent("Apart from default ", empty(), List.of()),
                            new LiteralFomponent("color", empty().setColor(constant(0.25f, 1.0f, 0.25f, 1.0f)), List.of()), new LiteralFomponent(", ", empty(), List.of()),
                            new LiteralFomponent("bold", empty().setBold(TRUE), List.of()), new LiteralFomponent(", ", empty(), List.of()),
                            new LiteralFomponent("italic", empty().setItalic(TRUE), List.of()), new LiteralFomponent(", ", empty(), List.of()),
                            new LiteralFomponent("underline", empty().setUnderlineColor(VEC4_ONE), List.of()), new LiteralFomponent(", ", empty(), List.of()),
                            new LiteralFomponent("strikethrough", empty().setStrikethroughColor(VEC4_ONE), List.of()), new LiteralFomponent(", ", empty(), List.of()),
                            new LiteralFomponent("and, of course, ", empty(), List.of()),
                            new LiteralFomponent("obfuscated", empty().setObfuscated(TRUE), List.of()), new LiteralFomponent(",\n", empty(), List.of()),
                            new LiteralFomponent("there's also ", empty(), List.of()),
                            new LiteralFomponent("scale", empty().setScale(constant(1.5f, 1.5f)), List.of()), new LiteralFomponent(", ", empty(), List.of()),
                            new LiteralFomponent("offset", empty().setOffset(constant(0.0f, 4.0f)), List.of()), new LiteralFomponent(", ", empty(), List.of()),
                            new LiteralFomponent("skew", empty().setSkew(constant(1.0f, 1.0f)), List.of()), new LiteralFomponent(", ", empty(), List.of()),
                            new LiteralFomponent("background", empty().setBackgroundColor(constant(1.0f, 1.0f, 0.5f, 0.5f)), List.of()), new LiteralFomponent(", ", empty(), List.of()),
                            new LiteralFomponent("and also some little things like ability to set the color of\n", empty(), List.of()),
                            new LiteralFomponent("shadow", empty().setShadowColor(constant(0.5f, 0, 0, 1)), List.of()), new LiteralFomponent(", ", empty(), List.of()),
                            new LiteralFomponent("underline", empty().setUnderlineColor(constant(1f, 0.5f, 0.5f, 1f)), List.of()), new LiteralFomponent(", and ", empty(), List.of()),
                            new LiteralFomponent("strikethrough", empty().setStrikethroughColor(constant(1f, 0.5f, 0.5f, 1f)), List.of()), new LiteralFomponent(".\n", empty(), List.of()),
                            new LiteralFomponent("Oh and there's also text outline :3", empty().setOutlineColor(constant(1f, 1f, 0.5f, 0.15f)), List.of()), new LiteralFomponent("\n", empty(), List.of()),

                            new LiteralFomponent("Bonus: A fun gradient!\n", empty().setColor(gradientVec4(constant(new Vector4f(1, 0, 0, 1)), constant(new Vector4f(0, 1, 0, 1)))), List.of()),
                            new LiteralFomponent("Bonus: Offset can be a gradient too :3!\n", empty().setOffset(gradientVec2(VEC2_ZERO, constant(new Vector2f(0, 5)))), List.of()),
                            new LiteralFomponent("Woa... A gradient of a gradient of a gradient???\n",
                                    empty()
                                        .setOffset(gradientVec2(VEC2_ZERO, gradientVec2(VEC2_ZERO, gradientVec2(VEC2_ZERO, constant(new Vector2f(0, 20))))))
                                        .setColor(gradientVec4(constant(1, 0, 1, 1), constant(0, 1, 1, 1))),
                                    List.of()
                            )
                    )
            );

            LiteralFomponent title = new LiteralFomponent(
                    "Hi!\n",
                    empty().setScale(constant(new Vector2f(2))).setShadowColor(constant(new Vector4f(0,0,0,1))),
                    List.of(mainText)
            );

            fomponent = title;
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

        @Override
        public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
            Font font = Minecraft.getInstance().font;
            Matrix4f matrix = graphics.pose().last().pose();
//            TextRenderer renderer = new TextRenderer(graphics,10, 10, font, matrix, Font.DisplayMode.NORMAL, 15728880);
//            RenderSystem.enableBlend();
//            component.visit(renderer::accept);
//            renderer.render();
//            RenderSystem.disableBlend();

            FomponentRenderer renderer = new FomponentRenderer(graphics, 10, 10, font, matrix, Font.DisplayMode.NORMAL, 15728880);
            RenderSystem.enableBlend();
            fomponent.visit(renderer);
            renderer.renderLine();
            RenderSystem.disableBlend();
        }
    }
}
