package org.figuramc.fwl.text.style;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import org.figuramc.fwl.text.fomponents.BaseFomponent;
import org.figuramc.fwl.text.properties.ConstantProperty;
import org.figuramc.fwl.text.properties.commutative.AddProperty;
import org.figuramc.fwl.text.properties.special.GradientProperty;
import org.figuramc.fwl.text.properties.Property;
import org.figuramc.fwl.text.properties.special.RandomProperty;
import org.figuramc.fwl.utils.ColorUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.List;

// Is Mutable to use as a builder, but CompiledStyle (used only during visiting) is not mutable
public class FomponentStyle {

    // An empty FStyle is not changed, so just compiles to EMPTY
    private boolean anyNotNull = false;
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
        if (!anyNotNull) return CompiledStyle.EMPTY;
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

    // Serialize each option into the json object
    public void writeTo(JsonObject dest) {
        if (bold != null) dest.add("bold", bold.toJson());
        if (italic != null) dest.add("italic", italic.toJson());
        if (obfuscated != null) dest.add("obfuscated", obfuscated.toJson());
        if (color != null) dest.add("color", color.toJson());
        if (backgroundColor != null) dest.add("background_color", backgroundColor.toJson());
        if (shadowColor != null) dest.add("shadow_color", shadowColor.toJson());
        if (strikethroughColor != null) dest.add("strikethrough_color", strikethroughColor.toJson());
        if (underlineColor != null) dest.add("underline_color", underlineColor.toJson());
        if (outlineColor != null) dest.add("outline_color", outlineColor.toJson());
        if (scale != null) dest.add("scale", scale.toJson());
        if (outlineScale != null) dest.add("outline_scale", outlineScale.toJson());
        if (skew != null) dest.add("skew", skew.toJson());
        if (offset != null) dest.add("offset", offset.toJson());
        if (shadowOffset != null) dest.add("shadow_offset", shadowOffset.toJson());
        if (verticalAlignment != null) dest.add("vertical_alignment", verticalAlignment.toJson());
        if (font != null) dest.add("font", font.toJson());
    }

    public static FomponentStyle readFrom(JsonObject source) {
        FomponentStyle style = new FomponentStyle();
        style.bold = source.has("bold") ? Property.parseBoolean(source.get("bold")) : null;
        style.italic = source.has("italic") ? Property.parseBoolean(source.get("italic")) : null;
        style.obfuscated = source.has("obfuscated") ? Property.parseBoolean(source.get("obfuscated")) : null;

        style.color = source.has("color") ? Property.parseColor(source.get("color")) : null;
        style.backgroundColor = source.has("background_color") ? Property.parseColor(source.get("background_color")) : null;
        style.shadowColor = source.has("shadow_color") ? Property.parseColor(source.get("shadow_color")) : null;
        style.strikethroughColor = source.has("strikethrough_color") ? Property.parseColor(source.get("strikethrough_color")) : null;
        style.underlineColor = source.has("underline_color") ? Property.parseColor(source.get("underline_color")) : null;
        style.outlineColor = source.has("outline_color") ? Property.parseColor(source.get("outline_color")) : null;

        style.scale = source.has("scale") ? Property.parseVec2(source.get("scale")) : null;
        style.outlineScale = source.has("outline_scale") ? Property.parseVec2(source.get("outline_scale")) : null;
        style.skew = source.has("skew") ? Property.parseVec2(source.get("skew")) : null;
        style.offset = source.has("offset") ? Property.parseVec2(source.get("offset")) : null;
        style.shadowOffset = source.has("shadow_offset") ? Property.parseVec2(source.get("shadow_offset")) : null;

        style.verticalAlignment = source.has("vertical_alignment") ? Property.parseFloat(source.get("vertical_alignment")) : null;

        style.font = source.has("font") ? Property.parseFont(source.get("font")) : null;

        style.anyNotNull = style.anyNotNull();
        style.anyVary = style.anyVaries();
        return style;
    }

    public static FomponentStyle style() { return new FomponentStyle(); }

    public boolean isEmpty() { return !anyNotNull; } // Has no style


    private boolean anyNotNull() {
        return
                bold != null ||
                italic != null ||
                obfuscated != null ||
                color != null ||
                backgroundColor != null ||
                shadowColor != null ||
                strikethroughColor != null ||
                underlineColor != null ||
                outlineColor != null ||
                scale != null ||
                outlineScale != null ||
                skew != null ||
                offset != null ||
                shadowOffset != null ||
                verticalAlignment != null ||
                font != null;
    }
    private boolean anyVaries() {
        return
                (bold != null && bold.varies()) ||
                (italic != null && italic.varies()) ||
                (obfuscated != null && obfuscated.varies()) ||
                (color != null && color.varies()) ||
                (backgroundColor != null && backgroundColor.varies()) ||
                (shadowColor != null && shadowColor.varies()) ||
                (strikethroughColor != null && strikethroughColor.varies()) ||
                (underlineColor != null && underlineColor.varies()) ||
                (outlineColor != null && outlineColor.varies()) ||
                (scale != null && scale.varies()) ||
                (outlineScale != null && outlineScale.varies()) ||
                (skew != null && skew.varies()) ||
                (offset != null && offset.varies()) ||
                (shadowOffset != null && shadowOffset.varies()) ||
                (verticalAlignment != null && verticalAlignment.varies()) ||
                (font != null && font.varies());
    }

    public FomponentStyle setBold(Property<Boolean> property) {
        this.bold = property;
        anyNotNull = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setItalic(Property<Boolean> property) {
        this.italic = property;
        anyNotNull = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setObfuscated(Property<Boolean> property) {
        this.obfuscated = property;
        anyNotNull = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setColor(Property<Vector4f> property) {
        this.color = property;
        anyNotNull = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setBackgroundColor(Property<Vector4f> property) {
        this.backgroundColor = property;
        anyNotNull = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setShadowColor(Property<Vector4f> property) {
        this.shadowColor = property;
        anyNotNull = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setStrikethroughColor(Property<Vector4f> property) {
        this.strikethroughColor = property;
        anyNotNull = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setUnderlineColor(Property<Vector4f> property) {
        this.underlineColor = property;
        anyNotNull = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setOutlineColor(Property<Vector4f> property) {
        this.outlineColor = property;
        anyNotNull = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setScale(Property<Vector2f> property) {
        this.scale = property;
        anyNotNull = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setOutlineScale(Property<Vector2f> property) {
        this.outlineScale = property;
        anyNotNull = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setSkew(Property<Vector2f> property) {
        this.skew = property;
        anyNotNull = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setOffset(Property<Vector2f> property) {
        this.offset = property;
        anyNotNull = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setShadowOffset(Property<Vector2f> property) {
        this.shadowOffset = property;
        anyNotNull = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setVerticalAlignment(Property<Float> property) {
        this.verticalAlignment = property;
        anyNotNull = true;
        anyVary |= property.varies();
        return this;
    }
    public FomponentStyle setFont(Property<ResourceLocation> property) {
        this.font = property;
        anyNotNull = true;
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
    @SafeVarargs public static Property<Float> gradient(Property<Float>... properties) { return new GradientProperty<>(List.of(properties), Float.class); }
    public static Property<Float> random(Property<Float> min, Property<Float> max) { return new RandomProperty<>(min, max, Float.class); }
    @SafeVarargs public static Property<Float> add(Property<Float>... values) { return new AddProperty<>(List.of(values), Float.class); }

    // Vector2
    public static final Property<Vector2f> VEC2_ZERO = new ConstantProperty<>(new Vector2f());
    public static final Property<Vector2f> VEC2_ONE = new ConstantProperty<>(new Vector2f(1));
    public static Property<Vector2f> constant(float x, float y) { return new ConstantProperty<>(new Vector2f(x, y)); }
    @SafeVarargs public static Property<Vector2f> gradient2(Property<Vector2f>... properties) { return new GradientProperty<>(List.of(properties), Vector2f.class); }
    public static Property<Vector2f> random2(Property<Vector2f> min, Property<Vector2f> max) { return new RandomProperty<>(min, max, Vector2f.class); }
    @SafeVarargs public static Property<Vector2f> add2(Property<Vector2f>... values) { return new AddProperty<>(List.of(values), Vector2f.class); }

    // Vector4
    public static final Property<Vector4f> VEC4_ZERO = new ConstantProperty<>(new Vector4f());
    public static final Property<Vector4f> VEC4_ONE = new ConstantProperty<>(new Vector4f(1));
    public static Property<Vector4f> constant(float x, float y, float z, float w) { return new ConstantProperty<>(new Vector4f(x, y, z, w)); }
    @SafeVarargs public static Property<Vector4f> gradient4(Property<Vector4f>... properties) { return new GradientProperty<>(List.of(properties), Vector4f.class); }
    public static Property<Vector4f> random4(Property<Vector4f> min, Property<Vector4f> max) { return new RandomProperty<>(min, max, Vector4f.class); }
    @SafeVarargs public static Property<Vector4f> add4(Property<Vector4f>... values) { return new AddProperty<>(List.of(values), Vector4f.class); }

    // Color from string
    public static Property<Vector4f> color(String color) { return new ConstantProperty<>(ColorUtils.fromString(color)); }

    // Any type constant
    public static <T> Property<T> constant(T value) { return new ConstantProperty<>(value); }

}
