package org.figuramc.fwl.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonUtils {
    public static int intOrDefault(JsonObject object, String member, int defaultValue) {
        JsonElement element = object.get(member);
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            return primitive.isNumber() ? primitive.getAsInt() : defaultValue;
        }
        return defaultValue;
    }
}
