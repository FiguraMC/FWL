package org.figuramc.fwl.text.properties.special;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.Mth;
import org.figuramc.fwl.text.fomponents.BaseFomponent;
import org.figuramc.fwl.text.properties.Property;
import org.figuramc.fwl.text.style.CompiledStyle;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.function.Function;

public class RandomProperty<T> implements Property<T> {

    private final Property<T> min, max;
    private Randomizer<T> randomizer; // min, max -> output
    private final boolean varies;

    public RandomProperty(Property<T> min, Property<T> max, Class<T> tClass) {
        this.min = min;
        this.max = max;
        if (tClass == Boolean.class) randomizer = (Randomizer<T>) (Randomizer<Boolean>) RandomProperty::random;
        else if (tClass == Float.class) randomizer = (Randomizer<T>) (Randomizer<Float>) RandomProperty::random;
        else if (tClass == Vector2f.class) randomizer = (Randomizer<T>) (Randomizer<Vector2f>) RandomProperty::random;
        else if (tClass == Vector3f.class) randomizer = (Randomizer<T>) (Randomizer<Vector3f>) RandomProperty::random;
        else if (tClass == Vector4f.class) randomizer = (Randomizer<T>) (Randomizer<Vector4f>) RandomProperty::random;
        varies = min.varies() || max.varies();
    }

    @FunctionalInterface
    private interface Randomizer<T> {
        T getRandom(T min, T max);
    }

    private static boolean random(boolean min, boolean max) { return min == max ? min : Math.random() < 0.5; }
    private static float random(float min, float max) { return Mth.lerp((float) Math.random(), min, max); }
    private static Vector2f random(Vector2f min, Vector2f max) { return min.lerp(max, (float) Math.random(), new Vector2f()); }
    private static Vector3f random(Vector3f min, Vector3f max) { return min.lerp(max, (float) Math.random(), new Vector3f()); }
    private static Vector4f random(Vector4f min, Vector4f max) { return min.lerp(max, (float) Math.random(), new Vector4f()); }

    @Override
    public CompiledStyle.CompiledProperty<T> compile(int currentIndex, BaseFomponent fomponent) {
        // TODO: If !varies, cache lambda
        CompiledStyle.CompiledProperty<T> compiledMin = min.compile(currentIndex, fomponent);
        CompiledStyle.CompiledProperty<T> compiledMax = max.compile(currentIndex, fomponent);
        return (i) -> randomizer.getRandom(compiledMin.fetch(i), compiledMax.fetch(i));
    }

    @Override
    public boolean varies() {
        return false;
    }

    @Override
    public JsonElement toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "random");
        obj.add("min", min.toJson());
        obj.add("max", max.toJson());
        return obj;
    }

    public static <T> RandomProperty<T> fromJson(JsonObject obj, Function<JsonElement, Property<T>> parser, Class<T> tClass) {
        if (!obj.has("min")) throw new IllegalArgumentException("\"random\" property must have \"min\"");
        if (!obj.has("max")) throw new IllegalArgumentException("\"random\" property must have \"max\"");
        Property<T> min = parser.apply(obj.get("min"));
        Property<T> max = parser.apply(obj.get("max"));
        return new RandomProperty<>(min, max, tClass);
    }

}
