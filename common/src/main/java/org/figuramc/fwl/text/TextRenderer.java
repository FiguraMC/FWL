package org.figuramc.fwl.text;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
import org.figuramc.fwl.text.providers.StyleProvider;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class TextRenderer {
    private final ArrayList<GlyphData> lineGlyphs = new ArrayList<>();
    private final Font font;
    private final GuiGraphics graphics;
    private final Font.DisplayMode layerType;
    private final Matrix4f matrix;
    private final int light;
    private final float initialX;
    private float x, y;
    private float lineHeight = 0;

    public TextRenderer(GuiGraphics graphics, float x, float y, Font font, Matrix4f matrix, Font.DisplayMode layerType, int light) {
        this.graphics = graphics;
        this.font = font;
        this.x = initialX = x;
        this.y = y;
        this.matrix = matrix;
        this.layerType = layerType;
        this.light = light;
    }

    public boolean accept(int index, StyleProvider provider, int codepoint) {
        if (codepoint == '\n') {
            render();
        }
        else {
            FWLStyle style = provider.get(index, 0);
            FontSet set = font.getFontSet(style.getFont());
            GlyphInfo info = set.getGlyphInfo(codepoint, font.filterFishyGlyphs);
            BakedGlyph glyph = style.isObfuscated() ? set.getRandomGlyph(info) : set.getGlyph(codepoint);
            Vector2f scale = style.getScale();
            float glyphWidth = info.getAdvance(style.isBold()) * scale.x;
            float glyphHeight = font.lineHeight * scale.y;
            lineHeight = Math.max(lineHeight, glyphHeight);
            lineGlyphs.add(new GlyphData(info, glyph, glyphWidth, glyphHeight, style));
        }
        return true;
    }

    private boolean renderLine() {
        if (lineGlyphs.isEmpty()) return false;
        for (GlyphData data: lineGlyphs) {
            x += renderChar(graphics, layerType, matrix, data.glyph, data.info, x, y, data.style, lineHeight, font.lineHeight, light);
        }
        return true;
    }

    public void render() {
        if (renderLine()) {
            x = initialX;
            y += lineGlyphs.isEmpty() ? font.lineHeight : lineHeight;
            lineGlyphs.clear();
            lineHeight = 0;
            graphics.flushIfUnmanaged();
        }
    }

    public static float renderChar(GuiGraphics graphics, Font.DisplayMode layerType, Matrix4f matrix, BakedGlyph glyph, GlyphInfo info, float x, float y, FWLStyle style, float lineHeight, int charHeight, int light) {
        final float initialX = x, initialY = y;

        float width = info.getAdvance(style.isBold());
        float height = charHeight;

        if (!(glyph instanceof EmptyGlyph)) {
            VertexConsumer consumer = graphics.bufferSource().getBuffer(glyph.renderType(layerType));
            Vector4f color = style.getColor();
            float r = color.x, g = color.y, b = color.z, a = color.w;
            float u0 = glyph.u0, v0 = glyph.v0, u1 = glyph.u1, v1 = glyph.v1;

            if (style.hasOffset()) {
                Vector2f offset = style.getOffset();
                x += offset.x;
                y += offset.y;
            }

            Vector2f scale = style.getScale();

            width *= scale.x;
            height *= scale.y;

            float alignment = style.getVerticalAlignment();

            y += (lineHeight - height) * alignment;

            float skewX = 0, skewY = 0;
            if (style.isItalic()) {
                skewX += scale.x;
            }

            if (style.hasSkew()) {
                Vector2f skew = style.getSkew();
                skewX += skew.x;
                skewY = skew.y;
            }


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

            if (style.hasBackground()) {
                // TODO render background
            }

            if (style.hasOutline()) {
                // TODO render outline
            }
            else if (style.hasShadow()) {
                // TODO render shadow
            }
            else if (style.isBold()) {
                // TODO render bold
            }

            renderCharInternal(consumer, matrix, x1, y1, x2, y2, u0, v0, u1, v1, skewX, skewY, r, g, b, a, light);
        }
        return width;
    }

    private static void renderCharInternal(VertexConsumer consumer, Matrix4f matrix, float x1, float y1, float x2, float y2, float u0, float v0, float u1, float v1, float skewX, float skewY, float r, float g, float b, float a, int light) {
        consumer.vertex(matrix, x1 + skewX, y1 - skewY, 0).color(r, g, b, a).uv(u0, v0).uv2(light).endVertex();
        consumer.vertex(matrix, x1 - skewX, y2 - skewY, 0).color(r, g, b, a).uv(u0, v1).uv2(light).endVertex();
        consumer.vertex(matrix, x2 - skewX, y2 + skewY, 0).color(r, g, b, a).uv(u1, v1).uv2(light).endVertex();
        consumer.vertex(matrix, x2 + skewX, y1 + skewY, 0).color(r, g, b, a).uv(u1, v0).uv2(light).endVertex();
    }

    private record GlyphData(GlyphInfo info, BakedGlyph glyph, float width, float height, FWLStyle style) {}
}
