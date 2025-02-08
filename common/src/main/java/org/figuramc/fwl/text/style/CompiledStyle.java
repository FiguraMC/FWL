package org.figuramc.fwl.text.style;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector4f;

/**
 * Immutable style, compiled from a FomponentStyle + some context.
 */
public record CompiledStyle(
        @Nullable CompiledStyle.CompiledProperty<Boolean> bold,
        @Nullable CompiledStyle.CompiledProperty<Boolean> italic,
        @Nullable CompiledStyle.CompiledProperty<Boolean> obfuscated,

        @Nullable CompiledStyle.CompiledProperty<Vector4f> color,
        @Nullable CompiledStyle.CompiledProperty<Vector4f> backgroundColor,
        @Nullable CompiledStyle.CompiledProperty<Vector4f> shadowColor,
        @Nullable CompiledStyle.CompiledProperty<Vector4f> strikethroughColor,
        @Nullable CompiledStyle.CompiledProperty<Vector4f> underlineColor,
        @Nullable CompiledStyle.CompiledProperty<Vector4f> outlineColor,

        @Nullable CompiledStyle.CompiledProperty<Vector2f> scale,
        @Nullable CompiledStyle.CompiledProperty<Vector2f> outlineScale,
        @Nullable CompiledStyle.CompiledProperty<Vector2f> skew,
        @Nullable CompiledStyle.CompiledProperty<Vector2f> offset,
        @Nullable CompiledStyle.CompiledProperty<Vector2f> shadowOffset,

        @Nullable CompiledStyle.CompiledProperty<Float> verticalAlignment,
        @Nullable CompiledStyle.CompiledProperty<ResourceLocation> font
) {

    public static final CompiledStyle EMPTY = new CompiledStyle(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    // Extend the parent with the child
    public CompiledStyle extend(CompiledStyle child) {
        if (this == EMPTY) return child;
        if (child == EMPTY) return this;
        return new CompiledStyle(
                child.bold != null ? child.bold : this.bold,
                child.italic != null ? child.italic : this.italic,
                child.obfuscated != null ? child.obfuscated : this.obfuscated,
                child.color != null ? child.color : this.color,
                child.backgroundColor != null ? child.backgroundColor : this.backgroundColor,
                child.shadowColor != null ? child.shadowColor : this.shadowColor,
                child.strikethroughColor != null ? child.strikethroughColor : this.strikethroughColor,
                child.underlineColor != null ? child.underlineColor : this.underlineColor,
                child.outlineColor != null ? child.outlineColor : this.outlineColor,
                child.scale != null ? child.scale : this.scale,
                child.outlineScale != null ? child.outlineScale : this.outlineScale,
                child.skew != null ? child.skew : this.skew,
                child.offset != null ? child.offset : this.offset,
                child.shadowOffset != null ? child.shadowOffset : this.shadowOffset,
                child.verticalAlignment != null ? child.verticalAlignment : this.verticalAlignment,
                child.font != null ? child.font : this.font
        );
    }

    public boolean isBold(int index) {
        return bold != null && bold.fetch(index);
    }
    public boolean isItalic(int index) {
        return italic != null && italic.fetch(index);
    }
    public boolean isObfuscated(int index) {
        return obfuscated != null && obfuscated.fetch(index);
    }

    private static final Vector4f VEC4_ZEROES = new Vector4f(0);
    private static final Vector4f VEC4_ONES = new Vector4f(1);

    public Vector4f getColor(int index) {
        return color == null ? VEC4_ONES : color.fetch(index);
    }

    public Vector4f getBackgroundColor(int index) {
        return backgroundColor == null ? VEC4_ZEROES : backgroundColor.fetch(index);
    }

    public Vector4f getShadowColor(int index) {
        return shadowColor == null ? VEC4_ZEROES : shadowColor.fetch(index);
    }

    public Vector4f getStrikethroughColor(int index) {
        return strikethroughColor == null ? VEC4_ZEROES : strikethroughColor.fetch(index);
    }

    public Vector4f getUnderlineColor(int index) {
        return underlineColor == null ? VEC4_ZEROES : underlineColor.fetch(index);
    }

    public Vector4f getOutlineColor(int index) {
        return outlineColor == null ? VEC4_ZEROES : outlineColor.fetch(index);
    }

    private static final Vector2f VEC2_ZEROES = new Vector2f(0);
    private static final Vector2f VEC2_ONES = new Vector2f(1);

    public Vector2f getScale(int index) {
        return scale == null ? VEC2_ONES : scale.fetch(index);
    }

    public Vector2f getOutlineScale(int index) {
        return outlineScale == null ? VEC2_ONES : outlineScale.fetch(index);
    }

    public Vector2f getSkew(int index) {
        return skew == null ? VEC2_ZEROES : skew.fetch(index);
    }

    public Vector2f getOffset(int index) {
        return offset == null ? VEC2_ZEROES : offset.fetch(index);
    }

    public Vector2f getShadowOffset(int index) {
        return shadowOffset == null ? VEC2_ONES : shadowOffset.fetch(index);
    }

    public float getVerticalAlignment(int index) {
        return verticalAlignment == null ? 1f : verticalAlignment.fetch(index);
    }

    private static final ResourceLocation DEFAULT_FONT = new ResourceLocation("minecraft", "default");
    public ResourceLocation getFont(int index) {
        return font == null ? DEFAULT_FONT : font.fetch(index);
    }

    @FunctionalInterface
    public interface CompiledProperty<T> {
        T fetch(int index);
    }

}
