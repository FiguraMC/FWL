package org.figuramc.fwl.gui.themes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.figuramc.fwl.gui.themes.configs.FWLAbstractConfig;
import org.figuramc.fwl.gui.themes.configs.FWLBreezeConfig;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.gui.widgets.containers.SimpleContainer;
import org.figuramc.fwl.gui.widgets.descriptors.*;
import org.figuramc.fwl.gui.widgets.descriptors.button.ButtonDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.button.CheckboxDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.ClickableDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.button.RadioButtonDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.input.TextInputDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.input.SliderDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.misc.ContextMenuDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.misc.IconDescriptor;
import org.figuramc.fwl.gui.widgets.input.Slider;
import org.figuramc.fwl.gui.widgets.input.TextInput;
import org.figuramc.fwl.gui.widgets.input.handlers.IntegerInputHandler;
import org.figuramc.fwl.gui.widgets.misc.ContextMenu;
import org.figuramc.fwl.gui.widgets.misc.Label;
import org.figuramc.fwl.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static net.minecraft.network.chat.Component.translatable;
import static net.minecraft.util.FastColor.ARGB32;
import static org.figuramc.fwl.utils.ColorUtils.argb;
import static org.figuramc.fwl.utils.JsonUtils.intOrDefault;
import static org.figuramc.fwl.gui.widgets.descriptors.BoundsDescriptor.Side;
import static org.figuramc.fwl.utils.MathUtils.lerp;
import static org.figuramc.fwl.utils.MathUtils.rlerp;
import static org.figuramc.fwl.utils.RenderUtils.*;
import static org.figuramc.fwl.utils.Pair.of;


public class FWLBreeze extends FWLTheme {

    private TreePathMap<Integer> colors;

    private int textColor;
    private int textHintColor;
    private int disabledTextColor;

    private int primaryColor;
    private int accentColor;
    private int successColor;

    private int borderColor;

    private int borderRadius;

    private final Pair<Component, FWLAbstractConfig.FieldConstructor<FWLBreeze>>[] CONSTRUCTORS = new Pair[] {
            of(translatable("fwl.breeze.color.primary"), colorPickerField(b -> b.primaryColor, (b, v) -> b.primaryColor = v, false)),
            of(translatable("fwl.breeze.color.accent"), colorPickerField(b -> b.accentColor, (b, v) -> b.accentColor = v, false)),
            of(translatable("fwl.breeze.color.success"), colorPickerField(b -> b.successColor, (b, v) -> b.successColor = v, false)),

            of(translatable("fwl.breeze.color.border"), colorPickerField(b -> b.borderColor, (b, v) -> b.borderColor = v, false)),

            of(translatable("fwl.breeze.color.text"), colorPickerField(b -> b.textColor, (b, v) -> b.textColor = v, false)),
            of(translatable("fwl.breeze.color.text.hint"), colorPickerField(b -> b.textHintColor, (b, v) -> b.textHintColor = v, false)),
            of(translatable("fwl.breeze.color.text.disabled"), colorPickerField(b -> b.disabledTextColor, (b, v) -> b.disabledTextColor = v, false)),

            of(translatable("fwl.breeze.border.radius"), sliderField(b -> (float) b.borderRadius, (b, v) -> b.borderRadius = (int) (float) v, 0, 10, 10))
    };

    public FWLBreeze() {
        this(null);
    }

    public FWLBreeze(@Nullable JsonObject preset) {
        applyPreset(preset);
    }


    @Override
    public void renderButton(GuiGraphics graphics, float delta, ButtonDescriptor button) {
        float x = button.x(), y = button.y(), width = button.width(), height = button.height();
        // Border radius
        float rad = Math.min(borderRadius * 1.5f, Math.min(button.width(), button.height()) / 2);

        // Getting button color by its type, and then getting gradient for it. Breeze theme doesn't check for namespace
        int buttonColor = applyStateModifier(getColorOrDefault(button.type(), 0xFFFFFFFF), button);

        int borderColor = getColorOrDefault(button.focused() ? ColorTypes.SECONDARY : ColorTypes.BORDER, 0xFF000000); // Getting border color. Breeze theme doesn't check for namespace

        float x1 = x+width;
        float y1 = y+height;

        float sc = getWindowScaling();
        float thickness = 3;

        // Button background
        renderRoundBg(graphics, buttonColor, x, y, x1, y1, rad, sc);

        // Button border
        renderRoundBorder(graphics, borderColor, x, y, x1, y1, rad, sc, thickness);
    }

    @Override
    public void renderCheckbox(GuiGraphics graphics, float delta, CheckboxDescriptor checkbox) {
        float x = checkbox.x(), y = checkbox.y(), width = checkbox.width(), height = checkbox.height();
        // Border radius
        float rad = Math.min(borderRadius * 1.5f, Math.min(checkbox.width(), checkbox.height()) / 2);

        // Getting button color by its type, and then getting gradient for it. Breeze theme doesn't check for namespace
        int buttonColor = applyStateModifier(getColorOrDefault(checkbox.checked() ? ColorTypes.SECONDARY : ColorTypes.BUTTON, 0xFFFFFFFF), checkbox);

        int borderColor = getColorOrDefault(checkbox.focused() ? ColorTypes.SECONDARY : ColorTypes.BORDER, 0xFF000000); // Getting border color. Breeze theme doesn't check for namespace

        float x1 = x+width;
        float y1 = y+height;

        float sc = getWindowScaling();
        float thickness = 3;

        // Button background
        renderRoundBg(graphics, buttonColor, x, y, x1, y1, rad, sc);

        // Button border
        renderRoundBorder(graphics, borderColor, x, y, x1, y1, rad, sc, thickness);
    }

    @Override
    public void renderRadioButton(GuiGraphics graphics, float delta, RadioButtonDescriptor radioButton) {
        float x = radioButton.x(), y = radioButton.y(), width = radioButton.width(), height = radioButton.height();
        // Border radius
        float rad = Math.min(radioButton.width(), radioButton.height()) / 2;


        // Getting button color by its type, and then getting gradient for it. Breeze theme doesn't check for namespace
        int buttonColor = applyStateModifier(getColorOrDefault(radioButton.active() ? ColorTypes.SECONDARY : ColorTypes.BUTTON, 0xFFFFFFFF), radioButton);

        int borderColor = getColorOrDefault(radioButton.focused() ? ColorTypes.SECONDARY : ColorTypes.BORDER, 0xFF000000); // Getting border color. Breeze theme doesn't check for namespace

        float x1 = x+width;
        float y1 = y+height;

        float sc = getWindowScaling();
        float thickness = 3;

        // Button background
        renderRoundBg(graphics, buttonColor, x, y, x1, y1, rad, sc);
        if (radioButton.active()) {
            float cX = x + width / 4;
            float cY = y + height / 4;
            float cWidth = width / 2;
            float cHeight = height / 2;
            float cX1 = cX + cWidth;
            float cY1 = cY + cHeight;
            float rad2 = Math.min(cWidth, cHeight) / 2;
            renderRoundBg(graphics, 0xFFFFFFFF, cX, cY, cX1, cY1, rad2, sc);
        }

        // Button border
        renderRoundBorder(graphics, borderColor, x, y, x1, y1, rad, sc, thickness);
    }

    @Override
    public void renderScrollBar(GuiGraphics graphics, float delta, ScrollBarDescriptor scrollBar) {
        float x = scrollBar.x(), y = scrollBar.y(), width = scrollBar.width(), height = scrollBar.height();
        float inX = x + 2, inY = y + 2, inWidth = width - 4, inHeight = height - 4;
        float rad = Math.min(borderRadius * 1.5f, Math.min(width, height) / 2);
        float barRad = Math.min(inWidth, inHeight) / 2;
        float barX, barY, barWidth, barHeight;

        if (scrollBar.orientation() == Orientation.VERTICAL) {
            barX = inX;
            barWidth = inWidth;
            barHeight = Math.max(barWidth, inHeight * scrollBar.coveredPartSize());
            float maxY = inHeight - barHeight;
            barY = inY + (maxY * scrollBar.progress());
        }
        else {
            barY = inY;
            barHeight = inHeight;
            barWidth = Math.max(barHeight, inWidth * scrollBar.coveredPartSize());
            float maxX = inWidth - barWidth;
            barX = inX + (maxX * scrollBar.progress());
        }

        float x1 = x + width, y1 = y + height;
        float barX1 = barX + barWidth, barY1 = barY + barHeight;

        float sc = getWindowScaling();
        float thickness = 3;

        int bgColor = getColorOrDefault(ColorTypes.PRIMARY, 0xFFFFFFFF);
        int barColor = applyStateModifier(getColorOrDefault(ColorTypes.SECONDARY, 0xFFFFFFFF), scrollBar);
        int borderColor = getColorOrDefault(scrollBar.focused() ? ColorTypes.SECONDARY : ColorTypes.BORDER, 0xFF000000); // Getting border color. Breeze theme doesn't check for namespace

        renderRoundBg(graphics, bgColor, x, y, x1, y1, rad, sc);
        RenderUtils.renderRoundBg(graphics, new StaticColor(barColor), barX, barY, barX1, barY1, barRad, sc);
        renderRoundBorder(graphics, borderColor, x, y, x1, y1, rad, sc, thickness);
    }

    @Override
    public void renderTextInput(GuiGraphics graphics, float delta, TextInputDescriptor input) {
        float x = input.x(), y = input.y(), width = input.width(), height = input.height();
        float rad = Math.min(borderRadius / 2f, Math.min(width, height) / 2);

        float x1 = x + width;
        float y1 = y + height;

        float sc = getWindowScaling();

        int bgColor = getColorOrDefault(ColorTypes.PRIMARY, 0xAAAAAAAA);
        int borderColor = getColorOrDefault(input.focused() ? ColorTypes.SECONDARY : ColorTypes.BORDER, 0xFF000000); // Getting border color. Breeze theme doesn't check for namespace

        float thickness = 2;

        renderRoundBg(graphics, bgColor, x, y, x1, y1, rad, sc);
        renderRoundBorder(graphics, borderColor, x, y, x1, y1, rad, sc, thickness);
    }

    @Override
    public void renderContextMenu(GuiGraphics graphics, float delta, ContextMenuDescriptor menu) {
        float x = menu.x(), y = menu.y(), width = menu.width(), height = menu.height();
        float rad = Math.min(borderRadius / 2f, Math.min(width, height) / 2);

        float x1 = x + width;
        float y1 = y + height;

        float sc = getWindowScaling();

        int bgColor = getColorOrDefault(ColorTypes.PRIMARY, 0xAAAAAAAA);
        int borderColor = getColorOrDefault(ColorTypes.BORDER, 0xFF000000); // Getting border color. Breeze theme doesn't check for namespace

        float thickness = 2;

        renderRoundBg(graphics, bgColor, x, y, x1, y1, rad, sc);
        renderRoundBorder(graphics, borderColor, x, y, x1, y1, rad, sc, thickness);
        for (int i = 0; i < menu.entriesCount() - 1; i++) {
            float entryY = y + ((i + 1) * ContextMenu.entryHeight()) + 1;
            fill(graphics, x, entryY, x1, entryY + 1f / sc, 0, borderColor);
        }
    }

    @Override
    public void renderBounds(GuiGraphics graphics, float delta, BoundsDescriptor bounds) {
        float x0 = bounds.x(), y0 = bounds.y(), width = bounds.width(), height = bounds.height();
        float rad = Math.min(borderRadius, Math.min(width, height) / 2);

        float x1 = x0 + width;
        float y1 = y0 + height;

        float scaling = getWindowScaling();
        float thickness = 3;
        int color = getColorOrDefault(bounds.focused() ? ColorTypes.SECONDARY : ColorTypes.BORDER, 0xFF000000); // Getting border color. Breeze theme doesn't check for namespace
        float lh = thickness / scaling; // Line height

        boolean topActive = bounds.isPresent(Side.TOP);
        boolean bottomActive = bounds.isPresent(Side.BOTTOM);
        boolean leftActive = bounds.isPresent(Side.LEFT);
        boolean rightActive = bounds.isPresent(Side.RIGHT);

        boolean topInner = bounds.isInner(Side.TOP);
        boolean bottomInner = bounds.isInner(Side.BOTTOM);
        boolean leftInner = bounds.isInner(Side.LEFT);
        boolean rightInner = bounds.isInner(Side.RIGHT);

        boolean topRound = topActive && !topInner;
        boolean bottomRound = bottomActive && !bottomInner;
        boolean leftRound = leftActive && !leftInner;
        boolean rightRound = rightActive && !rightInner;

        float innerX0 = !leftRound ? x0 : x0 + rad;
        float innerX1 = !rightRound ? x1 : x1 - rad;
        float innerY0 = !topRound ? y0 : y0 + rad;
        float innerY1 = !bottomRound ? y1 : y1 - rad;

        boolean topLeft = topRound && leftRound;
        boolean topRight = topRound && rightRound;
        boolean bottomLeft = bottomRound && leftRound;
        boolean bottomRight = bottomRound && rightRound;

        if (topActive) fill(graphics, topLeft ? innerX0 : x0, y0, topRight ? innerX1 : x1, y0 + lh, 0, color); // Top-middle
        if (bottomActive) fill(graphics, bottomLeft ? innerX0 : x0, y1 - lh, bottomRight ? innerX1 : x1, y1, 0, color); // Bottom-middle
        if (leftActive) fill(graphics, x0, topLeft ? innerY0 : y0, x0 + lh, bottomLeft ? innerY1 : y1, 0, color); // Left-center
        if (rightActive) fill(graphics, x1 - lh, topRight ? innerY0 : y0, x1, bottomRight ? innerY1 : y1, 0, color); // Right-center
        if (topLeft) renderArc(graphics, innerX0, y0, 0, rad, scaling, thickness, ArcOrient.NEGATIVE, ArcOrient.POSITIVE, color); // Top-left corner
        if (topRight) renderArc(graphics, innerX1, y0, 0, rad, scaling, thickness, ArcOrient.POSITIVE, ArcOrient.POSITIVE, color); // Top-right corner
        if (bottomLeft) renderArc(graphics, innerX0, y1, 0, rad, scaling, thickness, ArcOrient.NEGATIVE, ArcOrient.NEGATIVE, color); // Bottom-left corner
        if (bottomRight) renderArc(graphics, innerX1, y1, 0, rad, scaling, thickness, ArcOrient.POSITIVE, ArcOrient.NEGATIVE, color); // Bottom-right corner
    }

    @Override
    public void fillBounds(GuiGraphics graphics, float delta, BoundsDescriptor bounds) {
        Rectangle wBounds = bounds.widgetBounds();

        float x0 = bounds.x(), y0 = bounds.y(), width = bounds.width(), height = bounds.height();
        float rad = Math.min(borderRadius, Math.min(width, height) / 2);

        float x1 = x0 + width;
        float y1 = y0 + height;

        float scaling = getWindowScaling();
        int primary = getColorOrDefault(ColorTypes.PRIMARY, 0xFF777777);
        int bgColor = applyStateModifier(getColorOrDefault(primary, bounds.widgetType()), bounds); // Getting border color. Breeze theme doesn't check for namespace
        BreezeGradient color = getBreezeColorGradient(bgColor, wBounds.left(), wBounds.top(), wBounds.right(), wBounds.bottom());

        boolean topActive = bounds.isPresent(Side.TOP);
        boolean bottomActive = bounds.isPresent(Side.BOTTOM);
        boolean leftActive = bounds.isPresent(Side.LEFT);
        boolean rightActive = bounds.isPresent(Side.RIGHT);

        boolean topInner = bounds.isInner(Side.TOP);
        boolean bottomInner = bounds.isInner(Side.BOTTOM);
        boolean leftInner = bounds.isInner(Side.LEFT);
        boolean rightInner = bounds.isInner(Side.RIGHT);

        boolean topRound = topActive && !topInner;
        boolean bottomRound = bottomActive && !bottomInner;
        boolean leftRound = leftActive && !leftInner;
        boolean rightRound = rightActive && !rightInner;

        float innerX0 = !leftRound ? x0 : x0 + rad;
        float innerX1 = !rightRound ? x1 : x1 - rad;
        float innerY0 = !topRound ? y0 : y0 + rad;
        float innerY1 = !bottomRound ? y1 : y1 - rad;

        boolean topLeft = topRound && leftRound;
        boolean topRight = topRound && rightRound;
        boolean bottomLeft = bottomRound && leftRound;
        boolean bottomRight = bottomRound && rightRound;

        if (topLeft) renderArcFilled(graphics, innerX0, y0, 0, rad, scaling, ArcOrient.NEGATIVE, ArcOrient.POSITIVE, color); // Top-left corner
        if (topRight) renderArcFilled(graphics, innerX1, y0, 0, rad, scaling, ArcOrient.POSITIVE, ArcOrient.POSITIVE, color); // Top-right corner
        if (bottomLeft) renderArcFilled(graphics, innerX0, y1, 0, rad, scaling, ArcOrient.NEGATIVE, ArcOrient.NEGATIVE, color); // Bottom-left corner
        if (bottomRight) renderArcFilled(graphics, innerX1, y1, 0, rad, scaling, ArcOrient.POSITIVE, ArcOrient.NEGATIVE, color); // bottom-right corner
        if (topRound) fill(graphics, innerX0, y0, innerX1, innerY0, 0, color); // Top line
        if (bottomRound) fill(graphics, innerX0, innerY1, innerX1, y1, 0, color); // Bottom line
        if (leftRound) fill(graphics, x0, innerY0, innerX0, innerY1, 0, color); // Left line
        if (rightRound) fill(graphics, innerX1, innerY0, x1, innerY1, 0, color); // Left line
        fill(graphics, innerX0, innerY0, innerX1, innerY1, 0, color); // Middle
    }

    @Override
    public void renderSlider(GuiGraphics graphics, float delta, SliderDescriptor slider) {
        final float STEP_BAR_LENGTH_EXTENSION = 2;
        float x = slider.x(), y = slider.y(), width = slider.width(), height = slider.height();
        float x2 = x + width, y2 = y + height;
        float barThickness = 2;
        float sliderX, sliderY;
        float sliderSize = Math.min(width, height);
        float sliderRad = sliderSize / 2;

        Orientation orient = slider.orientation();

        float scaling = getWindowScaling();
        int sliderColor = applyStateModifier(getColorOrDefault(ColorTypes.SECONDARY, 0xFF777777), slider);
        int sliderBorderColor = getColorOrDefault(slider.focused() ? ColorTypes.SECONDARY : ColorTypes.BORDER, 0xFF000000); // Getting border color. Breeze theme doesn't check for namespace
        int barColor = getColorOrDefault(ColorTypes.BORDER, 0xFF111111);

        int steps = slider.steps();

        if (orient == Orientation.HORIZONTAL) {
            sliderX = lerp(x, x2 - sliderSize, slider.progress());
            sliderY = y;
            float bY1 = y + (height - barThickness) / 2;
            fill(graphics, x, bY1, x2, bY1 + barThickness, 0, barColor);

            if (steps > 1) for (int i = 0; i < steps; i++) {
                float progress = i / (float) (steps - 1);
                float stepX = lerp(x + sliderRad, x2 - sliderRad, progress);
                fill(graphics, stepX - 1, y - STEP_BAR_LENGTH_EXTENSION, stepX + 1, y2 + STEP_BAR_LENGTH_EXTENSION, 0, barColor);
            }
        }
        else {
            sliderX = x;
            sliderY = lerp(y, y2 - sliderSize, slider.progress());
            float bX1 = x + (width - barThickness) / 2;
            fill(graphics, bX1, y, bX1 + barThickness, y2, 0, barColor);
            if (steps > 1) for (int i = 0; i < steps; i++) {
                float progress = i / (float) (steps - 1);
                float stepY = lerp(y + sliderRad, y2 - sliderRad, progress);
                fill(graphics, x - STEP_BAR_LENGTH_EXTENSION, stepY - 1, x2 + STEP_BAR_LENGTH_EXTENSION, stepY + 1, 0, barColor);
            }
        }

        renderRoundBg(graphics, sliderColor, sliderX, sliderY, sliderX + sliderSize, sliderY + sliderSize, sliderRad, scaling);
        renderRoundBorder(graphics, sliderBorderColor, sliderX, sliderY, sliderX + sliderSize, sliderY + sliderSize, sliderRad, scaling, 1);
    }

    @Override
    public void renderIcon(GuiGraphics graphics, IconDescriptor icon) {

    }

    private void renderRoundBg(GuiGraphics graphics, int color, float x0, float y0, float x1, float y1, float rad, float scaling) {
        RenderUtils.renderRoundBg(graphics, getBreezeColorGradient(color, x0, y0, x1, y1), x0, y0, x1, y1, rad, scaling);
    }

    @Override
    public Integer getColor(String... path) {
        return colors.get(path);
    }

    public int applyStateModifier(int color, ClickableDescriptor state) {
        int r = ARGB32.red(color);
        int g = ARGB32.green(color);
        int b = ARGB32.blue(color);
        int a = ARGB32.alpha(color);
        if (state.disabled()) return argb(a, r - 30, g - 30, b - 30);
        if (state.clicked()) return argb(a, r - 20, g - 20, b - 20);
        if (state.hovered()) return argb(a, r - 10, g - 10, b - 10);
        return color;
    }

    public int applyStateModifier(int color, CheckboxDescriptor state) {
        int r = ARGB32.red(color);
        int g = ARGB32.green(color);
        int b = ARGB32.blue(color);
        int a = ARGB32.alpha(color);
        if (state.disabled()) return argb(a, r - 30, g - 30, b - 30);
        if (state.clicked()) return argb(a, r - 20, g - 20, b - 20);
        if (state.hovered()) return argb(a, r - 10, g - 10, b - 10);
        return color;
    }

    private BreezeGradient getBreezeColorGradient(int baseColor, float x0, float y0, float x1, float y1) {
        int r = ARGB32.red(baseColor);
        int g = ARGB32.green(baseColor);
        int b = ARGB32.blue(baseColor);
        int a = ARGB32.alpha(baseColor);

        int g3 = 12;

        int bR = r - g3, bG = g - g3, bB = b - g3;

        return new BreezeGradient(y0, y1,
            r / 255f, g / 255f, b / 255f, a / 255f,
                bR / 255f, bG / 255f, bB / 255f, a / 255f);
    }

    public void applyValues() {
        colors = new TreePathMap<>() {{
            add(ColorTypes.BUTTON, primaryColor);

            add(ColorTypes.PRIMARY, primaryColor);
            add(ColorTypes.SECONDARY, accentColor);
            add(ColorTypes.SUCCESS, successColor);

            add(ColorTypes.BORDER, borderColor);

            add(ColorTypes.TEXT, textColor);
            add(ColorTypes.TEXT_DISABLED, disabledTextColor);
            add(ColorTypes.TEXT_HINT, textHintColor);

            add(ColorTypes.BUTTON_TEXT, textColor);
            add(disabledTextColor, ColorTypes.BUTTON_TEXT.getPath(), "disabled");
        }};
    }

    @Override
    public void applyPreset(@Nullable JsonObject preset) {
        if (preset != null) {
            primaryColor = intOrDefault(preset, "color.primary", 0xFF3A3A3A);
            accentColor = intOrDefault(preset, "color.accent", 0xFF14539E);
            successColor = intOrDefault(preset, "color.success", 0xFF25AA61);

            borderColor = intOrDefault(preset, "color.border", 0xFF111111);

            textColor = intOrDefault(preset, "color.text", 0xFFFFFFFF);
            textHintColor = intOrDefault(preset, "color.text.hint", 0xFFFFFFFF);
            disabledTextColor = intOrDefault(preset, "color.text.disabled", 0xFF777777);

            borderRadius = intOrDefault(preset, "border.radius", 3);
        }
        else {
            primaryColor = 0xFF3A3A3A;
            accentColor = 0xFF3473BE;
            successColor = 0xFF25AA61;

            borderColor = 0xFF111111;

            textColor = 0xFFFFFFFF;
            textHintColor = 0xFF999999;
            disabledTextColor = 0xFF777777;

            borderRadius = 2;
        }
        applyValues();
    }

    @Override
    public @NotNull JsonObject savePreset() {
        JsonObject preset = new JsonObject();
        preset.addProperty("color.primary", primaryColor);
        preset.addProperty("color.accent", accentColor);
        preset.addProperty("color.success", successColor);

        preset.addProperty("color.border", borderColor);

        preset.addProperty("color.text", textColor);
        preset.addProperty("color.text.hint", textHintColor);
        preset.addProperty("color.text.disabled", disabledTextColor);

        preset.addProperty("border.radius", borderRadius);
        return preset;
    }

    @Override
    public FWLWidget settingsRootWidget(float areaWidth, float areaHeight) {
        return new FWLBreezeConfig(this, areaWidth, areaHeight, CONSTRUCTORS);
    }

    private static FWLAbstractConfig.FieldConstructor<FWLBreeze> intField(Function<FWLBreeze, Integer> provider, BiConsumer<FWLBreeze, Integer> consumer, int radix) {
        return intField(provider, consumer, radix, true);
    }

    private static FWLAbstractConfig.FieldConstructor<FWLBreeze> intField(Function<FWLBreeze, Integer> provider, BiConsumer<FWLBreeze, Integer> consumer, int radix, boolean unsigned) {
        return (parent, x, y) -> {
            int initialValue = provider.apply(parent);
            TextInput intInput = new TextInput(x, y, 100, 20, unsigned ? Integer.toUnsignedString(initialValue, radix) : Integer.toString(initialValue, radix));
            IntegerInputHandler handler = new IntegerInputHandler();
            handler.setValueConsumer(i -> {
                consumer.accept(parent, i);
                parent.applyValues();
            }).setRadix(radix).setUnsigned(unsigned);
            intInput.setChangeCallback(handler).setTextBaker(handler);
            return intInput;
        };
    }

    private static FWLAbstractConfig.FieldConstructor<FWLBreeze> sliderField(Function<FWLBreeze, Float> provider, BiConsumer<FWLBreeze, Float> consumer, float min, float max, int steps) {
        return (parent, x, y) -> {
            float initialValue = provider.apply(parent);
            Slider slider = new Slider(x, y, 100, 10, rlerp(min, max, initialValue)).setSteps(steps);
            slider.setCallback(v -> consumer.accept(parent, lerp(min, max, v)));
            return slider;
        };
    }

    private static final String[] COLOR_COMPONENT_NAMES = new String[] {
            "R", "G", "B", "A"
    };

    private static FWLAbstractConfig.FieldConstructor<FWLBreeze> colorPickerField(Function<FWLBreeze, Integer> provider, BiConsumer<FWLBreeze, Integer> consumer) {
        return colorPickerField(provider, consumer, true);
    }

    private static FWLAbstractConfig.FieldConstructor<FWLBreeze> colorPickerField(Function<FWLBreeze, Integer> provider, BiConsumer<FWLBreeze, Integer> consumer, boolean alpha) {
        return (parent, x, y) -> {
            AtomicInteger currentValue = new AtomicInteger(provider.apply(parent));
            int[] components = new int[] {ARGB32.red(currentValue.get()), ARGB32.green(currentValue.get()), ARGB32.blue(currentValue.get()), ARGB32.alpha(currentValue.get())};
            int availableComponents = alpha ? 4 : 3;
            float cY = y;

            SimpleContainer container = new SimpleContainer();

            for (int i = 0; i < availableComponents; i++) {
                Label cLabel = new Label(x, cY, Component.literal(COLOR_COMPONENT_NAMES[i]));
                Slider cSlider = new Slider(cLabel.boundaries().right() + 10, cY, 100, 10, components[i] / 255f).setUpdateOnMove(true);
                TextInput cInput = new TextInput(cSlider.boundaries().right() + 10, cY, 50, 10, Integer.toString(components[i]));
                int componentIndex = i;
                cSlider.setCallback((v) -> {
                   int c = (int) (v * 255);
                   cInput.setValue(Integer.toString(c));
                });
                IntegerInputHandler cHandler = new IntegerInputHandler().setValueConsumer(v -> {
                    if (v < 0 || v > 255) throw new RuntimeException("Component value has to be in range from 0 to 255");
                    components[componentIndex] = v;
                    int color = argb(components[3], components[0], components[1], components[2]);
                    consumer.accept(parent, color);
                    currentValue.set(color);
                    parent.applyValues();
                    cSlider.setUseCallback(false).setProgress(v / 255f).setUseCallback(true);
                });
                cInput.setChangeCallback(cHandler).setTextBaker(cHandler);
                cInput.setUpdater(t -> {
                    int value = provider.apply(parent);
                    if (value != currentValue.get()) {
                        components[0] = ARGB32.red(value);
                        components[1] = ARGB32.green(value);
                        components[2] = ARGB32.blue(value);
                        components[3] = ARGB32.alpha(value);
                        currentValue.set(value);
                    }
                    cInput.setUseCallback(false).setValue(Integer.toString(components[componentIndex])).setUseCallback(true);
                    cSlider.setUseCallback(false).setProgress(components[componentIndex] / 255f).setUseCallback(true);
                });
                container.addWidget(cLabel);
                container.addWidget(cSlider);
                container.addWidget(cInput);
                cY += 10;
            }

            return container;
        };
    }

    private static class BreezeGradient implements ColorProvider {
        final float y0, y1;
        final float topR, topG, topB, topA;
        final float bottomR, bottomG, bottomB, bottomA;

        private BreezeGradient(float y0, float y1, float topR, float topG, float topB, float topA, float bottomR, float bottomG, float bottomB, float bottomA) {
            this.y0 = y0;
            this.y1 = y1;
            this.topR = topR;
            this.topG = topG;
            this.topB = topB;
            this.topA = topA;
            this.bottomR = bottomR;
            this.bottomG = bottomG;
            this.bottomB = bottomB;
            this.bottomA = bottomA;
        }


        @Override
        public int get(float x, float y) {
            float progress = (y - y0) / (y1 - y0);
            int r = (int) ((topR + (progress * (bottomR - topR))) * 255);
            int g = (int) ((topG + (progress * (bottomG - topG))) * 255);
            int b = (int) ((topB + (progress * (bottomB - topB))) * 255);
            int a = (int) ((topA + (progress * (bottomA - topA))) * 255);
            return argb(a, r, g, b);
        }
    }
}
