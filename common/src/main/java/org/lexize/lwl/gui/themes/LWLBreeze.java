package org.lexize.lwl.gui.themes;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector4i;
import org.lexize.lwl.LWL;
import org.lexize.lwl.gui.widgets.descriptors.*;
import org.lexize.lwl.utils.TreePathMap;

import java.util.Arrays;

import static net.minecraft.util.FastColor.ARGB32;
import static org.lexize.lwl.gui.themes.ColorTypes.*;
import static org.lexize.lwl.utils.ColorUtils.argb;


public class LWLBreeze extends LWLTheme {
    private static final TreePathMap<Integer> BREEZE_COLORS = new TreePathMap<>() {{
        add(BUTTON, 0xFF3A3A3A);

        add(PRIMARY, 0xFF14539E);
        add(SECONDARY, 0xFF14539E);
        add(SUCCESS, 0xFF25AA61);

        add(BORDER, 0xFF111111);

        add(TEXT, 0xFFFFFFFF);
        add(TEXT_DISABLED, 0xFF999999);
        add(TEXT_HINT, 0xFF999999);

        add(BUTTON_TEXT, 0xFFFFFFFF);
        add(0xFF777777, BUTTON_TEXT.getPath(), "disabled");
    }};

    @Override
    public void renderButton(GuiGraphics graphics, float delta, ButtonDescriptor button) {
        // Get current theme. Needed for combined themes compatibility. Used for getting colors for buttons.
        LWLTheme theme = LWL.peekTheme();

        float x = button.x(), y = button.y(), width = button.width(), height = button.height();
        // Border radius
        float rad = Math.min(3, Math.min(button.width(), button.height()) / 2);

        // Getting button color by its type, and then getting gradient for it. Breeze theme doesn't check for namespace
        int buttonColor = applyStateModifier(theme.getColorOrDefault(button.type(), 0xFFFFFFFF), button);

        int borderColor = theme.getColorOrDefault(button.focused() ? ColorTypes.PRIMARY : ColorTypes.BORDER, 0xFF000000); // Getting border color. Breeze theme doesn't check for namespace

        float x1 = x+width;
        float y1 = y+height;

        float sc = getScaling();

        // Button background
        renderRoundBg(graphics, buttonColor, x, y, x1, y1, rad, sc);

        // Button border
        renderRoundBorder(graphics, borderColor, x, y, x1, y1, rad, sc);
    }

    @Override
    public void renderCheckbox(GuiGraphics graphics, float delta, CheckboxDescriptor checkbox) {
        // Get current theme. Needed for combined themes compatibility. Used for getting colors for buttons.
        LWLTheme theme = LWL.peekTheme();

        float x = checkbox.x(), y = checkbox.y(), width = checkbox.width(), height = checkbox.height();
        // Border radius
        float rad = Math.min(3, Math.min(checkbox.width(), checkbox.height()) / 2);

        // Getting button color by its type, and then getting gradient for it. Breeze theme doesn't check for namespace
        int buttonColor = applyStateModifier(theme.getColorOrDefault(checkbox.checked() ? PRIMARY : BUTTON, 0xFFFFFFFF), checkbox);

        int borderColor = theme.getColorOrDefault(checkbox.focused() ? ColorTypes.PRIMARY : ColorTypes.BORDER, 0xFF000000); // Getting border color. Breeze theme doesn't check for namespace

        float x1 = x+width;
        float y1 = y+height;

        float sc = getScaling();

        // Button background
        renderRoundBg(graphics, buttonColor, x, y, x1, y1, rad, sc);

        // Button border
        renderRoundBorder(graphics, borderColor, x, y, x1, y1, rad, sc);
    }

    @Override
    public void renderRadioButton(GuiGraphics graphics, float delta, RadioButtonDescriptor button) {

    }

    @Override
    public void renderScrollBar(GuiGraphics graphics, float delta, ScrollBarDescriptor scrollBar) {

    }

    @Override
    public void renderSlider(GuiGraphics graphics, float delta, SliderDescriptor slider) {

    }

    @Override
    public void renderBorder(GuiGraphics graphics, BorderDescriptor border) {

    }

    @Override
    public void renderSplitter(GuiGraphics graphics, SplitterDescriptor splitter) {

    }

    @Override
    public void renderIcon(GuiGraphics graphics, IconDescriptor icon) {

    }

    private void renderRoundBg(GuiGraphics graphics, int color, float x0, float y0, float x1, float y1, float rad, float scaling) {
        Vector4i gradient = getBreezeColorGradient(color);
        int baseColor = gradient.x;
        int topMiddleColor = gradient.y;
        int bottomMiddleColor = gradient.z;
        int bottomColor = gradient.w;

        float height = y1 - y0;

        fill(graphics, x0 + rad, y0, x1 - rad, y0 + rad, 0, baseColor); // Top
        fill(graphics, x0, y0 + rad, x1, y0 + (height / 2), 0, topMiddleColor); // Higher middle
        fill(graphics, x0, y0 + (height / 2), x1, y1 - rad, 0, bottomMiddleColor); // Higher middle
        fill(graphics, x0 + rad, y1 - rad, x1 - rad, y1, 0, bottomColor); // Bottom
        renderArcFilled(graphics, x0 + rad, y0, 0, rad, scaling, ArcOrient.NEGATIVE, ArcOrient.POSITIVE, baseColor); // Top-left corner
        renderArcFilled(graphics, x1 - rad, y0, 0, rad, scaling, ArcOrient.POSITIVE, ArcOrient.POSITIVE, baseColor); // Top-right corner
        renderArcFilled(graphics, x0 + rad, y1, 0, rad, scaling, ArcOrient.NEGATIVE, ArcOrient.NEGATIVE, bottomColor); // Bottom-left corner
        renderArcFilled(graphics, x1 - rad, y1, 0, rad, scaling, ArcOrient.POSITIVE, ArcOrient.NEGATIVE, bottomColor); // Bottom-right corner
    }

    private void renderRoundBorder(GuiGraphics graphics, int color, float x0, float y0, float x1, float y1, float rad, float scaling) {
        float thickness = 2;
        float lh = thickness / scaling; // Line height

        fill(graphics, x0 + rad, y0, x1 - rad, y0 + lh, 0, color); // Top-middle
        fill(graphics, x0 + rad, y1 - lh, x1 - rad, y1, 0, color); // Bottom-middle
        fill(graphics, x0, y0 + rad, x0 + lh, y1 - rad, 0, color); // Left-center
        fill(graphics, x1 - lh, y0 + rad, x1, y1 - rad, 0, color); // Right-center
        renderArc(graphics, x0 + rad, y0, 0, rad, scaling, thickness, ArcOrient.NEGATIVE, ArcOrient.POSITIVE, color); // Top-left corner
        renderArc(graphics, x1 - rad, y0, 0, rad, scaling, thickness, ArcOrient.POSITIVE, ArcOrient.POSITIVE, color); // Top-right corner
        renderArc(graphics, x0 + rad, y1, 0, rad, scaling, thickness, ArcOrient.NEGATIVE, ArcOrient.NEGATIVE, color); // Bottom-left corner
        renderArc(graphics, x1 - rad, y1, 0, rad, scaling, thickness, ArcOrient.POSITIVE, ArcOrient.NEGATIVE, color); // Bottom-right corner
    }

    @Override
    public int defaultBorderRadius() {
        // Border radius reference is taken from Breeze Dark theme for KDE Plasma
        return 6;
    }

    @Override
    public BorderDescriptor.CornerType defaultCornerType() {
        return BorderDescriptor.CornerType.ROUND;
    }

    @Override
    public Integer getColor(String... path) {
        return BREEZE_COLORS.get(path);
    }

    public int applyStateModifier(int color, ButtonDescriptor state) {
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

    private Vector4i getBreezeColorGradient(int baseColor) {
        int r = ARGB32.red(baseColor);
        int g = ARGB32.green(baseColor);
        int b = ARGB32.blue(baseColor);
        int a = ARGB32.alpha(baseColor);

        int g1 = 3;
        int g2 = 6;
        int g3 = 9;

        int topMiddleColor = argb(a, r - g1, g - g1, b - g1);
        int bottomMiddleColor = argb(a, r - g2, g - g2, b - g2);
        int bottomColor = argb(a, r - g3, g - g3, b - g3);
        return new Vector4i(baseColor, topMiddleColor, bottomMiddleColor, bottomColor);
    }
}
