package org.figuramc.fwl.text.properties.commutative;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.figuramc.fwl.text.fomponents.BaseFomponent;
import org.figuramc.fwl.text.properties.Property;
import org.figuramc.fwl.text.style.CompiledStyle;
import org.figuramc.fwl.utils.JsonUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class CommutativeProperty<T> implements Property<T> {

    private final List<Property<T>> values;
    private final boolean cacheable;
    private final Operation<T> operation;
    private final T unit;

    @SuppressWarnings("unchecked")
    protected CommutativeProperty(List<Property<T>> values, Class<T> tClass) {
        this.values = values;
        if (tClass == Boolean.class) { operation = (Operation<T>) (Operation<Boolean>) this::operate; unit = (T) (Boolean) unitBool(); }
        else if (tClass == Float.class) { operation = (Operation<T>) (Operation<Float>) this::operate; unit = (T) (Float) unitFloat(); }
        else if (tClass == Vector2f.class) { operation = (Operation<T>) (Operation<Vector2f>) this::operate; unit = (T) unitVec2(); }
        else if (tClass == Vector3f.class) { operation = (Operation<T>) (Operation<Vector3f>) this::operate; unit = (T) unitVec3(); }
        else if (tClass == Vector4f.class) { operation = (Operation<T>) (Operation<Vector4f>) this::operate; unit = (T) unitVec4(); }
        else throw new IllegalArgumentException("Unexpected class for AddProperty: expected boolean, float, or vector, got " + tClass);
        this.cacheable = values.stream().allMatch(Property::cacheable);
    }

    protected abstract String id();

    protected abstract boolean unitBool();
    protected abstract float unitFloat();
    protected abstract Vector2f unitVec2();
    protected abstract Vector3f unitVec3();
    protected abstract Vector4f unitVec4();

    protected abstract boolean operate(boolean left, boolean right);
    protected abstract float operate(float left, float right);
    protected abstract Vector2f operate(Vector2f left, Vector2f right);
    protected abstract Vector3f operate(Vector3f left, Vector3f right);
    protected abstract Vector4f operate(Vector4f left, Vector4f right);

    @FunctionalInterface
    protected interface Operation<T> {
        T act(T left, T right);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CompiledStyle.CompiledProperty<T> compile(int currentIndex, BaseFomponent fomponent) {
        // TODO: If !varies, cache this array and lambda to improve perf
        CompiledStyle.CompiledProperty<T>[] compiledValues = new CompiledStyle.CompiledProperty[values.size()];
        for (int i = 0; i < values.size(); i++) compiledValues[i] = values.get(i).compile(currentIndex, fomponent);
        return (i) -> {
            T value = unit;
            for (CompiledStyle.CompiledProperty<T> compiled : compiledValues)
                value = operation.act(value, compiled.fetch(i));
            return value;
        };
    }

    @Override
    public boolean cacheable() {
        return cacheable;
    }

    @Override
    public JsonElement toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", id());
        JsonArray jsonValues = new JsonArray();
        for (Property<T> value : this.values)
            jsonValues.add(value.toJson());
        obj.add("values", jsonValues);
        return obj;
    }

    public static <T, P extends CommutativeProperty<T>> P fromJson(JsonObject obj, Function<JsonElement, Property<T>> parser, Class<T> tClass, String key, BiFunction<List<Property<T>>, Class<T>, P> constructor) {
        @Nullable JsonArray values = JsonUtils.arrayOrNull(obj, "values");
        if (values == null) throw new IllegalArgumentException("\"" + key + "\" property must have values array");
        List<Property<T>> valuesList = new ArrayList<>();
        for (JsonElement value : values)
            valuesList.add(parser.apply(value));
        return constructor.apply(valuesList, tClass);
    }

}
