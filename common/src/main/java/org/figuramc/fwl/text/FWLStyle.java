package org.figuramc.fwl.text;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.figuramc.fwl.text.effects.Applier;
import org.figuramc.fwl.text.providers.headless.ProviderBuilder;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.Objects;

public class FWLStyle {
    public static final ResourceLocation DEFAULT_FONT = new ResourceLocation("minecraft", "default");
    public static final FWLStyle EMPTY = new FWLStyle();
    private @Nullable Boolean bold, italic, obfuscated;
    private @Nullable Vector4f color, backgroundColor, shadowColor, strikethroughColor, underlineColor, outlineColor;
    private @Nullable Vector2f scale, outlineScale, skew, offset, shadowOffset;
    private @Nullable Float verticalAlignment;
    private @Nullable ResourceLocation font;

    public static final Property<Boolean>
            BOLD = property(FWLStyle::isBold, FWLStyle::withBold, Boolean.class),
            ITALIC = property(FWLStyle::isItalic, FWLStyle::withItalic, Boolean.class),
            OBFUSCATED = property(FWLStyle::isObfuscated, FWLStyle::withObfuscated, Boolean.class);

    public static final Property<Vector4f>
            COLOR = property(FWLStyle::getColor, FWLStyle::withColor, Vector4f.class),
            BACKGROUND_COLOR = property(FWLStyle::getBackgroundColor, FWLStyle::withBackgroundColor, Vector4f.class),
            SHADOW_COLOR = property(FWLStyle::getShadowColor, FWLStyle::withShadowColor, Vector4f.class),
            STRIKETHROUGH_COLOR = property(FWLStyle::getStrikethroughColor, FWLStyle::withStrikethroughColor, Vector4f.class),
            UNDERLINE_COLOR = property(FWLStyle::getUnderlineColor, FWLStyle::withUnderlineColor, Vector4f.class),
            OUTLINE_COLOR = property(FWLStyle::getOutlineColor, FWLStyle::withOutlineColor, Vector4f.class);

    public static final Property<Vector2f>
            SCALE = property(FWLStyle::getScale, FWLStyle::withScale, Vector2f.class),
            OUTLINE_SCALE = property(FWLStyle::getOutlineScale, FWLStyle::withOutlineScale, Vector2f.class),
            SKEW = property(FWLStyle::getSkew, FWLStyle::withSkew, Vector2f.class),
            OFFSET = property(FWLStyle::getOffset, FWLStyle::withOffset, Vector2f.class),
            SHADOW_OFFSET = property(FWLStyle::getShadowOffset, FWLStyle::withOffset, Vector2f.class);

    public static final Property<Float> VERTICAL_ALIGNMENT = property(FWLStyle::getVerticalAlignment, FWLStyle::withVerticalAlignment, Float.class);

    public FWLStyle() {

    }

    public FWLStyle(@Nullable Boolean bold, @Nullable Boolean italic, @Nullable Boolean obfuscated, @Nullable Vector4f color, @Nullable Vector4f backgroundColor, @Nullable Vector4f shadowColor, @Nullable Vector4f strikethroughColor, @Nullable Vector4f underlineColor, @Nullable Vector4f outlineColor, @Nullable Vector2f scale, @Nullable Vector2f outlineScale, @Nullable Vector2f skew, @Nullable Vector2f offset, @Nullable Vector2f shadowOffset, @Nullable Float verticalAlignment, @Nullable ResourceLocation font) {
        this.bold = bold;
        this.italic = italic;
        this.obfuscated = obfuscated;
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

    public FWLStyle withColor(@Nullable Vector4f color) {
        FWLStyle inst = this.clone();
        inst.color = color;
        return inst;
    }

    public FWLStyle withColor(float r, float g, float b, float a) {
        return withColor(new Vector4f(r, g, b, a));
    }

    public FWLStyle withColor(int argb) {
        return withColor(argbToColorVec4(argb));
    }

    public FWLStyle withBackgroundColor(@Nullable Vector4f backgroundColor) {
        FWLStyle inst = this.clone();
        inst.backgroundColor = backgroundColor;
        return inst;
    }

    public FWLStyle withBackgroundColor(float r, float g, float b, float a) {
        return withBackgroundColor(new Vector4f(r, g, b, a));
    }

    public FWLStyle withBackgroundColor(int argb) {
        return withBackgroundColor(argbToColorVec4(argb));
    }

    public FWLStyle withShadowColor(@Nullable Vector4f shadowColor) {
        FWLStyle inst = this.clone();
        inst.shadowColor = shadowColor;
        return inst;
    }

    public FWLStyle withShadowColor(float r, float g, float b, float a) {
        return withShadowColor(new Vector4f(r, g, b, a));
    }

    public FWLStyle withShadowColor(int argb) {
        return withShadowColor(argbToColorVec4(argb));
    }

    public FWLStyle withStrikethroughColor(@Nullable Vector4f strikethroughColor) {
        FWLStyle inst = this.clone();
        inst.strikethroughColor = strikethroughColor;
        return inst;
    }

    public FWLStyle withStrikethroughColor(float r, float g, float b, float a) {
        return withStrikethroughColor(new Vector4f(r, g, b, a));
    }

    public FWLStyle withStrikethroughColor(int argb) {
        return withStrikethroughColor(argbToColorVec4(argb));
    }

    public FWLStyle withUnderlineColor(@Nullable Vector4f underlineColor) {
        FWLStyle inst = this.clone();
        inst.underlineColor = underlineColor;
        return inst;
    }

    public FWLStyle withUnderlineColor(float r, float g, float b, float a) {
        return withUnderlineColor(new Vector4f(r, g, b, a));
    }

    public FWLStyle withUnderlineColor(int argb) {
        return withUnderlineColor(argbToColorVec4(argb));
    }

    public FWLStyle withOutlineColor(@Nullable Vector4f outlineColor) {
        FWLStyle inst = this.clone();
        inst.outlineColor = outlineColor;
        return inst;
    }

    public FWLStyle withOutlineColor(float r, float g, float b, float a) {
        return withOutlineColor(new Vector4f(r, g, b, a));
    }

    public FWLStyle withOutlineColor(int argb) {
        return withOutlineColor(argbToColorVec4(argb));
    }

    public FWLStyle withScale(@Nullable Vector2f scale) {
        FWLStyle inst = this.clone();
        inst.scale = scale;
        return inst;
    }

    public FWLStyle withScale(float x, float y) {
        return withScale(new Vector2f(x, y));
    }

    public FWLStyle withOutlineScale(@Nullable Vector2f outlineScale) {
        FWLStyle inst = this.clone();
        inst.outlineScale = outlineScale;
        return inst;
    }

    public FWLStyle withOutlineScale(float x, float y) {
        return withOutlineScale(new Vector2f(x, y));
    }

    public FWLStyle withSkew(@Nullable Vector2f skew) {
        FWLStyle inst = this.clone();
        inst.skew = skew;
        return inst;
    }

    public FWLStyle withSkew(float x, float y) {
        return withSkew(new Vector2f(x, y));
    }

    public FWLStyle withOffset(@Nullable Vector2f offset) {
        FWLStyle inst = this.clone();
        inst.offset = offset;
        return inst;
    }

    public FWLStyle withOffset(float x, float y) {
        return withOffset(new Vector2f(x, y));
    }

    public FWLStyle withShadowOffset(@Nullable Vector2f shadowOffset) {
        FWLStyle inst = this.clone();
        inst.shadowOffset = shadowOffset;
        return inst;
    }

    public FWLStyle withShadowOffset(float x, float y) {
        return withShadowOffset(new Vector2f(x, y));
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

    public boolean hasOutline() {
        return outlineColor != null;
    }

    public Vector4f getOutlineColor() {
        return outlineColor == null ? new Vector4f(0f) : new Vector4f(outlineColor);
    }

    public Vector2f getScale() {
        return scale == null ? new Vector2f(1f) : new Vector2f(scale);
    }

    public boolean hasScale() {
        return scale != null;
    }

    public Vector2f getOutlineScale() {
        return outlineScale == null ? new Vector2f(1f) : new Vector2f(outlineScale);
    }

    public boolean hasOutlineScale() {
        return outlineScale != null;
    }

    public Vector2f getSkew() {
        return skew == null ? new Vector2f(0f) : new Vector2f(skew);
    }

    public boolean hasSkew() {
        return skew != null;
    }

    public Vector2f getOffset() {
        return offset == null ? new Vector2f(0f) : new Vector2f(offset);
    }

    public boolean hasOffset() {
        return offset != null;
    }

    public Vector2f getShadowOffset() {
        return shadowOffset == null ? new Vector2f(1f) : new Vector2f(shadowOffset);
    }

    public boolean hasShadowOffset() {
        return shadowOffset != null;
    }

    public float getVerticalAlignment() {
        return verticalAlignment == null ? 1f : verticalAlignment;
    }

    public ResourceLocation getFont() {
        return font == null ? DEFAULT_FONT : font;
    }

    public static FWLStyle merge(FWLStyle a, FWLStyle b) {
        return new FWLStyle(
                b.bold == null ? a.bold : b.bold,
                b.italic == null ? a.italic : b.italic,
                b.obfuscated == null ? a.obfuscated : b.obfuscated,
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

    public <V> ProviderBuilder withEffect(Property<V> property, Applier<V> applier) {
        return new ProviderBuilder(this).withEffect(property, applier);
    }

    public FWLStyle clone() {
        return new FWLStyle(bold, italic, obfuscated, color, backgroundColor, shadowColor, strikethroughColor, underlineColor, outlineColor, scale, outlineScale, skew, offset, shadowOffset, verticalAlignment, font);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FWLStyle fwlStyle)) return false;
        return Objects.equals(bold, fwlStyle.bold) && Objects.equals(italic, fwlStyle.italic) && Objects.equals(obfuscated, fwlStyle.obfuscated) && Objects.equals(color, fwlStyle.color) && Objects.equals(backgroundColor, fwlStyle.backgroundColor) && Objects.equals(shadowColor, fwlStyle.shadowColor) && Objects.equals(strikethroughColor, fwlStyle.strikethroughColor) && Objects.equals(underlineColor, fwlStyle.underlineColor) && Objects.equals(outlineColor, fwlStyle.outlineColor) && Objects.equals(scale, fwlStyle.scale) && Objects.equals(outlineScale, fwlStyle.outlineScale) && Objects.equals(skew, fwlStyle.skew) && Objects.equals(offset, fwlStyle.offset) && Objects.equals(shadowOffset, fwlStyle.shadowOffset) && Objects.equals(verticalAlignment, fwlStyle.verticalAlignment) && Objects.equals(font, fwlStyle.font);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bold, italic, obfuscated, color, backgroundColor, shadowColor, strikethroughColor, underlineColor, outlineColor, scale, outlineScale, skew, offset, shadowOffset, verticalAlignment, font);
    }

    private static Vector4f argbToColorVec4(int argb) {
        float red = FastColor.ARGB32.red(argb) / 255f;
        float green = FastColor.ARGB32.green(argb) / 255f;
        float blue = FastColor.ARGB32.blue(argb) / 255f;
        float alpha = FastColor.ARGB32.alpha(argb) / 255f;
        return new Vector4f(red, green, blue, alpha);
    }

    public static FWLStyle style() {
        return EMPTY;
    }

    private static <V> Property<V> property(PropertyGetter<V> getter, PropertySetter<V> setter, Class<? extends V> clazz) {
        return new Property<>(getter, setter, clazz);
    }

    public interface PropertyGetter<V> {
        V get(FWLStyle style);
    }

    public interface PropertySetter<V> {
        FWLStyle set(FWLStyle style, V value);
    }
}
