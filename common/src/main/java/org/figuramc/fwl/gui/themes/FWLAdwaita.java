package org.figuramc.fwl.gui.themes;

import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.gui.widgets.descriptors.BoundsDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.ClickableDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.Orientation;
import org.figuramc.fwl.gui.widgets.descriptors.ScrollBarDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.button.ButtonDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.button.CheckboxDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.button.RadioButtonDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.input.SliderDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.input.TextInputDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.misc.ContextMenuDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.misc.IconDescriptor;
import org.figuramc.fwl.gui.widgets.misc.ContextMenu;
import org.figuramc.fwl.utils.Rectangle;
import org.figuramc.fwl.utils.RenderUtils;
import org.figuramc.fwl.utils.TreePathMap;
import org.jetbrains.annotations.Nullable;

import static org.figuramc.fwl.utils.ColorUtils.argb;
import static org.figuramc.fwl.utils.JsonUtils.intOrDefault;
import static org.figuramc.fwl.utils.MathUtils.clamp;
import static org.figuramc.fwl.utils.MathUtils.lerp;
import static org.figuramc.fwl.utils.RenderUtils.*;

import static net.minecraft.util.FastColor.ARGB32;

public class FWLAdwaita extends FWLTheme {
    private static final StaticColor WHITE_COLOR = new StaticColor(0xFFFFFFFF);

    private TreePathMap<Integer> colors;

    private int borderRadius = 2;
    private int checkboxBorderRadius = 3;
    private int scrollbarBorderRadius = 3;
    private int textboxBorderRadius = 2;
    private int contextMenuBorderRadius = 2;
    private int transitionTicks = 2;

    private int textColor;
    private int textHintColor;
    private int disabledTextColor;

    private int primaryColor;
    private int accentColor;
    private int successColor;

    private int buttonColor;

    public FWLAdwaita() {
        this(null);
    }

    public FWLAdwaita(@Nullable JsonObject preset) {
        applyPreset(preset);
    }

    @Override
    public void renderButton(GuiGraphics graphics, float delta, ButtonDescriptor button) {
        float x = button.x(), y = button.y(), width = button.width(), height = button.height();
        // Border radius
        float rad = Math.min(button.width(), button.height()) / 2;

        // Getting button color by its type, and then getting gradient for it. Breeze theme doesn't check for namespace
        int buttonColor = applyStateModifier(getColorOrDefault(button.type(), 0xFFFFFFFF), delta, button);

        float x1 = x+width;
        float y1 = y+height;

        float sc = getWindowScaling();

        // Button background
        renderRoundBg(graphics, new RenderUtils.StaticColor(buttonColor), x, y, x1, y1, rad, sc);
    }

    @Override
    public void renderCheckbox(GuiGraphics graphics, float delta, CheckboxDescriptor checkbox) {
        float x = checkbox.x(), y = checkbox.y(), width = checkbox.width(), height = checkbox.height();
        // Border radius
        float rad = Math.min(checkboxBorderRadius, Math.min(checkbox.width(), checkbox.height()) / 2);

        int buttonColor = applyStateModifier(getColorBlend(checkbox.checkedTicks(), delta, checkbox.checked(), ColorTypes.BUTTON, ColorTypes.SECONDARY), delta, checkbox);

        float x1 = x+width;
        float y1 = y+height;

        float sc = getWindowScaling();

        // Button background
        renderRoundBg(graphics, new RenderUtils.StaticColor(buttonColor), x, y, x1, y1, rad, sc);
    }

    @Override
    public void renderRadioButton(GuiGraphics graphics, float delta, RadioButtonDescriptor radioButton) {
        float x = radioButton.x(), y = radioButton.y(), width = radioButton.width(), height = radioButton.height();
        // Border radius
        float rad = Math.min(radioButton.width(), radioButton.height()) / 2;


        // Getting button color by its type, and then getting gradient for it. Adwaita theme doesn't check for namespace
        int buttonColor = applyStateModifier(getColorBlend(radioButton.activeTicks(), delta, radioButton.active(), ColorTypes.BUTTON, ColorTypes.SECONDARY), delta, radioButton);

        int borderColor = getColorBlend(radioButton.focusedTicks(), delta, radioButton.focused(), ColorTypes.BUTTON, ColorTypes.SECONDARY);

        float x1 = x+width;
        float y1 = y+height;

        float sc = getWindowScaling();
        float thickness = 2;

        // Button background
        renderRoundBg(graphics, new StaticColor(buttonColor), x, y, x1, y1, rad, sc);

        // Button border
        renderRoundBorder(graphics, borderColor, x, y, x1, y1, rad, sc, thickness);
    }

    @Override
    public void renderScrollBar(GuiGraphics graphics, float delta, ScrollBarDescriptor scrollBar) {
        float x = scrollBar.x(), y = scrollBar.y(), width = scrollBar.width(), height = scrollBar.height();
        float inX = x + 2, inY = y + 2, inWidth = width - 4, inHeight = height - 4;
        float rad = Math.min(scrollbarBorderRadius, Math.min(width, height) / 2);
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
        int barColor = applyStateModifier(getColorOrDefault(ColorTypes.SECONDARY, 0xFFFFFFFF), delta, scrollBar);
        int borderColor = getColorBlend(scrollBar.focusedTicks(), delta, scrollBar.focused(), ColorTypes.SECONDARY, ColorTypes.BORDER);

        renderRoundBg(graphics, new StaticColor(bgColor), x, y, x1, y1, rad, sc);
        RenderUtils.renderRoundBg(graphics, new StaticColor(barColor), barX, barY, barX1, barY1, barRad, sc);
        renderRoundBorder(graphics, borderColor, x, y, x1, y1, rad, sc, thickness);
    }

    @Override
    public void renderTextInput(GuiGraphics graphics, float delta, TextInputDescriptor input) {
        float x = input.x(), y = input.y(), width = input.width(), height = input.height();
        float rad = Math.min(textboxBorderRadius, Math.min(width, height) / 2);

        float x1 = x + width;
        float y1 = y + height;

        float sc = getWindowScaling();

        int bgColor = getColorOrDefault(ColorTypes.PRIMARY, 0xAAAAAAAA);
        int borderColor = getColorBlend(input.focusedTicks(), delta, input.focused(), ColorTypes.BORDER, ColorTypes.SECONDARY);

        float thickness = 2;

        renderRoundBg(graphics, new StaticColor(bgColor), x, y, x1, y1, rad, sc);
        renderRoundBorder(graphics, borderColor, x, y, x1, y1, rad, sc, thickness);
    }

    @Override
    public void renderContextMenu(GuiGraphics graphics, float delta, ContextMenuDescriptor menu) {
        float x = menu.x(), y = menu.y(), width = menu.width(), height = menu.height();
        float rad = Math.min(contextMenuBorderRadius, Math.min(width, height) / 2);

        float x1 = x + width;
        float y1 = y + height;

        float sc = getWindowScaling();

        int bgColor = getColorOrDefault(ColorTypes.PRIMARY, 0xAAAAAAAA);
        int borderColor = getColorOrDefault(ColorTypes.BORDER, 0xFF000000); // Getting border color. Breeze theme doesn't check for namespace

        float thickness = 2;

        renderRoundBg(graphics, new StaticColor(bgColor), x, y, x1, y1, rad, sc);
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

        boolean topActive = bounds.isPresent(BoundsDescriptor.Side.TOP);
        boolean bottomActive = bounds.isPresent(BoundsDescriptor.Side.BOTTOM);
        boolean leftActive = bounds.isPresent(BoundsDescriptor.Side.LEFT);
        boolean rightActive = bounds.isPresent(BoundsDescriptor.Side.RIGHT);

        boolean topInner = bounds.isInner(BoundsDescriptor.Side.TOP);
        boolean bottomInner = bounds.isInner(BoundsDescriptor.Side.BOTTOM);
        boolean leftInner = bounds.isInner(BoundsDescriptor.Side.LEFT);
        boolean rightInner = bounds.isInner(BoundsDescriptor.Side.RIGHT);

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
        int bgColor = applyStateModifier(getColorOrDefault(primary, bounds.widgetType()), delta, bounds); // Getting border color. Breeze theme doesn't check for namespace
        StaticColor color = new StaticColor(bgColor);

        boolean topActive = bounds.isPresent(BoundsDescriptor.Side.TOP);
        boolean bottomActive = bounds.isPresent(BoundsDescriptor.Side.BOTTOM);
        boolean leftActive = bounds.isPresent(BoundsDescriptor.Side.LEFT);
        boolean rightActive = bounds.isPresent(BoundsDescriptor.Side.RIGHT);

        boolean topInner = bounds.isInner(BoundsDescriptor.Side.TOP);
        boolean bottomInner = bounds.isInner(BoundsDescriptor.Side.BOTTOM);
        boolean leftInner = bounds.isInner(BoundsDescriptor.Side.LEFT);
        boolean rightInner = bounds.isInner(BoundsDescriptor.Side.RIGHT);

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
        int sliderColor = applyStateModifier(getColorOrDefault(ColorTypes.SECONDARY, 0xFF777777), delta, slider);
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

        renderRoundBg(graphics, new StaticColor(sliderColor), sliderX, sliderY, sliderX + sliderSize, sliderY + sliderSize, sliderRad, scaling);
        renderRoundBorder(graphics, sliderBorderColor, sliderX, sliderY, sliderX + sliderSize, sliderY + sliderSize, sliderRad, scaling, 1);
    }

    @Override
    public void renderIcon(GuiGraphics graphics, IconDescriptor icon) {

    }

    public void applyValues() {
        colors = new TreePathMap<>() {{
            add(ColorTypes.BUTTON, buttonColor);

            add(ColorTypes.PRIMARY, primaryColor);
            add(ColorTypes.SECONDARY, accentColor);
            add(ColorTypes.SUCCESS, successColor);

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

            textColor = intOrDefault(preset, "color.text", 0xFFFFFFFF);
            textHintColor = intOrDefault(preset, "color.text.hint", 0xFFFFFFFF);
            disabledTextColor = intOrDefault(preset, "color.text.disabled", 0xFF777777);

            borderRadius = intOrDefault(preset, "border.radius", 3);

            buttonColor = intOrDefault(preset, "border.radius", 0xFF4A4A4A);
        }
        else {
            primaryColor = 0xFF3A3A3A;
            accentColor = 0xFF3473BE;
            successColor = 0xFF25AA61;

            textColor = 0xFFFFFFFF;
            textHintColor = 0xFF999999;
            disabledTextColor = 0xFF777777;

            borderRadius = 2;

            buttonColor = 0xFF4A4A4A;
        }
        applyValues();
    }

    @Override
    public @Nullable FWLWidget settingsRootWidget(float areaWidth, float areaHeight) {
        return null;
    }

    @Override
    public Integer getColor(String... path) {
        return colors.get(path);
    }

    public int applyStateModifier(int color, float delta, ClickableDescriptor state) {
        int r = ARGB32.red(color);
        int g = ARGB32.green(color);
        int b = ARGB32.blue(color);
        int a = ARGB32.alpha(color);

        float disabledTicks = state.disabled() ? state.disabledTicks() + delta : transitionTicks + (state.disabledTicks() - delta);
        float disabledProgress = clamp(disabledTicks / transitionTicks, 0, 1);

        float clickedTicks = state.clicked() ? state.clickedTicks() + delta : transitionTicks + (state.clickedTicks() - delta);
        float clickedProgress = clamp(clickedTicks / transitionTicks, 0, 1);

        float hoveredTicks = state.hovered() ? state.hoveredTicks() + delta : transitionTicks + (state.hoveredTicks() - delta);
        float hoveredProgress = clamp(hoveredTicks / transitionTicks, 0, 1);

        int rR = r;
        int rG = g;
        int rB = b;

        if (hoveredProgress > 0) {
            rR = lerp(rR, r - 10, hoveredProgress);
            rG = lerp(rG, g - 10, hoveredProgress);
            rB = lerp(rB, b - 10, hoveredProgress);
        }

        if (clickedProgress > 0) {
            rR = lerp(rR, r - 20, clickedProgress);
            rG = lerp(rG, g - 20, clickedProgress);
            rB = lerp(rB, b - 20, clickedProgress);
        }

        if (disabledProgress > 0) {
            rR = lerp(rR, r - 30, disabledProgress);
            rG = lerp(rG, g - 30, disabledProgress);
            rB = lerp(rB, b - 30, disabledProgress);
        }

        return argb(a, rR, rG, rB);
    }

    private int getColorBlend(int ticks, float delta, boolean state, ResourceLocation color1, ResourceLocation color2) {
        float t = state ? ticks + delta : transitionTicks + (ticks - delta);
        int fromColor = getColorOrDefault(color1, 0xFFFFFFFF);
        if (t <= 0) return fromColor;
        int toColor = getColorOrDefault(color2, 0xFFFFFFFF);
        if (t >= transitionTicks) return toColor;
        int fR = ARGB32.red(fromColor), fG = ARGB32.green(fromColor), fB = ARGB32.blue(fromColor), fA = ARGB32.alpha(fromColor);
        int tR = ARGB32.red(fromColor), tG = ARGB32.green(fromColor), tB = ARGB32.blue(fromColor), tA = ARGB32.alpha(fromColor);
        float progress = clamp((t) / transitionTicks, 0, 1);
        return argb(
                lerp(fA, tA, progress),
                lerp(fR, tR, progress),
                lerp(fG, tG, progress),
                lerp(fB, tB, progress)
        );
    }
}
