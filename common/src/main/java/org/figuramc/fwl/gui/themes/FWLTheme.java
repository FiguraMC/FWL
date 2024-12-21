package org.figuramc.fwl.gui.themes;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.figuramc.fwl.gui.widgets.descriptors.*;
import org.figuramc.fwl.gui.widgets.descriptors.input.TextInputDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.misc.ContextMenuDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.misc.IconDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.misc.SliderDescriptor;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.figuramc.fwl.gui.widgets.descriptors.button.CheckboxDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.button.RadioButtonDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.button.ButtonDescriptor;

public abstract class FWLTheme {
    public abstract void renderButton(GuiGraphics graphics, float delta, ButtonDescriptor button);
    public abstract void renderCheckbox(GuiGraphics graphics, float delta, CheckboxDescriptor checkbox);
    public abstract void renderRadioButton(GuiGraphics graphics, float delta, RadioButtonDescriptor button);
    public abstract void renderScrollBar(GuiGraphics graphics, float delta, ScrollBarDescriptor scrollBar);
    public abstract void renderSlider(GuiGraphics graphics, float delta, SliderDescriptor slider);
    public abstract void renderTextInput(GuiGraphics graphics, float delta, TextInputDescriptor textInput);
    public abstract void renderContextMenu(GuiGraphics graphics, float delta, ContextMenuDescriptor menu);
    public abstract void renderBounds(GuiGraphics graphics, float delta, BoundsDescriptor descriptor);
    public abstract void fillBounds(GuiGraphics graphics, float delta, BoundsDescriptor descriptor);
    public abstract void renderIcon(GuiGraphics graphics, IconDescriptor icon);

    public abstract void applyPreset(@Nullable JsonObject preset);
    public abstract JsonObject savePreset();

    public static float renderText(GuiGraphics graphics, Component text, float x, float y, float z, float scale, int color, boolean shadow) {
        PoseStack stack = graphics.pose();
        Font font = Minecraft.getInstance().font;
        stack.pushPose();
        stack.scale(scale, scale, scale);
        stack.translate(x, y, z);
        int w = graphics.drawString(font, text, 0, 0, color, shadow);
        stack.popPose();
        return w * scale;
    }

    public float textWidth(GuiGraphics graphics, Component text, float scale) {
        Font font = Minecraft.getInstance().font;
        return font.width(text) * scale;
    }

    public float textHeight(GuiGraphics graphics, Component text, float scale, float maxWidth) {
        Font font = Minecraft.getInstance().font;
        return font.split(text, (int) (maxWidth / scale)).size() * font.lineHeight * scale;
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

    protected void renderRoundBorder(GuiGraphics graphics, int color, float x0, float y0, float x1, float y1, float rad, float scaling, float thickness) {
        renderRoundBorder(graphics, new StaticColor(color), x0, y0, x1, y1, rad, scaling, thickness);
    }

    public void renderRoundBorder(GuiGraphics graphics, ColorProvider color, float x0, float y0, float x1, float y1, float rad, float scaling, float thickness) {
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

    public enum ArcOrient {
        POSITIVE,
        NEGATIVE
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

    public void renderRoundBg(GuiGraphics graphics, ColorProvider color, float x0, float y0, float x1, float y1, float rad, float scaling) {
        fill(graphics, x0 + rad, y0, x1 - rad, y1, 0, color); // Middle
        fill(graphics, x0, y0 + rad, x0 + rad, y1 - rad, 0, color); // Left center
        fill(graphics, x1 - rad, y0 + rad, x1, y1 - rad, 0, color); // Right center
        renderArcFilled(graphics, x0 + rad, y0, 0, rad, scaling, ArcOrient.NEGATIVE, ArcOrient.POSITIVE, color); // Top-left corner
        renderArcFilled(graphics, x1 - rad, y0, 0, rad, scaling, ArcOrient.POSITIVE, ArcOrient.POSITIVE, color); // Top-right corner
        renderArcFilled(graphics, x0 + rad, y1, 0, rad, scaling, ArcOrient.NEGATIVE, ArcOrient.NEGATIVE, color); // Bottom-left corner
        renderArcFilled(graphics, x1 - rad, y1, 0, rad, scaling, ArcOrient.POSITIVE, ArcOrient.NEGATIVE, color); // Bottom-right corner
    }

    public static float getWindowScaling() {
        return (float) Minecraft.getInstance().getWindow().getGuiScale();
    }

    public abstract Integer getColor(String... path);
    public Integer getColor(ResourceLocation path) {
        return getColor(path.getPath());
    }

    public int getColorOrDefault(ResourceLocation type, int fallback) {
        return getColorOrDefault(fallback, type.getPath());
    }

    public int getColorOrDefault(int fallback, String... path) {
        Integer color = getColor(path);
        return color != null ? color : fallback;
    }

    public interface ColorProvider {
        int get(float x, float y);
    }

    public interface DescriptorRenderer<T extends FWLDescriptor> {
        void render(GuiGraphics graphics, float delta, T desc);
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
