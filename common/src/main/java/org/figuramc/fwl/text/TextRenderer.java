package org.figuramc.fwl.text;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
import net.minecraft.client.renderer.MultiBufferSource;
import org.figuramc.fwl.text.providers.StyleProvider;
import org.figuramc.fwl.utils.Watch;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class TextRenderer {
    public static final int FLOAT_1 = Float.floatToRawIntBits(1f);

    private final ArrayList<GlyphData> lineGlyphs = new ArrayList<>();
    private final Font font;
    private final MultiBufferSource.BufferSource bufferSource;
    private final Font.DisplayMode layerType;
    private final Matrix4f matrix;
    private final int light;
    private final float initialX;
    private float x, y;
    private float lineHeight = 0;

    public TextRenderer(MultiBufferSource.BufferSource bufferSource, float x, float y, Font font, Matrix4f matrix, Font.DisplayMode layerType, int light) {
        this.bufferSource = bufferSource;
        this.font = font;
        this.x = initialX = x;
        this.y = y;
        this.matrix = matrix;
        this.layerType = layerType;
        this.light = light;
    }

    public boolean accept(int index, StyleProvider provider, int codepoint) {
        FWLStyle style = provider.getOrEmpty(index);
        Vector2f scale = style.getScale();
        float glyphHeight = font.lineHeight * scale.y;
        lineHeight = Math.max(lineHeight, glyphHeight);
        if (codepoint == '\n') {
            render();
        }
        else {
            FontSet set = font.getFontSet(style.getFont());
            GlyphInfo info = set.getGlyphInfo(codepoint, font.filterFishyGlyphs);
            BakedGlyph glyph = style.isObfuscated() ? set.getRandomGlyph(info) : set.getGlyph(codepoint);
            float glyphWidth = info.getAdvance(style.isBold()) * scale.x;
            lineGlyphs.add(new GlyphData(info, glyph, glyphWidth, glyphHeight, style));
        }
        return true;
    }

    private boolean renderLine() {
        if (lineGlyphs.isEmpty()) return false;
        for (GlyphData data: lineGlyphs) {
            x += renderChar(bufferSource, font, layerType, matrix, data.glyph, data.info, x, y, data.style, lineHeight, font.lineHeight, light);
        }
        return true;
    }

    public void render() {
        if (renderLine()) {
            x = initialX;
            y += lineGlyphs.isEmpty() ? font.lineHeight : lineHeight;
            lineGlyphs.clear();
            lineHeight = 0;
        }
    }

    public static float renderChar(MultiBufferSource.BufferSource source, Font renderer, Font.DisplayMode layerType, Matrix4f matrix, BakedGlyph glyph, GlyphInfo info, float x, float y, FWLStyle style, float lineHeight, int charHeight, int light) {
        final float initialX = x, initialY = y;

        float width = info.getAdvance(style.isBold());
        float height = charHeight;

        if (style.hasBackground()) {
            Vector4f backgroundColor = style.getBackgroundColor();
            float cR = backgroundColor.x;
            float cG = backgroundColor.y;
            float cB = backgroundColor.z;
            float cA = backgroundColor.w;
            renderEffect(source, renderer, layerType, matrix, initialX, initialY, initialX + width, initialY + lineHeight, cR, cG, cB, cA, light);
        }

        if (!(glyph instanceof EmptyGlyph)) {
            VertexConsumer consumer = source.getBuffer(glyph.renderType(layerType));
            Vector4f color = style.getColor();
            float r = color.x, g = color.y, b = color.z, a = color.w;
            float u0 = glyph.u0, v0 = glyph.v0, u1 = glyph.u1, v1 = glyph.v1;

            if (style.hasOffset()) {
                Vector2f offset = style.getOffset();
                x += offset.x;
                y += offset.y;
            }

            Vector2f scale;
            if (style.hasScale()) {
                scale = style.getScale();
                width *= scale.x;
                height *= scale.y;
            }
            else {
                scale = null;
            }
            y += (lineHeight - height) * style.getVerticalAlignment();

            float skewX = 0, skewY = 0;

            boolean applySkew = false;

            if (style.isItalic()) {
                skewX += scale == null ? 1 : scale.x;
                applySkew = true;
            }

            if (style.hasSkew()) {
                Vector2f skew = style.getSkew();
                skewX += skew.x;
                skewY = skew.y;
                applySkew = true;
            }


            float x1 = x + glyph.left;
            float x2 = x + glyph.right;


            float rY1 = glyph.up - 3;
            float rY2 = glyph.down - 3;

            float y1 = y + rY1;
            float y2 = y + rY2;


            if (scale != null) {
                float xDiff = x2 - x1;
                float yDiff = y2 - y1;
                x2 += xDiff * (scale.x - 1);
                y2 += yDiff * (scale.y - 1);
            }

            if (style.hasShadow()) {
                Vector4f shadowColor = style.getShadowColor();
                Vector2f shadowOffset = style.getShadowOffset();
                if (!style.hasShadowOffset() && scale != null) {
                    shadowOffset.mul(scale.x, scale.y);
                }
                float oX = shadowOffset.x, oY = shadowOffset.y;
                float cR = shadowColor.x;
                float cG = shadowColor.y;
                float cB = shadowColor.z;
                float cA = shadowColor.w;
                if (applySkew) renderCharInternal(consumer, matrix, x1 + oX, y1 + oY, x2 + oX, y2 + oY, u0, v0, u1, v1, skewX, skewY, cR, cG, cB, cA, light);
                else renderCharInternalSkewless(consumer, matrix, x1 + oX, y1 + oY, x2 + oX, y2 + oY, u0, v0, u1, v1, cR, cG, cB, cA, light);
            }

            if (style.hasOutline()) {
                Vector4f outlineColor = style.getOutlineColor();
                Vector2f outlineScale = style.getOutlineScale();
                float cR = outlineColor.x;
                float cG = outlineColor.y;
                float cB = outlineColor.z;
                float cA = outlineColor.w;
                if (applySkew) {
                    for (int oY = -1; oY <= 1; oY++) {
                        for (int oX = -1; oX <= 1; oX++) {
                            if (oY == 0 && oX == 0) continue;
                            float cX = oX * outlineScale.x;
                            float cY = oY * outlineScale.y;
                            renderCharInternal(consumer, matrix, x1 + cX, y1 + cY, x2 + cX, y2 + cY, u0, v0, u1, v1, skewX, skewY, cR, cG, cB, cA, light);
                        }
                    }
                }
                else {
                    for (int oY = -1; oY <= 1; oY++) {
                        for (int oX = -1; oX <= 1; oX++) {
                            if (oY == 0 && oX == 0) continue;
                            float cX = oX * outlineScale.x;
                            float cY = oY * outlineScale.y;
                            renderCharInternalSkewless(consumer, matrix, x1 + cX, y1 + cY, x2 + cX, y2 + cY, u0, v0, u1, v1, cR, cG, cB, cA, light);
                        }
                    }
                }
            }

            if (style.isBold()) {
                float weight = scale == null ? info.getBoldOffset() : (info.getBoldOffset() * scale.x);
                if (applySkew) renderCharInternal(consumer, matrix, x1 + weight, y1, x2 + weight, y2, u0, v0, u1, v1, skewX, skewY, r, g, b, a, light);
                else renderCharInternalSkewless(consumer, matrix, x1 + weight, y1, x2 + weight, y2, u0, v0, u1, v1, r, g, b, a, light);
            }

            if (applySkew) renderCharInternal(consumer, matrix, x1, y1, x2, y2, u0, v0, u1, v1, skewX, skewY, r, g, b, a, light);
            else renderCharInternalSkewless(consumer, matrix, x1, y1, x2, y2, u0, v0, u1, v1, r, g, b, a, light);
        }

        if (style.hasStrikethrough()) {
            Vector4f strikethroughColor = style.getStrikethroughColor();
            float cX2 = initialX + width;
            float cY1 = initialY + (lineHeight / 2) - 1;
            float cY2 = cY1 + 1;
            float cR = strikethroughColor.x;
            float cG = strikethroughColor.y;
            float cB = strikethroughColor.z;
            float cA = strikethroughColor.w;
            renderEffect(source, renderer, layerType, matrix, initialX, cY1, cX2, cY2, cR, cG, cB, cA, light);
        }

        if (style.hasUnderline()) {
            Vector4f underlineColor = style.getUnderlineColor();
            float cX2 = initialX + width;
            float cY2 = initialY + (lineHeight);
            float cY1 = cY2 - 1;
            float cR = underlineColor.x;
            float cG = underlineColor.y;
            float cB = underlineColor.z;
            float cA = underlineColor.w;
            renderEffect(source, renderer, layerType, matrix, initialX, cY1, cX2, cY2, cR, cG, cB, cA, light);
        }
        return width;
    }

    private static void renderCharInternal(VertexConsumer consumer, Matrix4f matrix, float x1, float y1, float x2, float y2, float u0, float v0, float u1, float v1, float skewX, float skewY, float r, float g, float b, float a, int light) {
        consumer.vertex(matrix, x1 + skewX, y1 - skewY, 0).color(r, g, b, a).uv(u0, v0).uv2(light).endVertex();
        consumer.vertex(matrix, x1 - skewX, y2 - skewY, 0).color(r, g, b, a).uv(u0, v1).uv2(light).endVertex();
        consumer.vertex(matrix, x2 - skewX, y2 + skewY, 0).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
        consumer.vertex(matrix, x2 + skewX, y1 + skewY, 0).color(r, g, b, a).uv(u1, v0).uv2(light).endVertex();
    }

    private static void renderCharInternalSkewless(VertexConsumer consumer, Matrix4f matrix, float x1, float y1, float x2, float y2, float u0, float v0, float u1, float v1, float r, float g, float b, float a, int light) {
        consumer.vertex(matrix, x1, y1, 0).color(r, g, b, a).uv(u0, v0).uv2(light).endVertex();
        consumer.vertex(matrix, x1, y2, 0).color(r, g, b, a).uv(u0, v1).uv2(light).endVertex();
        consumer.vertex(matrix, x2, y2, 0).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
        consumer.vertex(matrix, x2, y1, 0).color(r, g, b, a).uv(u1, v0).uv2(light).endVertex();
    }

    private static void renderEffect(MultiBufferSource.BufferSource source, Font renderer, Font.DisplayMode layerType, Matrix4f matrix, float x1, float y1, float x2, float y2, float r, float g, float b, float a, int light) {
        FontSet fontSet = renderer.getFontSet(FWLStyle.DEFAULT_FONT);
        BakedGlyph whiteGlyph = fontSet.whiteGlyph();

        VertexConsumer consumer = source.getBuffer(whiteGlyph.renderType(layerType));

        consumer.vertex(matrix, x1, y1, 0).color(r, g, b, a).uv(whiteGlyph.u0, whiteGlyph.v0).uv2(light).endVertex();
        consumer.vertex(matrix, x1, y2, 0).color(r, g, b, a).uv(whiteGlyph.u0, whiteGlyph.v1).uv2(light).endVertex();
        consumer.vertex(matrix, x2, y2, 0).color(r, g, b, a).uv(whiteGlyph.u1, whiteGlyph.v1).uv2(light).endVertex();
        consumer.vertex(matrix, x2, y1, 0).color(r, g, b, a).uv(whiteGlyph.u1, whiteGlyph.v0).uv2(light).endVertex();
    }

    private record GlyphData(GlyphInfo info, BakedGlyph glyph, float width, float height, FWLStyle style) {}
}
