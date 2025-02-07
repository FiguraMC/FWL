package org.figuramc.fwl.text.style;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.figuramc.fwl.text.fomponents.BaseFomponent;
import org.figuramc.fwl.text.properties.ConstantProperty;
import org.figuramc.fwl.text.properties.GradientProperty;
import org.figuramc.fwl.text.properties.Property;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector4f;

// Is Mutable to use as a builder, but CompiledStyle (used only during visiting) is not mutable
public class FomponentStyle {

    // An empty FStyle is not changed, so just compiles to EMPTY
    private boolean changed = false;
    private boolean anyVary = false;

    private @Nullable Property<Boolean> bold;
    private @Nullable Property<Boolean> italic;
    private @Nullable Property<Boolean> obfuscated;
    private @Nullable Property<Vector4f> color;
    private @Nullable Property<Vector4f> backgroundColor;
    private @Nullable Property<Vector4f> shadowColor;
    private @Nullable Property<Vector4f> strikethroughColor;
    private @Nullable Property<Vector4f> underlineColor;
    private @Nullable Property<Vector4f> outlineColor;
    private @Nullable Property<Vector2f> scale;
    private @Nullable Property<Vector2f> outlineScale;
    private @Nullable Property<Vector2f> skew;
    private @Nullable Property<Vector2f> offset;
    private @Nullable Property<Vector2f> shadowOffset;
    private @Nullable Property<Float> verticalAlignment;
    private @Nullable Property<ResourceLocation> font;

    private @Nullable CompiledStyle cached; // Cache compiled style if possible (!anyVary)
    public CompiledStyle compile(int currentIndex, BaseFomponent fomponent) {
        if (!changed) return CompiledStyle.EMPTY;
        if (cached != null) return cached;
        CompiledStyle compiled = new CompiledStyle(
                bold == null ? null : bold.compile(currentIndex, fomponent),
                italic == null ? null : italic.compile(currentIndex, fomponent),
                obfuscated == null ? null : obfuscated.compile(currentIndex, fomponent),
                color == null ? null : color.compile(currentIndex, fomponent),
                backgroundColor == null ? null : backgroundColor.compile(currentIndex, fomponent),
                shadowColor == null ? null : shadowColor.compile(currentIndex, fomponent),
                strikethroughColor == null ? null : strikethroughColor.compile(currentIndex, fomponent),
                underlineColor == null ? null : underlineColor.compile(currentIndex, fomponent),
                outlineColor == null ? null : outlineColor.compile(currentIndex, fomponent),
                scale == null ? null : scale.compile(currentIndex, fomponent),
                outlineScale == null ? null : outlineScale.compile(currentIndex, fomponent),
                skew == null ? null : skew.compile(currentIndex, fomponent),
                offset == null ? null : offset.compile(currentIndex, fomponent),
                shadowOffset == null ? null : shadowOffset.compile(currentIndex, fomponent),
                verticalAlignment == null ? null : verticalAlignment.compile(currentIndex, fomponent),
                font == null ? null : font.compile(currentIndex, fomponent)
        );
        if (!anyVary) this.cached = compiled;
        return compiled;
    }

    public static FomponentStyle empty() { return new FomponentStyle(); }

    public FomponentStyle setBold(Property<Boolean> property) {
        this.bold = property;
        changed = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setItalic(Property<Boolean> property) {
        this.italic = property;
        changed = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setObfuscated(Property<Boolean> property) {
        this.obfuscated = property;
        changed = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setColor(Property<Vector4f> property) {
        this.color = property;
        changed = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setBackgroundColor(Property<Vector4f> property) {
        this.backgroundColor = property;
        changed = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setShadowColor(Property<Vector4f> property) {
        this.shadowColor = property;
        changed = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setStrikethroughColor(Property<Vector4f> property) {
        this.strikethroughColor = property;
        changed = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setUnderlineColor(Property<Vector4f> property) {
        this.underlineColor = property;
        changed = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setOutlineColor(Property<Vector4f> property) {
        this.outlineColor = property;
        changed = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setScale(Property<Vector2f> property) {
        this.scale = property;
        changed = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setOutlineScale(Property<Vector2f> property) {
        this.outlineScale = property;
        changed = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setSkew(Property<Vector2f> property) {
        this.skew = property;
        changed = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setOffset(Property<Vector2f> property) {
        this.offset = property;
        changed = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setShadowOffset(Property<Vector2f> property) {
        this.shadowOffset = property;
        changed = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setVerticalAlignment(Property<Float> property) {
        this.verticalAlignment = property;
        changed = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setFont(Property<ResourceLocation> property) {
        this.font = property;
        changed = true;
        anyVary |= property.varies();
        return this;
    }

    // Some common properties:

    // Booleans
    public static final Property<Boolean> FALSE = new ConstantProperty<>(false);
    public static final Property<Boolean> TRUE = new ConstantProperty<>(true);
    public static Property<Boolean> constant(boolean value) { return value ? TRUE : FALSE; }

    // Floats
    public static final Property<Float> ZERO = new ConstantProperty<>(0.0f);
    public static final Property<Float> ONE = new ConstantProperty<>(1.0f);
    public static Property<Float> constant(float value) { return new ConstantProperty<>(value); }
    public static Property<Float> gradientFloat(Property<Float> start, Property<Float> end) { return new GradientProperty<>(start, end, Mth::lerp); }

    // Vector2
    public static final Property<Vector2f> VEC2_ZERO = new ConstantProperty<>(new Vector2f());
    public static final Property<Vector2f> VEC2_ONE = new ConstantProperty<>(new Vector2f(1));
    public static Property<Vector2f> constant(Vector2f value) { return new ConstantProperty<>(value); }
    public static Property<Vector2f> constant(float x, float y) { return new ConstantProperty<>(new Vector2f(x, y)); }
    public static Property<Vector2f> gradientVec2(Property<Vector2f> start, Property<Vector2f> end) { return new GradientProperty<>(start, end, (d, s, e) -> s.lerp(e, d, new Vector2f())); }

    // Vector4
    public static final Property<Vector4f> VEC4_ZERO = new ConstantProperty<>(new Vector4f());
    public static final Property<Vector4f> VEC4_ONE = new ConstantProperty<>(new Vector4f(1));
    public static Property<Vector4f> constant(Vector4f value) { return new ConstantProperty<>(value); }
    public static Property<Vector4f> constant(float x, float y, float z, float w) { return new ConstantProperty<>(new Vector4f(x, y, z, w)); }
    public static Property<Vector4f> gradientVec4(Property<Vector4f> start, Property<Vector4f> end) { return new GradientProperty<>(start, end, (d, s, e) -> s.lerp(e, d, new Vector4f())); }




}
