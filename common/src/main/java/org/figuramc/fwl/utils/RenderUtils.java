package org.figuramc.fwl.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import org.figuramc.fwl.FWL;
import org.figuramc.fwl.gui.themes.ColorTypes;
import org.figuramc.fwl.gui.themes.FWLTheme;
import org.figuramc.fwl.gui.widgets.descriptors.BoundsDescriptor;
import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.FWLCharSequence;
import org.figuramc.fwl.text.TextRenderer;
import org.figuramc.fwl.text.components.AbstractComponent;
import org.joml.Matrix4f;

import java.util.List;

import static org.figuramc.fwl.utils.MathUtils.clamp;
import static org.figuramc.fwl.utils.TextUtils.splitByNewLine;

public class RenderUtils {
    public static float renderText(GuiGraphics graphics, Component text, float x, float y, float z, float scale, boolean shadow) {
        int color = FWL.fwl().currentTheme().getColorOrDefault(ColorTypes.TEXT, 0xFFFFFFFF);
        return renderText(graphics, text, x, y, z, 1, color, shadow);
    }

    public static float renderText(GuiGraphics graphics, Component text, float x, float y, float z, float scale, int color, boolean shadow) {
        PoseStack stack = graphics.pose();
        Font font = Minecraft.getInstance().font;
        stack.pushPose();
        stack.translate(x, y, z);
        stack.scale(scale, scale, scale);
        int w = graphics.drawString(font, text, 0, 0, color, shadow);
        stack.popPose();
        return w * scale;
    }

    public static float renderText(GuiGraphics graphics, FormattedCharSequence text, float x, float y, float z, float scale, boolean shadow) {
        int color = FWL.fwl().currentTheme().getColorOrDefault(ColorTypes.TEXT, 0xFFFFFFFF);
        return renderText(graphics, text, x, y, z, scale, color, shadow);
    }

    public static float renderText(GuiGraphics graphics, FormattedCharSequence text, float x, float y, float z, float scale, int color, boolean shadow) {
        PoseStack stack = graphics.pose();
        Font font = Minecraft.getInstance().font;
        stack.pushPose();
        stack.translate(x, y, z);
        stack.scale(scale, scale, scale);
        List<FormattedCharSequence> lines = splitByNewLine(text);
        int w = 0;
        int yOffset = 0;
        for (FormattedCharSequence line: lines) {
            w = Math.max(graphics.drawString(font, line, 0, yOffset, color, shadow), w);
            yOffset += font.lineHeight;
        }
        stack.popPose();
        return w * scale;
    }

    public static void renderText(GuiGraphics graphics, AbstractComponent text, float x, float y, float z) {
        renderText(graphics, text::visit, x, y, z);
    }

    public static void renderText(GuiGraphics graphics, FWLCharSequence text, float x, float y, float z) {
        PoseStack stack = graphics.pose();
        stack.pushPose();
        stack.translate(0, 0, z);
        TextRenderer renderer = new TextRenderer(graphics.bufferSource(), x, y, getFont(), stack.last().pose(), Font.DisplayMode.NORMAL, 15728880);
        RenderSystem.enableBlend();
        text.accept(renderer::accept);
        renderer.render();
        graphics.flushIfUnmanaged();
        RenderSystem.disableBlend();
        stack.popPose();
    }

    public static float scrollProgress(float totalTicks, float scrollTicks, float ticks) {
        boolean scrollBack = (ticks / totalTicks) % 2 >= 1;
        float progressTicks = ((ticks) % totalTicks);
        float progress = clamp(progressTicks / scrollTicks, 0, 1);
        return scrollBack ? 1 - progress : progress;
    }

    public static float textWidth(Component text, float scale) {
        Font font = Minecraft.getInstance().font;
        return font.width(text) * scale;
    }

    public static float textWidth(AbstractComponent component) {
        return TextUtils.width(component);
    }

    public static float textHeight(AbstractComponent component) {
        return TextUtils.height(component);
    }

    public static float textWidth(FWLCharSequence component) {
        return TextUtils.width(component);
    }

    public static float textHeight(FWLCharSequence component) {
        return TextUtils.height(component);
    }

    public static float textWidth(FormattedCharSequence text, float scale) {
        Font font = Minecraft.getInstance().font;
        List<FormattedCharSequence> lines = TextUtils.splitByNewLine(text);
        int w = 0;
        for (FormattedCharSequence line : lines) {
            w = Math.max(font.width(line), w);
        }
        return w * scale;
    }

    public static float lineHeight() {
        return Minecraft.getInstance().font.lineHeight;
    }

    public static float textWidth(String text, float scale) {
        Font font = Minecraft.getInstance().font;
        return font.width(text) * scale;
    }

    public static float textHeight(Component text, float scale, float maxWidth) {
        Font font = Minecraft.getInstance().font;
        return font.split(text, (int) (maxWidth / scale)).size() * font.lineHeight * scale;
    }

    public static float textHeight(FormattedCharSequence text, float scale, float maxWidth) {
        Font font = Minecraft.getInstance().font;
        return TextUtils.lines(text) * font.lineHeight * scale;
    }


    public static void renderLine(GuiGraphics graphics, float x0, float y0, float x1, float y1, float z, float scaling, int color) {
        renderLine(graphics, x0, y0, x1, y1, z, scaling, new StaticColor(color));
    }

    public static void renderLine(GuiGraphics graphics, float x0, float y0, float x1, float y1, float z, float scaling, ColorProvider color) {
        float xDif = Math.abs(x1 - x0);
        float yDif = Math.abs(y1 - y0);
        float lineWidth = 1f / scaling;
        if (xDif == 0) fill(graphics, x0, y0, x0 + lineWidth, y1, z, color);
        else if (yDif == 0) fill(graphics, x0, y1, x1, y1 + lineWidth , z, color);
        else renderLineDiagonal(graphics, x0, y0, x1, y1, z, scaling, color);
    }

    private static void renderLineDiagonal(GuiGraphics graphics, float x0, float y0, float x1, float y1, float z, float scaling, ColorProvider color) {
        float width = x1 - x0;
        float height = y1 - y0;
        float steps = Math.abs(Math.min(Math.abs(width), Math.abs(height)) * scaling);
        float xStep = (width / steps);
        float yStep = (height / steps);
        float cX = x0;
        float cY = y0;
        for (int i = 0; i < steps; i++) {
            float nX = cX + xStep;
            float nY = cY + yStep;
            fill(graphics, cX, cY, nX, nY, z, color);
            cX = nX;
            cY = nY;
        }
    }

    public static int[] findArcMaxXs(int r) {
        int x = 0;
        int y = r;
        int[] maxX = new int[r];
        int r2 = r * 3;
        while (x < y) {
            int yMid2 = (y * 3) - 1;
            int x2 = x * 3;
            int p = (x2 * x2) + (yMid2 * yMid2);
            if (p >= r2 * r2) {
                y--;
            }
            int bDist = r - y;
            int i = r - x;
            maxX[bDist] = x;
            if (i < r) maxX[i] = y;
            x++;
        }
        return maxX;
    }

    private static int[] diffArray(int[] src) {
        int[] diffs = new int[src.length];
        for (int i = 0; i < src.length; i++) {
            int prev = i == 0 ? 0 : src[i-1];
            diffs[i] = src[i] - prev;
        }
        return diffs;
    }

    public static void renderArc(GuiGraphics graphics, float sX, float sY, float z, float radius, float scaling, float thickness, ArcOrient sideX, ArcOrient sideY, int color) {
        renderArc(graphics, sX, sY, z, radius, scaling, thickness, sideX, sideY, new StaticColor(color));
    }

    public static void renderArc(GuiGraphics graphics, float sX, float sY, float z, float radius, float scaling, float thickness, ArcOrient sideX, ArcOrient sideY, ColorProvider color) {
        int r = Math.round(radius * scaling);
        int[] xs = diffArray(findArcMaxXs(r));
        renderArcHollow(graphics, sX, sY, z, scaling, thickness, sideX, sideY, color, xs);
    }

    public static void renderArcFilled(GuiGraphics graphics, float sX, float sY, float z, float radius, float scaling, ArcOrient sideX, ArcOrient sideY, int color) {
        renderArcFilled(graphics, sX, sY, z, radius, scaling, sideX, sideY, new StaticColor(color));
    }

    public static void renderArcFilled(GuiGraphics graphics, float sX, float sY, float z, float radius, float scaling, ArcOrient sideX, ArcOrient sideY, ColorProvider color) {
        int r = Math.round(radius * scaling);
        int[] xs = diffArray(findArcMaxXs(r));
        renderArcFilled(graphics, sX, sY, z, scaling, r, sideX, sideY, color, xs);
    }

    public static void renderArcDotted(GuiGraphics graphics, float sX, float sY, float z, float radius, float scaling, float thickness, ArcOrient sideX, ArcOrient sideY, ColorProvider color) {
        int r = Math.round(radius * scaling);
        int[] xs = diffArray(findArcMaxXs(r));
        renderArcDotted(graphics, sX, sY, z, scaling, thickness, sideX, sideY, color, xs);
    }

    private static void renderArcHollow(GuiGraphics graphics, float sX, float sY, float z, float scaling, float thickness, ArcOrient sideX, ArcOrient sideY, ColorProvider color, int[] xs) {
        float xMul = (sideX == ArcOrient.NEGATIVE ? -1 : 1) / scaling;
        float yMul = (sideY == ArcOrient.NEGATIVE ? -1 : 1) / scaling;
        int xOffset = 0;
        float t = thickness - 1;
        for (int y = 0; y < xs.length; y++) {
            int x = xs[y];
            float x0 = ((xOffset - (x == 0 ? 1 : 0)) - t) * xMul;
            float y0 = y * yMul;
            float x1 = (xOffset + x) * xMul;
            float y1 = (y + 1 + t) * yMul;
            fill(graphics, sX + x0, sY + y0, sX + x1, sY + y1, z, color);
            xOffset += x;
        }
    }

    private static void renderArcFilled(GuiGraphics graphics, float sX, float sY, float z, float scaling, int r, ArcOrient sideX, ArcOrient sideY, ColorProvider color, int[] xs) {
        float xMul = (sideX == ArcOrient.NEGATIVE ? -1 : 1) / scaling;
        float yMul = (sideY == ArcOrient.NEGATIVE ? -1 : 1) / scaling;
        int xOffset = 0;
        for (int y = 0; y < xs.length; y++) {
            int x = xs[y];
            if (x != 0) {
                float x0 = xOffset * xMul;
                float y0 = r * yMul;
                float x1 = (xOffset + x) * xMul;
                float y1 = y * yMul;
                fill(graphics, sX + x0, sY + y0, sX + x1, sY + y1, z, color);
            }
            xOffset += x;
        }
    }

    private static void renderArcDotted(GuiGraphics graphics, float sX, float sY, float z, float scaling, float thickness, ArcOrient sideX, ArcOrient sideY, ColorProvider color, int[] xs) {
        float xMul = (sideX == ArcOrient.NEGATIVE ? -1 : 1) / scaling;
        float yMul = (sideY == ArcOrient.NEGATIVE ? -1 : 1) / scaling;
        boolean draw = true;
        int xOffset = 0;
        for (int y = 0; y < xs.length; y++) {
            int xDif = xs[y];
            for (int x = 0; x == 0 || x < xDif; x++) {
                if (draw) {
                    int aX = xOffset + x - (xDif == 0 ? 1 : 0);
                    float x0 = aX * xMul;
                    float y0 = y * yMul;
                    float x1 = (aX + 1) * xMul * thickness;
                    float y1 = (y + 1) * yMul * thickness;
                    fill(graphics, sX + x0, sY + y0, sX + x1, sY + y1, z, color);
                }
                draw = !draw;
            }
            xOffset += xDif;
        }
    }

    public static void fill(GuiGraphics graphics, float x0, float y0, float x1, float y1, float z, ColorProvider color) {
        VertexConsumer consumer = graphics.bufferSource().getBuffer(RenderType.gui());
        Matrix4f mat = graphics.pose().last().pose();
        float minX = Math.min(x0, x1);
        float minY = Math.min(y0, y1);
        float maxX = Math.max(x0, x1);
        float maxY = Math.max(y0, y1);

        consumer.vertex(mat, maxX, maxY, z).color(color.get(maxX, maxY)).endVertex();
        consumer.vertex(mat, maxX, minY, z).color(color.get(maxX, minY)).endVertex();
        consumer.vertex(mat, minX, minY, z).color(color.get(minX, minY)).endVertex();
        consumer.vertex(mat, minX, maxY, z).color(color.get(minX, maxY)).endVertex();
    }

    public static void fill(GuiGraphics graphics, float x0, float y0, float x1, float y1, float z, int color) {
        VertexConsumer consumer = graphics.bufferSource().getBuffer(RenderType.gui());
        Matrix4f mat = graphics.pose().last().pose();
        float minX = Math.min(x0, x1);
        float minY = Math.min(y0, y1);
        float maxX = Math.max(x0, x1);
        float maxY = Math.max(y0, y1);

        float a = (float) FastColor.ARGB32.alpha(color) / 255.0F;
        float r = (float) FastColor.ARGB32.red(color) / 255.0F;
        float g = (float) FastColor.ARGB32.green(color) / 255.0F;
        float b = (float) FastColor.ARGB32.blue(color) / 255.0F;

        consumer.vertex(mat, maxX, maxY, z).color(r, g, b, a).endVertex();
        consumer.vertex(mat, maxX, minY, z).color(r, g, b, a).endVertex();
        consumer.vertex(mat, minX, minY, z).color(r, g, b, a).endVertex();
        consumer.vertex(mat, minX, maxY, z).color(r, g, b, a).endVertex();
    }

    public static float getWindowScaling() {
        return (float) Minecraft.getInstance().getWindow().getGuiScale();
    }

    public static Font getFont() {
        return Minecraft.getInstance().font;
    }

    public static void renderRoundBorder(GuiGraphics graphics, int color, float x0, float y0, float x1, float y1, float rad, float scaling, float thickness) {
        renderRoundBorder(graphics, new StaticColor(color), x0, y0, x1, y1, rad, scaling, thickness);
    }

    public static void renderRoundBorder(GuiGraphics graphics, ColorProvider color, float x0, float y0, float x1, float y1, float rad, float scaling, float thickness) {
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

    public static void renderRoundBg(GuiGraphics graphics, ColorProvider color, float x0, float y0, float x1, float y1, float rad, float scaling) {
        fill(graphics, x0 + rad, y0, x1 - rad, y1, 0, color); // Middle
        fill(graphics, x0, y0 + rad, x0 + rad, y1 - rad, 0, color); // Left center
        fill(graphics, x1 - rad, y0 + rad, x1, y1 - rad, 0, color); // Right center
        renderArcFilled(graphics, x0 + rad, y0, 0, rad, scaling, ArcOrient.NEGATIVE, ArcOrient.POSITIVE, color); // Top-left corner
        renderArcFilled(graphics, x1 - rad, y0, 0, rad, scaling, ArcOrient.POSITIVE, ArcOrient.POSITIVE, color); // Top-right corner
        renderArcFilled(graphics, x0 + rad, y1, 0, rad, scaling, ArcOrient.NEGATIVE, ArcOrient.NEGATIVE, color); // Bottom-left corner
        renderArcFilled(graphics, x1 - rad, y1, 0, rad, scaling, ArcOrient.POSITIVE, ArcOrient.NEGATIVE, color); // Bottom-right corner
    }

    public static boolean renderTextTooltip(GuiGraphics graphics, FormattedCharSequence text, float textX, float textY, float textScale, float mouseX, float mouseY) {
        Style hoveredStyle = TextUtils.getHoveredStyle(getFont(), text, textX, textY, textScale, mouseX, mouseY);
        HoverEvent event = hoveredStyle.getHoverEvent();
        FormattedCharSequence tooltip = null;
        if (event != null) {
            Object value = event.getValue(event.getAction());
            if (value instanceof Component component) tooltip = component.getVisualOrderText();
        }
        if (tooltip != null) {
            renderTooltip(graphics, tooltip, mouseX, mouseY, 1);
        }
        return false;
    }

    public static boolean renderTextTooltip(GuiGraphics graphics, AbstractComponent text, float textX, float textY, float mouseX, float mouseY) {
        FWLStyle hoveredStyle = TextUtils.getHoveredStyle(getFont(), text::visit, textX, textY, mouseX, mouseY);
        AbstractComponent tooltip = hoveredStyle.getTooltip();
        if (tooltip != null) {
            renderTooltip(graphics, tooltip, mouseX, mouseY);
        }
        return false;
    }

    public static void renderTooltip(GuiGraphics graphics, AbstractComponent tooltip, float x, float y) {
        renderTooltip(graphics, tooltip::visit, x, y, 0);
    }

    public static void renderTooltip(GuiGraphics graphics, FWLCharSequence tooltip, float x, float y) {
        renderTooltip(graphics, tooltip, x, y, 0);
    }

    public static void renderTooltip(GuiGraphics graphics, FWLCharSequence tooltip, float x, float y, float z) {
        final float TEXT_OFFSET = 4;
        FWLTheme theme = FWL.fwl().currentTheme();
        float textWidth = textWidth(tooltip);
        float textHeight = textHeight(tooltip);
        BoundsDescriptor desc = new BoundsDescriptor(x, y, textWidth + (TEXT_OFFSET * 2), textHeight + (TEXT_OFFSET * 2));
        desc.setWidgetType("tooltip");
        PoseStack stack = graphics.pose();
        stack.pushPose();
        stack.translate(0, 0, z);
        theme.fillBounds(graphics, 0, desc);
        theme.renderBounds(graphics, 0, desc);
        renderText(graphics, tooltip, x + TEXT_OFFSET, y + TEXT_OFFSET, 0);
        stack.popPose();
    }

    public static void renderTooltip(GuiGraphics graphics, FormattedCharSequence tooltip, float x, float y, float textScale) {
        renderTooltip(graphics, tooltip, x, y, 1, textScale);
    }

    public static void renderTooltip(GuiGraphics graphics, FormattedCharSequence tooltip, float x, float y, float z, float textScale) {
        final float TEXT_OFFSET = 4;
        FWLTheme theme = FWL.fwl().currentTheme();
        float textWidth = textWidth(tooltip, 1);
        float textHeight = textHeight(tooltip, 1, Integer.MAX_VALUE);
        BoundsDescriptor desc = new BoundsDescriptor(x, y, textWidth + (TEXT_OFFSET * 2), textHeight + (TEXT_OFFSET * 2));
        desc.setWidgetType("tooltip");
        PoseStack stack = graphics.pose();
        stack.pushPose();
        stack.translate(0, 0, z);
        theme.fillBounds(graphics, 0, desc);
        theme.renderBounds(graphics, 0, desc);
        renderText(graphics, tooltip, x + TEXT_OFFSET, y + TEXT_OFFSET, 0, textScale, false);
        stack.popPose();
    }

    public enum ArcOrient {
        POSITIVE,
        NEGATIVE
    }

    public interface ColorProvider {
        int get(float x, float y);
    }

    public static class StaticColor implements ColorProvider {
        private final int color;

        public StaticColor(int color) {
            this.color = color;
        }

        @Override
        public int get(float x, float y) {
            return color;
        }
    }
}
