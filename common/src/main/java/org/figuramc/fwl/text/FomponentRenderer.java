package org.figuramc.fwl.text;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
import org.figuramc.fwl.text.style.CompiledStyle;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class FomponentRenderer implements FomponentVisitor {

    private final Font font;
    private final GuiGraphics graphics;
    private final Font.DisplayMode layerType;
    private final Matrix4f matrix;
    private final int light;
    private final float initialX;
    private float x, y;
    private float lineHeight = 0;

    private final List<GlyphData> lineGlyphs = new ArrayList<>(); // Queue of glyphs to render on this line

    public FomponentRenderer(GuiGraphics graphics, float x, float y, Font font, Matrix4f matrix, Font.DisplayMode layerType, int light) {
        this.graphics = graphics;
        this.font = font;
        this.x = initialX = x;
        this.y = y;
        this.matrix = matrix;
        this.layerType = layerType;
        this.light = light;
    }


    @Override
    public void accept(int index, CompiledStyle style, int codepoint) {
        Vector2f scale = style.getScale(index);
        float glyphHeight = font.lineHeight * scale.y;
        lineHeight = Math.max(lineHeight, glyphHeight);
        if (codepoint == '\n') {
            renderLine();
        } else {
            FontSet set = font.getFontSet(style.getFont(index));
            GlyphInfo info = set.getGlyphInfo(codepoint, font.filterFishyGlyphs);
            BakedGlyph glyph = style.isObfuscated(index) ? set.getRandomGlyph(info) : set.getGlyph(codepoint);
            float glyphWidth = info.getAdvance(style.isBold(index)) * scale.x;
            lineGlyphs.add(new GlyphData(info, glyph, glyphWidth, glyphHeight, style, index));
        }
    }

    public void renderLine() {
        boolean empty = lineGlyphs.isEmpty();
        for (GlyphData data : lineGlyphs)
            x += renderChar(data);
        x = initialX;
        y += empty ? font.lineHeight : lineHeight;
        lineGlyphs.clear();
        lineHeight = 0;
        if (!empty) graphics.flushIfUnmanaged();
    }

    private float renderChar(GlyphData data) {
        int index = data.index();
        CompiledStyle style = data.style();
        BakedGlyph glyph = data.glyph();

        float x = this.x, y = this.y;
        final float initialX = x, initialY = y;
        float u0 = glyph.u0, v0 = glyph.v0, u1 = glyph.u1, v1 = glyph.v1;

        float width = data.width;
        float height = data.height;

        Vector4f backgroundColor = style.getBackgroundColor(index);
        if (backgroundColor.w != 0)
            renderEffect(initialX, initialY, initialX + width, initialY + height, backgroundColor);

        if (!(glyph instanceof EmptyGlyph)) {
            VertexConsumer consumer = graphics.bufferSource().getBuffer(glyph.renderType(layerType));

            // Color
            Vector4f color = style.getColor(index);
            float r = color.x, g = color.y, b = color.z, a = color.w;

            // Offset
            Vector2f offset = style.getOffset(index);
            x += offset.x;
            y += offset.y;

            // Scale
            Vector2f scale = style.getScale(index);
//            width *= scale.x;
//            height *= scale.y;

            // Alignment
            float alignment = style.getVerticalAlignment(index);
            y += (lineHeight - height) * alignment;

            // Skew
            float skewX = 0, skewY = 0;
            if (style.isItalic(index))
                skewX += scale.x;
            Vector2f skew = style.getSkew(index);
            skewX += skew.x;
            skewY += skew.y;

            // Rendering vars
            float x1 = x + glyph.left;
            float x2 = x + glyph.right;
            float xDiff = x2 - x1;
            float rY1 = glyph.up - 3;
            float rY2 = glyph.down - 3;
            float y1 = y + rY1;
            float y2 = y + rY2;
            float yDiff = y2 - y1;
            x2 += xDiff * (scale.x - 1);
            y2 += yDiff * (scale.y - 1);

            // Shadow
            Vector4f shadowColor = style.getShadowColor(index);
            if (shadowColor.w != 0) {
                Vector2f shadowOffset = style.getShadowOffset(index);
                float oX = shadowOffset.x * scale.x;
                float oY = shadowOffset.y * scale.y;
                renderCharInternal(consumer, x1 + oX, y1 + oY, x2 + oX, y2 + oY, u0, v0, u1, v1, skewX, skewY, shadowColor.x, shadowColor.y, shadowColor.z, shadowColor.w, light);
            }

            // Outline
            Vector4f outlineColor = style.getOutlineColor(index);
            if (outlineColor.w != 0) {
                Vector2f outlineScale = style.getOutlineScale(index);
                float cR = outlineColor.x, cG = outlineColor.y, cB = outlineColor.z, cA = outlineColor.w;
                for (int oY = -1; oY <= 1; oY++) {
                    for (int oX = -1; oX <= 1; oX++) {
                        if (oX == 0 && oY == 0) continue;
                        float cX = oX * outlineScale.x;
                        float cY = oY * outlineScale.y;
                        renderCharInternal(consumer, x1 + cX, y1 + cY, x2 + cX, y2 + cY, u0, v0, u1, v1, skewX, skewY, cR, cG, cB, cA, light);
                    }
                }
            }

            // Bold
            if (style.isBold(index)) {
                float weight = data.info().getBoldOffset() * scale.x;
                renderCharInternal(consumer, x1 + weight, y1, x2 + weight, y2, u0, v0, u1, v1, skewX, skewY, r, g, b, a, light);
            }

            // Normal rendering
            renderCharInternal(consumer, x1, y1, x2, y2, u0, v0, u1, v1, skewX, skewY, r, g, b, a, light);
        }

        // Strikethrough
        Vector4f strikethroughColor = style.getStrikethroughColor(index);
        if (strikethroughColor.w != 0) {
            float cY1 = initialY + (lineHeight / 2) - 1;
            float cY2 = cY1 + 1;
            renderEffect(initialX, cY1, initialX + width, cY2, strikethroughColor);
        }

        // Underline
        Vector4f underlineColor = style.getUnderlineColor(index);
        if (underlineColor.w != 0) {
            float cY2 = initialY + lineHeight;
            float cY1 = cY2 - 1;
            renderEffect(initialX, cY1, initialX + width, cY2, underlineColor);
        }

        return width;
    }

    private void renderCharInternal(VertexConsumer consumer, float x1, float y1, float x2, float y2, float u0, float v0, float u1, float v1, float skewX, float skewY, float r, float g, float b, float a, int light) {
        consumer.vertex(matrix, x1 + skewX, y1 - skewY, 0).color(r, g, b, a).uv(u0, v0).uv2(light).endVertex();
        consumer.vertex(matrix, x1 - skewX, y2 - skewY, 0).color(r, g, b, a).uv(u0, v1).uv2(light).endVertex();
        consumer.vertex(matrix, x2 - skewX, y2 + skewY, 0).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
        consumer.vertex(matrix, x2 + skewX, y1 + skewY, 0).color(r, g, b, a).uv(u1, v0).uv2(light).endVertex();
    }

    private void renderEffect(float x1, float y1, float x2, float y2, Vector4f color) {
        float r = color.x, g = color.y, b = color.z, a = color.w;
        FontSet fontSet = font.getFontSet(CompiledStyle.EMPTY.getFont(-1));
        BakedGlyph whiteGlyph = fontSet.whiteGlyph();
        VertexConsumer consumer = graphics.bufferSource().getBuffer(whiteGlyph.renderType(layerType));
        consumer.vertex(matrix, x1, y1, 0).color(r, g, b, a).uv(whiteGlyph.u0, whiteGlyph.v0).uv2(light).endVertex();
        consumer.vertex(matrix, x1, y2, 0).color(r, g, b, a).uv(whiteGlyph.u0, whiteGlyph.v1).uv2(light).endVertex();
        consumer.vertex(matrix, x2, y2, 0).color(r, g, b, a).uv(whiteGlyph.u1, whiteGlyph.v1).uv2(light).endVertex();
        consumer.vertex(matrix, x2, y1, 0).color(r, g, b, a).uv(whiteGlyph.u1, whiteGlyph.v0).uv2(light).endVertex();
    }

    private record GlyphData(GlyphInfo info, BakedGlyph glyph, float width, float height, CompiledStyle style, int index) {}

}
