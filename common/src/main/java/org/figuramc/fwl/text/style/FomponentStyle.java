package org.figuramc.fwl.text.style;

import com.google.gson.JsonObject;
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
    private boolean allCacheable = false;

    private final @Nullable Property<?>[] properties = new Property<?>[StyleField.count()];

    private @Nullable CompiledStyle cached; // Cache compiled style if possible (!anyVary)
    public CompiledStyle compile(int currentIndex, BaseFomponent fomponent) {
        if (!anyNotNull) return CompiledStyle.EMPTY;
        if (cached != null) return cached;
        CompiledStyle.CompiledProperty<?>[] compiledProperties = new CompiledStyle.CompiledProperty[StyleField.count()];
        for (int i = 0; i < StyleField.count(); i++)
            compiledProperties[i] = properties[i] == null ? null : properties[i].compile(currentIndex, fomponent);
        CompiledStyle compiled = new CompiledStyle(compiledProperties);
        if (allCacheable) this.cached = compiled;
        return compiled;
    }

    // Set a field, return self for chaining
    public <T> FomponentStyle set(StyleField<T> field, Property<T> value) {
        properties[field.index] = value;
        anyNotNull = true;
        allCacheable &= value.cacheable();
        return this;
    }

    // Helper for conciseness with constants
    public <T> FomponentStyle set(StyleField<T> field, T value) {
        properties[field.index] = new ConstantProperty<>(value);
        anyNotNull = true;
        return this;
    }

    // Serialize each option into the json object
    public void writeJson(JsonObject dest) {
        for (int i = 0; i < StyleField.count(); i++)
            if (properties[i] != null)
                dest.add(StyleField.get(i).name, properties[i].toJson());
    }

    public static FomponentStyle readJson(JsonObject source) {
        // Read from the json object
        FomponentStyle style = style();
        for (int i = 0; i < StyleField.count(); i++) {
            StyleField<?> field = StyleField.get(i);
            if (source.has(field.name)) {
                Property<?> prop = field.parser.apply(source.get(field.name));
                style.properties[i] = prop;
                style.anyNotNull = true;
                style.allCacheable &= prop.cacheable();
            }
        }
        return style;
    }

    public static FomponentStyle style() { return new FomponentStyle(); }

    public boolean isEmpty() { return !anyNotNull; } // Has no style

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
