package org.lexize.lwl.themes;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.joml.Vector3i;
import org.joml.Vector4i;
import org.lexize.lwl.LWL;
import org.lexize.lwl.utils.TreePathMap;
import org.lexize.lwl.widgets.descriptors.*;

import static org.lexize.lwl.themes.LWLTheme.ArcOrient.NEGATIVE;
import static org.lexize.lwl.themes.LWLTheme.ArcOrient.POSITIVE;
import static net.minecraft.util.FastColor.ARGB32;


public class LWLBreeze extends LWLTheme {
    private static final TreePathMap<Integer> BREEZE_COLORS = new TreePathMap<>() {{
        add("button", 0xFF3A3A3A);
        add("button/disabled", 0xFF323232);
        add("primary", 0xFF14539E);
        add("secondary", 0xFF14539E);
        add("success", 0xFF25AA61);
        add("border", 0xFF111111);
        add("text", 0xFFFFFFFF);
        add("text/disabled", 0xFF999999);
        add("text/hint", 0xFF999999);
    }};

    @Override
    public void renderButton(GuiGraphics graphics, float delta, ButtonDescriptor button) {
        // Get current theme. Needed for combined themes compatibility. Used for getting colors for buttons.
        LWLTheme theme = LWL.peekTheme();

        float x = button.x(), y = button.y(), width = button.width(), height = button.height();
        // Border radius
        float rad = Math.min(4, Math.min(button.width(), button.height()) / 2);

        // Getting button color by its type, and then getting gradient for it. Breeze theme doesn't check for namespace
        Vector4i buttonColor = getBreezeColorGradient(theme.getColorOrDefault(button.type(), 0xFFFFFFFF, button.state()));
        int baseButtonColor = buttonColor.x;
        int topMiddleButtonColor = buttonColor.y;
        int bottomMiddleButtonColor = buttonColor.z;
        int bottomButtonColor = buttonColor.w;

        int borderColor = theme.getColorOrDefault(button.state() == WidgetState.FOCUSED ? ColorTypes.PRIMARY : ColorTypes.BORDER, 0xFF000000); // Getting border color. Breeze theme doesn't check for namespace

        float x1 = x+width;
        float y1 = y+height;

        float sc = getScaling();
        float lh = 1 / sc; // Line height

        // Button background
        fill(graphics, x + rad, y, x1 - rad, y + rad, 0, baseButtonColor); // Top
        fill(graphics, x, y + rad, x1, y + (height / 2), 0, topMiddleButtonColor); // Higher middle
        fill(graphics, x, y + (height / 2), x1, y1 - rad, 0, bottomMiddleButtonColor); // Higher middle
        fill(graphics, x + rad, y1 - rad, x1 - rad, y1, 0, bottomButtonColor); // Bottom
        renderArc(graphics, x + rad, y, 0, rad, sc, NEGATIVE, POSITIVE, true, baseButtonColor); // Top-left corner
        renderArc(graphics, x1 - rad, y, 0, rad, sc, POSITIVE, POSITIVE, true, baseButtonColor); // Top-right corner
        renderArc(graphics, x + rad, y1, 0, rad, sc, NEGATIVE, NEGATIVE, true, bottomButtonColor); // Bottom-left corner
        renderArc(graphics, x1 - rad, y1, 0, rad, sc, POSITIVE, NEGATIVE, true, bottomButtonColor); // Bottom-right corner

        // Button border
        fill(graphics, x + rad, y, x1 - rad, y + lh, 0, borderColor); // Top-middle
        fill(graphics, x + rad, y1 - lh, x1 - rad, y1, 0, borderColor); // Bottom-middle
        fill(graphics, x, y + rad, x + lh, y1 - rad, 0, borderColor); // Left-center
        fill(graphics, x1 - lh, y + rad, x1, y1 - rad, 0, borderColor); // Right-center
        renderArc(graphics, x + rad, y, 0, rad, sc, NEGATIVE, POSITIVE, false, borderColor); // Top-left corner
        renderArc(graphics, x1 - rad, y, 0, rad, sc, POSITIVE, POSITIVE, false, borderColor); // Top-right corner
        renderArc(graphics, x + rad, y1, 0, rad, sc, NEGATIVE, NEGATIVE, false, borderColor); // Bottom-left corner
        renderArc(graphics, x1 - rad, y1, 0, rad, sc, POSITIVE, NEGATIVE, false, borderColor); // Bottom-right corner
    }

    @Override
    public void renderCheckbox(GuiGraphics graphics, float delta, CheckboxDescriptor checkbox) {

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
    public Integer getColor(ResourceLocation type) {
        return BREEZE_COLORS.get(type.getPath());
    }

    @Override
    public int applyStateModifier(int color, WidgetState state) {
        int r = ARGB32.red(color);
        int g = ARGB32.green(color);
        int b = ARGB32.blue(color);
        int a = ARGB32.alpha(color);
        return switch (state) {
            case DEFAULT, FOCUSED -> color;
            case HOVERED -> ARGB32.color(a, r - 10, g - 10, b - 10);
            case CLICKED -> ARGB32.color(a, r - 20, g - 20, b - 20);
        };
    }

    private Vector4i getBreezeColorGradient(int baseColor) {
        int r = ARGB32.red(baseColor);
        int g = ARGB32.green(baseColor);
        int b = ARGB32.blue(baseColor);
        int a = ARGB32.alpha(baseColor);

        int topMiddleColor = ARGB32.color(a, r - 2, g - 2, b - 2);
        int bottomMiddleColor = ARGB32.color(a, r - 4, g - 4, b - 4);
        int bottomColor = ARGB32.color(a, r - 6, g - 6, b - 6);
        return new Vector4i(baseColor, topMiddleColor, bottomMiddleColor, bottomColor);
    }
}
