package org.figuramc.fwl.utils;

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
        throw new IllegalArgumentException("Invalid member type");
    }

    public static int intOrDefault(JsonObject object, String member, int defaultValue) {
        JsonElement element = object.get(member);
        if (element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            return primitive.isNumber() ? primitive.getAsInt() : defaultValue;
        }
        return defaultValue;
    }

    public static String stringOrThrow(JsonObject object, String member) {
        JsonElement element = object.get(member);
        if (element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isString()) return primitive.getAsString();
        }
        throw new IllegalArgumentException("Invalid type of member \"%s\". Expected string, got");
    }

    public static String stringOrDefault(JsonObject object, String member, String defaultValue) {
        JsonElement element = object.get(member);
        if (element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            return primitive.isString() ? primitive.getAsString() : defaultValue;
        }
        return defaultValue;
    }
}
