package org.figuramc.fwl.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonUtils {
    public static int intOrThrow(JsonObject object, String member) {
        JsonElement element = object.get(member);
        if (element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isNumber()) return primitive.getAsInt();
        }
        throw new IllegalArgumentException("Invalid type of member \"%s\". Expected int.".formatted(member));
    }

    public static int intOrDefault(JsonObject object, String member, int defaultValue) {
        try { return intOrThrow(object, member); }
        catch (Exception e) { return defaultValue; }
    }

    public static String stringOrThrow(JsonObject object, String member) {
        JsonElement element = object.get(member);
        if (element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isString()) return primitive.getAsString();
        }
        throw new IllegalArgumentException("Invalid type of member \"%s\". Expected string.".formatted(member));
    }

    public static String stringOrDefault(JsonObject object, String member, String defaultValue) {
        try { return stringOrThrow(object, member); }
        catch (Exception e) { return defaultValue; }
    }

    public static JsonArray arrayOrThrow(JsonObject object, String member) {
        JsonElement element = object.get(member);
        if (element != null && element.isJsonArray()) return element.getAsJsonArray();
        throw new IllegalArgumentException("Invalid type of member \"%s\". Expected array.".formatted(member));
    }

    public static JsonArray arrayOrDefault(JsonObject object, String member, JsonArray defaultValue) {
        try { return arrayOrThrow(object, member); }
        catch (Exception e) { return defaultValue; }
    }
}
