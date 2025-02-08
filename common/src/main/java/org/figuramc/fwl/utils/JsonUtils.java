package org.figuramc.fwl.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class JsonUtils {

    public static int intOrDefault(JsonObject object, String member, int defaultValue) {
        JsonElement element = object.get(member);
        if (element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            return primitive.isNumber() ? primitive.getAsInt() : defaultValue;
        }
        return defaultValue;
    }

    @Contract("_,_,!null->!null")
    public static @Nullable String stringOrDefault(JsonObject object, String member, String defaultValue) {
        JsonElement element = object.get(member);
        if (element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            return primitive.isString() ? primitive.getAsString() : defaultValue;
        }
        return defaultValue;
    }

    public static boolean booleanOrDefault(JsonObject object, String member, boolean defaultValue) {
        JsonElement element = object.get(member);
        if (element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            return primitive.isBoolean() ? primitive.getAsBoolean() : defaultValue;
        }
        return defaultValue;
    }

    public static @Nullable JsonArray arrayOrNull(JsonObject object, String member) {
        @Nullable JsonElement element = object.get(member);
        if (element != null && element.isJsonArray()) return element.getAsJsonArray();
        return null;
    }

    public static JsonArray vec(float... floats) {
        JsonArray array = new JsonArray();
        for (float f : floats) array.add(f);
        return array;
    }

    public static boolean isVec(@NotNull JsonElement element, int size) {
        if (!element.isJsonArray()) return false;
        JsonArray arr = element.getAsJsonArray();
        if (arr.size() != size) return false;
        for (int i = 0; i < size; i++)
            if (!arr.get(i).isJsonPrimitive() || !arr.get(i).getAsJsonPrimitive().isNumber()) return false;
        return true;
    }


    public static JsonElement fromObject(Object unknown) {
        if (unknown instanceof Boolean b) return new JsonPrimitive(b);
        if (unknown instanceof String s) return new JsonPrimitive(s);
        if (unknown instanceof Number n) return new JsonPrimitive(n);
        if (unknown instanceof Vector2f vec) return JsonUtils.vec(vec.x, vec.y);
        if (unknown instanceof Vector3f vec) return JsonUtils.vec(vec.x, vec.y, vec.z);
        if (unknown instanceof Vector4f vec) return JsonUtils.vec(vec.x, vec.y, vec.z, vec.w);
        throw new IllegalArgumentException("Unexpected type to JsonUtils.fromObject: " + unknown.getClass());
    }

}
