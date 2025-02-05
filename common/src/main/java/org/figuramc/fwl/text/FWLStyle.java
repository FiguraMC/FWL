package org.figuramc.fwl.text;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.Objects;

public class FWLStyle {
    public static final FWLStyle EMPTY = new FWLStyle();
    private @Nullable Boolean bold, italic, obfuscated, blended;
    private @Nullable Vector4f color, backgroundColor, shadowColor, strikethroughColor, underlineColor, outlineColor;
    private @Nullable Vector2f scale, outlineScale, skew, offset, shadowOffset;
    private @Nullable Float verticalAlignment;
    private @Nullable ResourceLocation font;

    public FWLStyle() {

    }

    public FWLStyle(@Nullable Boolean bold, @Nullable Boolean italic, @Nullable Boolean obfuscated, @Nullable Boolean blended, @Nullable Vector4f color, @Nullable Vector4f backgroundColor, @Nullable Vector4f shadowColor, @Nullable Vector4f strikethroughColor, @Nullable Vector4f underlineColor, @Nullable Vector4f outlineColor, @Nullable Vector2f scale, @Nullable Vector2f outlineScale, @Nullable Vector2f skew, @Nullable Vector2f offset, @Nullable Vector2f shadowOffset, @Nullable Float verticalAlignment, @Nullable ResourceLocation font) {
        this.bold = bold;
        this.italic = italic;
        this.obfuscated = obfuscated;
        this.blended = blended;
        this.color = color;
        this.backgroundColor = backgroundColor;
        this.shadowColor = shadowColor;
        this.strikethroughColor = strikethroughColor;
        this.underlineColor = underlineColor;
        this.outlineColor = outlineColor;
        this.scale = scale;
        this.outlineScale = outlineScale;
        this.skew = skew;
        this.offset = offset;
        this.shadowOffset = shadowOffset;
        this.verticalAlignment = verticalAlignment;
        this.font = font;
    }

    public FWLStyle withBold(@Nullable Boolean bold) {
        FWLStyle inst = this.clone();
        inst.bold = bold;
        return inst;
    }

    public FWLStyle withItalic(@Nullable Boolean italic) {
        FWLStyle inst = this.clone();
        inst.italic = italic;
        return inst;
    }

    public FWLStyle withObfuscated(@Nullable Boolean obfuscated) {
        FWLStyle inst = this.clone();
        inst.obfuscated = obfuscated;
        return inst;
    }

    public FWLStyle withBlended(@Nullable Boolean blended) {
        FWLStyle inst = this.clone();
        inst.blended = blended;
        return inst;
    }

    public FWLStyle withColor(@Nullable Vector4f color) {
        FWLStyle inst = this.clone();
        inst.color = color;
        return inst;
    }

    public FWLStyle withBackgroundColor(@Nullable Vector4f backgroundColor) {
        FWLStyle inst = this.clone();
        inst.backgroundColor = backgroundColor;
        return inst;
    }

    public FWLStyle withShadowColor(@Nullable Vector4f shadowColor) {
        FWLStyle inst = this.clone();
        inst.shadowColor = shadowColor;
        return inst;
    }

    public FWLStyle withStrikethroughColor(@Nullable Vector4f strikethroughColor) {
        FWLStyle inst = this.clone();
        inst.strikethroughColor = strikethroughColor;
        return inst;
    }

    public FWLStyle withUnderlineColor(@Nullable Vector4f underlineColor) {
        FWLStyle inst = this.clone();
        inst.underlineColor = underlineColor;
        return inst;
    }

    public FWLStyle withOutlineColor(@Nullable Vector4f outlineColor) {
        FWLStyle inst = this.clone();
        inst.outlineColor = outlineColor;
        return inst;
    }

    public FWLStyle withScale(@Nullable Vector2f scale) {
        FWLStyle inst = this.clone();
        inst.scale = scale;
        return inst;
    }

    public FWLStyle withOutlineScale(@Nullable Vector2f outlineScale) {
        FWLStyle inst = this.clone();
        inst.outlineScale = outlineScale;
        return inst;
    }

    public FWLStyle withSkew(@Nullable Vector2f skew) {
        FWLStyle inst = this.clone();
        inst.skew = skew;
        return inst;
    }

    public FWLStyle withOffset(@Nullable Vector2f offset) {
        FWLStyle inst = this.clone();
        inst.offset = offset;
        return inst;
    }

    public FWLStyle withShadowOffset(@Nullable Vector2f shadowOffset) {
        FWLStyle inst = this.clone();
        inst.shadowOffset = shadowOffset;
        return inst;
    }

    public FWLStyle withVerticalAlignment(@Nullable Float verticalAlignment) {
        FWLStyle inst = this.clone();
        inst.verticalAlignment = verticalAlignment;
        return inst;
    }

    public FWLStyle withFont(@Nullable ResourceLocation font) {
        FWLStyle inst = this.clone();
        inst.font = font;
        return inst;
    }

    public boolean isBold() {
        return bold != null ? bold : false;
    }

    public boolean isItalic() {
        return italic != null ? italic : false;
    }

    public boolean isObfuscated() {
        return obfuscated != null ? obfuscated : false;
    }

    public boolean isBlended() {
        return blended != null ? blended : false;
    }

    public Vector4f getColor() {
        return color == null ? new Vector4f(1f) : new Vector4f(color);
    }

    public boolean hasBackground() {
        return backgroundColor != null;
    }

    public Vector4f getBackgroundColor() {
        return backgroundColor == null ? new Vector4f(0f) : new Vector4f(backgroundColor);
    }

    public boolean hasShadow() {
        return shadowColor != null;
    }

    public Vector4f getShadowColor() {
        return shadowColor == null ? new Vector4f(0f, 0f, 0f, 0.25f) : new Vector4f(shadowColor);
    }

    public boolean hasStrikethrough() {
        return strikethroughColor != null;
    }

    public Vector4f getStrikethroughColor() {
        return strikethroughColor == null ? new Vector4f(0f) : new Vector4f(strikethroughColor);
    }

    public boolean hasUnderline() {
        return underlineColor != null;
    }

    public Vector4f getUnderlineColor() {
        return underlineColor == null ? new Vector4f(0f) : new Vector4f(underlineColor);
    }

    public Vector2f getScale() {
        return scale == null ? new Vector2f(1f) : new Vector2f(scale);
    }

    public Vector2f getOutlineScale() {
        return outlineScale == null ? new Vector2f(1f) : new Vector2f(outlineScale);
    }

    public Vector2f getSkew() {
        return skew == null ? new Vector2f(0f) : new Vector2f(skew);
    }

    public Vector2f getOffset() {
        return offset == null ? new Vector2f(0f) : new Vector2f(offset);
    }

    public Vector2f getShadowOffset() {
        return shadowOffset == null ? new Vector2f(1f) : new Vector2f(shadowOffset);
    }

    public float getVerticalAlignment() {
        return verticalAlignment == null ? 1f : verticalAlignment;
    }

    public ResourceLocation getFont() {
        return font == null ? new ResourceLocation("minecraft", "default") : font;
    }

    public static FWLStyle merge(FWLStyle a, FWLStyle b) {
        return new FWLStyle(
                b.bold == null ? a.bold : b.bold,
                b.italic == null ? a.italic : b.italic,
                b.obfuscated == null ? a.obfuscated : b.obfuscated,
                b.blended == null ? a.blended : b.blended,
                b.color == null ? a.color : b.color,
                b.backgroundColor == null ? a.backgroundColor : b.backgroundColor,
                b.shadowColor == null ? a.shadowColor : b.shadowColor,
                b.strikethroughColor == null ? a.strikethroughColor : b.strikethroughColor,
                b.underlineColor == null ? a.underlineColor : b.underlineColor,
                b.outlineColor == null ? a.outlineColor : b.outlineColor,
                b.scale == null ? a.scale : b.scale,
                b.outlineScale == null ? a.outlineScale : b.outlineScale,
                b.skew == null ? a.skew : b.skew,
                b.offset == null ? a.offset : b.offset,
                b.shadowOffset == null ? a.shadowOffset : b.shadowOffset,
                b.verticalAlignment == null ? a.verticalAlignment : b.verticalAlignment,
                b.font == null ? a.font : b.font
        );
    }

    public FWLStyle applyFrom(FWLStyle other) {
        return merge(this, other);
    }

    public FWLStyle applyTo(FWLStyle parent) {
        return merge(parent, this);
    }

    public FWLStyle clone() {
        return new FWLStyle(bold, italic, obfuscated, blended, color, backgroundColor, shadowColor, strikethroughColor, underlineColor, outlineColor, scale, outlineScale, skew, offset, shadowOffset, verticalAlignment, font);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FWLStyle fwlStyle)) return false;
        return Objects.equals(bold, fwlStyle.bold) && Objects.equals(italic, fwlStyle.italic) && Objects.equals(obfuscated, fwlStyle.obfuscated) && Objects.equals(blended, fwlStyle.blended) && Objects.equals(color, fwlStyle.color) && Objects.equals(backgroundColor, fwlStyle.backgroundColor) && Objects.equals(shadowColor, fwlStyle.shadowColor) && Objects.equals(strikethroughColor, fwlStyle.strikethroughColor) && Objects.equals(underlineColor, fwlStyle.underlineColor) && Objects.equals(outlineColor, fwlStyle.outlineColor) && Objects.equals(scale, fwlStyle.scale) && Objects.equals(outlineScale, fwlStyle.outlineScale) && Objects.equals(skew, fwlStyle.skew) && Objects.equals(offset, fwlStyle.offset) && Objects.equals(shadowOffset, fwlStyle.shadowOffset) && Objects.equals(verticalAlignment, fwlStyle.verticalAlignment) && Objects.equals(font, fwlStyle.font);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bold, italic, obfuscated, blended, color, backgroundColor, shadowColor, strikethroughColor, underlineColor, outlineColor, scale, outlineScale, skew, offset, shadowOffset, verticalAlignment, font);
    }
}
