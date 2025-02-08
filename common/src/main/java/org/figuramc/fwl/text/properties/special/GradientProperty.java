package org.figuramc.fwl.text.properties.special;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.Mth;
import org.figuramc.fwl.text.properties.Property;
import org.figuramc.fwl.text.style.CompiledStyle;
import org.figuramc.fwl.text.fomponents.BaseFomponent;
import org.figuramc.fwl.utils.JsonUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GradientProperty<T> implements Property<T> {

    private final List<Property<T>> values;
    private final Lerper<T> lerper;

    @SuppressWarnings("unchecked")
    public GradientProperty(List<Property<T>> values, Class<T> tClass) {
        this.values = values;
        if (tClass == Boolean.class) lerper = (Lerper<T>) (Lerper<Boolean>) (delta, start, end) -> delta < 0.5 ? start : end;
        else if (tClass == Float.class) lerper = (Lerper<T>) (Lerper<Float>) Mth::lerp;
        else if (tClass == Vector2f.class) lerper = (Lerper<T>) (Lerper<Vector2f>) (delta, start, end) -> start.lerp(end, delta, new Vector2f());
        else if (tClass == Vector3f.class) lerper = (Lerper<T>) (Lerper<Vector3f>) (delta, start, end) -> start.lerp(end, delta, new Vector3f());
        else if (tClass == Vector4f.class) lerper = (Lerper<T>) (Lerper<Vector4f>) (delta, start, end) -> start.lerp(end, delta, new Vector4f());
        else throw new IllegalArgumentException("Unexpected class for GradientProperty: expected boolean, float or vector, got " + tClass);
    }

    @Override
    public CompiledStyle.CompiledProperty<T> compile(int startIndex, BaseFomponent fomponent) {
        // TODO Cache array if none of the values vary
        CompiledStyle.CompiledProperty<T>[] compiledValues = new CompiledStyle.CompiledProperty[values.size()];
        for (int i = 0; i < compiledValues.length; i++) compiledValues[i] = values.get(i).compile(startIndex, fomponent);

        int len = fomponent.length();
        return (i) -> {
            float progress = (float) (i - startIndex) / len; // 0 to 1
            float index = progress * (compiledValues.length - 1);
            return lerper.lerp(index % 1, compiledValues[(int) index].fetch(i), compiledValues[((int) index)+1].fetch(i));
        };
    }

    @Override public boolean varies() { return true; }

    @FunctionalInterface
    private interface Lerper<T> {
        T lerp(float delta, T start, T end);
    }

    @Override
    public JsonElement toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "gradient");
        JsonArray jsonValues = new JsonArray();
        for (Property<T> value : this.values)
            jsonValues.add(value.toJson());
        obj.add("values", jsonValues);
        return obj;
    }

    public static <T> GradientProperty<T> fromJson(JsonObject obj, Function<JsonElement, Property<T>> parser, Class<T> tClass) {
        @Nullable JsonArray values = JsonUtils.arrayOrNull(obj, "values");
        if (values == null) throw new IllegalArgumentException("\"gradient\" property must have values array");
        List<Property<T>> valuesList = new ArrayList<>();
        for (JsonElement value : values)
            valuesList.add(parser.apply(value));
        return new GradientProperty<>(valuesList, tClass);
    }

}