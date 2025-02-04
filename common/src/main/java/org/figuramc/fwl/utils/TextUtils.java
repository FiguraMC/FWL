package org.figuramc.fwl.utils;

import com.mojang.blaze3d.font.GlyphInfo;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class TextUtils {
    public static int findCursorJumpAfter(String str, int ind) {
        int len = str.length();
        if (ind >= len) return len;
        int i = ind + 1;
        for (; i < len; i++) {
            char c = str.charAt(i);
            if (Character.isWhitespace(c)) return i;
        }
        return i;
    }

    public static int findCursorJumpBefore(String str, int ind) {
        if (ind <= 0) return 0;
        ind = Math.min(str.length(), ind);
        int i = ind - 1;
        for (; i >= 0; i--) {
            char c = str.charAt(i);
            if (Character.isWhitespace(c)) return i;
        }
        return i;
    }

    public static String themeToTranslationString(ResourceLocation theme) {
        return "%s.theme.%s".formatted(theme.getNamespace(), theme.getPath().replace('/', '.'));
    }

    public static FormattedCharSequence substr(FormattedCharSequence sequence, int beginIndex, int endIndex) {
        return (sink) -> sequence.accept((index, style, codepoint) -> {
            if (index >= beginIndex) {
                if (index < endIndex) return sink.accept(index, style, codepoint);
                else return false;
            }
            else return true;
        });
    }

    public static Style getHoveredStyle(Font font, FormattedCharSequence sequence, float textX, float textY, float textScale, float mouseX, float mouseY) {
        float relativeX = mouseX - textX;
        float relativeY = mouseY - textY;
        AtomicReference<Float> currentX = new AtomicReference<>((float) 0);
        AtomicReference<Float> currentY = new AtomicReference<>((float) 0);
        AtomicReference<Style> returnStyle = new AtomicReference<>(Style.EMPTY);
        sequence.accept((index, style, codepoint) -> {
            if (codepoint == '\n') {
                currentX.set(0f);
                currentY.updateAndGet(v -> v + font.lineHeight);
            }
            boolean bold = style.isBold();
            FontSet fontSet = font.getFontSet(style.getFont());
            GlyphInfo info = fontSet.getGlyphInfo(codepoint, font.filterFishyGlyphs);
            float sizeX = info.getAdvance(bold);
            float sizeY = font.lineHeight;
            Rectangle rect = new Rectangle(currentX.get(), currentY.get(), sizeX, sizeY);
            if (rect.pointIn(relativeX, relativeY)) {
                returnStyle.set(style);
                return false;
            }
            currentX.updateAndGet(v -> v + sizeX);
            return true;
        });
        return returnStyle.get();
    }

    public static List<FormattedCharSequence> splitByNewLine(FormattedCharSequence sequence) {
        ArrayList<FormattedCharSequence> lines = new ArrayList<>();
        AtomicInteger startIndex = new AtomicInteger(0);
        AtomicInteger endIndex = new AtomicInteger(0);
        sequence.accept((index, style, codepoint) -> {
            endIndex.set(index);
            if (codepoint == '\n') {
                lines.add(substr(sequence, startIndex.get(), endIndex.get()));
                startIndex.set(endIndex.get() + 1);
                return true;
            }
            return true;
        });
        lines.add(substr(sequence, startIndex.get(), endIndex.get() + 1));
        return lines;
    }

    public static int lines(FormattedCharSequence text) {
        AtomicInteger lines = new AtomicInteger(1);
        text.accept((index, style, codepoint) -> {
            if (codepoint == '\n') lines.incrementAndGet();
            return true;
        });
        return lines.get();
    }
}
