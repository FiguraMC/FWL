package org.lexize.lwl.gui.themes;

import net.minecraft.client.gui.GuiGraphics;
import org.joml.Vector4i;
import org.lexize.lwl.LWL;
import org.lexize.lwl.gui.widgets.descriptors.*;
import org.lexize.lwl.gui.widgets.descriptors.button.ClickableDescriptor;
import org.lexize.lwl.gui.widgets.descriptors.button.CheckboxDescriptor;
import org.lexize.lwl.gui.widgets.descriptors.button.RadioButtonDescriptor;
import org.lexize.lwl.gui.widgets.descriptors.button.ButtonDescriptor;
import org.lexize.lwl.utils.TreePathMap;

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
        float thickness = 3;

        // Button background
        renderRoundBg(graphics, buttonColor, x, y, x1, y1, rad, sc);

        // Button border
        renderRoundBorder(graphics, borderColor, x, y, x1, y1, rad, sc, thickness);
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
        float thickness = 3;

        // Button background
        renderRoundBg(graphics, buttonColor, x, y, x1, y1, rad, sc);

        // Button border
        renderRoundBorder(graphics, borderColor, x, y, x1, y1, rad, sc, thickness);
    }

    @Override
    public void renderRadioButton(GuiGraphics graphics, float delta, RadioButtonDescriptor radioButton) {
        // Get current theme. Needed for combined themes compatibility. Used for getting colors for buttons.
        LWLTheme theme = LWL.peekTheme();

        float x = radioButton.x(), y = radioButton.y(), width = radioButton.width(), height = radioButton.height();
        // Border radius
        float rad = Math.min(radioButton.width(), radioButton.height()) / 2;


        // Getting button color by its type, and then getting gradient for it. Breeze theme doesn't check for namespace
        int buttonColor = applyStateModifier(theme.getColorOrDefault(radioButton.active() ? PRIMARY : BUTTON, 0xFFFFFFFF), radioButton);

        int borderColor = theme.getColorOrDefault(radioButton.focused() ? ColorTypes.PRIMARY : ColorTypes.BORDER, 0xFF000000); // Getting border color. Breeze theme doesn't check for namespace

        float x1 = x+width;
        float y1 = y+height;

        float sc = getScaling();
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
        BreezeGradient gradient = getBreezeColorGradient(color, x0, y0, x1, y1);

        fill(graphics, x0 + rad, y0, x1 - rad, y1, 0, gradient); // Middle
        fill(graphics, x0, y0 + rad, x0 + rad, y1 - rad, 0, gradient); // Left center
        fill(graphics, x1 - rad, y0 + rad, x1, y1 - rad, 0, gradient); // Right center
        renderArcFilled(graphics, x0 + rad, y0, 0, rad, scaling, ArcOrient.NEGATIVE, ArcOrient.POSITIVE, gradient); // Top-left corner
        renderArcFilled(graphics, x1 - rad, y0, 0, rad, scaling, ArcOrient.POSITIVE, ArcOrient.POSITIVE, gradient); // Top-right corner
        renderArcFilled(graphics, x0 + rad, y1, 0, rad, scaling, ArcOrient.NEGATIVE, ArcOrient.NEGATIVE, gradient); // Bottom-left corner
        renderArcFilled(graphics, x1 - rad, y1, 0, rad, scaling, ArcOrient.POSITIVE, ArcOrient.NEGATIVE, gradient); // Bottom-right corner
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
