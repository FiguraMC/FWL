package org.figuramc.fwl.gui.themes;

import net.minecraft.client.gui.GuiGraphics;
import org.figuramc.fwl.FWL;
import org.figuramc.fwl.gui.widgets.descriptors.*;
import org.figuramc.fwl.gui.widgets.descriptors.button.ButtonDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.button.CheckboxDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.button.ClickableDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.button.RadioButtonDescriptor;
import org.figuramc.fwl.utils.ColorUtils;
import org.figuramc.fwl.utils.TreePathMap;

import static net.minecraft.util.FastColor.ARGB32;


public class FWLBreeze extends FWLTheme {
    private static final TreePathMap<Integer> BREEZE_COLORS = new TreePathMap<>() {{
        add(ColorTypes.BUTTON, 0xFF3A3A3A);

        add(ColorTypes.PRIMARY, 0xFF3A3A3A);
        add(ColorTypes.SECONDARY, 0xFF14539E);
        add(ColorTypes.SUCCESS, 0xFF25AA61);

        add(ColorTypes.BORDER, 0xFF111111);

        add(ColorTypes.TEXT, 0xFFFFFFFF);
        add(ColorTypes.TEXT_DISABLED, 0xFF999999);
        add(ColorTypes.TEXT_HINT, 0xFF999999);

        add(ColorTypes.BUTTON_TEXT, 0xFFFFFFFF);
        add(0xFF777777, ColorTypes.BUTTON_TEXT.getPath(), "disabled");
    }};

    @Override
    public void renderButton(GuiGraphics graphics, float delta, ButtonDescriptor button) {
        // Get current theme. Needed for combined themes compatibility. Used for getting colors for buttons.
        FWLTheme theme = FWL.peekTheme();

        float x = button.x(), y = button.y(), width = button.width(), height = button.height();
        // Border radius
        float rad = Math.min(3, Math.min(button.width(), button.height()) / 2);

        // Getting button color by its type, and then getting gradient for it. Breeze theme doesn't check for namespace
        int buttonColor = applyStateModifier(theme.getColorOrDefault(button.type(), 0xFFFFFFFF), button);

        int borderColor = theme.getColorOrDefault(button.focused() ? ColorTypes.SECONDARY : ColorTypes.BORDER, 0xFF000000); // Getting border color. Breeze theme doesn't check for namespace

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
        // Get current theme. Needed for combined themes compatibility. Used for getting colors for buttons.
        FWLTheme theme = FWL.peekTheme();

        float x = checkbox.x(), y = checkbox.y(), width = checkbox.width(), height = checkbox.height();
        // Border radius
        float rad = Math.min(3, Math.min(checkbox.width(), checkbox.height()) / 2);

        // Getting button color by its type, and then getting gradient for it. Breeze theme doesn't check for namespace
        int buttonColor = applyStateModifier(theme.getColorOrDefault(checkbox.checked() ? ColorTypes.SECONDARY : ColorTypes.BUTTON, 0xFFFFFFFF), checkbox);

        int borderColor = theme.getColorOrDefault(checkbox.focused() ? ColorTypes.SECONDARY : ColorTypes.BORDER, 0xFF000000); // Getting border color. Breeze theme doesn't check for namespace

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
        // Get current theme. Needed for combined themes compatibility. Used for getting colors for buttons.
        FWLTheme theme = FWL.peekTheme();

        float x = radioButton.x(), y = radioButton.y(), width = radioButton.width(), height = radioButton.height();
        // Border radius
        float rad = Math.min(radioButton.width(), radioButton.height()) / 2;


        // Getting button color by its type, and then getting gradient for it. Breeze theme doesn't check for namespace
        int buttonColor = applyStateModifier(theme.getColorOrDefault(radioButton.active() ? ColorTypes.SECONDARY : ColorTypes.BUTTON, 0xFFFFFFFF), radioButton);

        int borderColor = theme.getColorOrDefault(radioButton.focused() ? ColorTypes.SECONDARY : ColorTypes.BORDER, 0xFF000000); // Getting border color. Breeze theme doesn't check for namespace

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
        // Get current theme. Needed for combined themes compatibility. Used for getting colors for the scroll bar.
        FWLTheme theme = FWL.peekTheme();

        float x = scrollBar.x(), y = scrollBar.y(), width = scrollBar.width(), height = scrollBar.height();
        float inX = x + 2, inY = y + 2, inWidth = width - 4, inHeight = height - 4;
        float rad = Math.min(3, Math.min(width, height) / 2);
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

        int bgColor = theme.getColorOrDefault(ColorTypes.PRIMARY, 0xFFFFFFFF);
        int barColor = applyStateModifier(getColorOrDefault(ColorTypes.SECONDARY, 0xFFFFFFFF), scrollBar);
        int borderColor = theme.getColorOrDefault(scrollBar.focused() ? ColorTypes.SECONDARY : ColorTypes.BORDER, 0xFF000000); // Getting border color. Breeze theme doesn't check for namespace

        renderRoundBg(graphics, bgColor, x, y, x1, y1, rad, sc);
        renderRoundBg(graphics, new StaticColor(barColor), barX, barY, barX1, barY1, barRad, sc);
        renderRoundBorder(graphics, borderColor, x, y, x1, y1, rad, sc, thickness);
    }

    @Override
    public void renderSlider(GuiGraphics graphics, float delta, SliderDescriptor slider) {

    }

    @Override
    public void renderIcon(GuiGraphics graphics, IconDescriptor icon) {

    }

    @Override
    public void renderPane(GuiGraphics graphics, float delta, float x, float y, float width, float height) {
        // Get current theme. Needed for combined themes compatibility. Used for getting color.
        FWLTheme theme = FWL.peekTheme();

        // Border
        float rad = Math.min(3, Math.min(width, height) / 2);
        float sc = getWindowScaling();
        float thickness = 2;

        int color = theme.getColorOrDefault(ColorTypes.PRIMARY, 0xFFFFFFFF); // Getting pane color. Breeze theme doesn't check for namespace
        int border = theme.getColorOrDefault(ColorTypes.BORDER, 0xFFFFFFFF); // Getting border color. Breeze theme doesn't check for namespace

        float x1 = x + width, y1 = y + height;

        renderRoundBg(graphics, color, x, y, x1, y1, rad, sc);
        renderRoundBorder(graphics, border, x, y, x1, y1, rad, sc, thickness);
    }

    private void renderRoundBg(GuiGraphics graphics, int color, float x0, float y0, float x1, float y1, float rad, float scaling) {
        renderRoundBg(graphics, getBreezeColorGradient(color, x0, y0, x1, y1), x0, y0, x1, y1, rad, scaling);
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
        if (state.disabled()) return ColorUtils.argb(a, r - 30, g - 30, b - 30);
        if (state.clicked()) return ColorUtils.argb(a, r - 20, g - 20, b - 20);
        if (state.hovered()) return ColorUtils.argb(a, r - 10, g - 10, b - 10);
        return color;
    }

    public int applyStateModifier(int color, CheckboxDescriptor state) {
        int r = ARGB32.red(color);
        int g = ARGB32.green(color);
        int b = ARGB32.blue(color);
        int a = ARGB32.alpha(color);
        if (state.disabled()) return ColorUtils.argb(a, r - 30, g - 30, b - 30);
        if (state.clicked()) return ColorUtils.argb(a, r - 20, g - 20, b - 20);
        if (state.hovered()) return ColorUtils.argb(a, r - 10, g - 10, b - 10);
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
            return ColorUtils.argb(a, r, g, b);
        }
    }
}
