package org.figuramc.fwl.utils;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Type;

public class ResourceLocationSerializer implements JsonSerializer<ResourceLocation>, JsonDeserializer<ResourceLocation> {
    @Override
    public ResourceLocation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive primitive = jsonElement.getAsJsonPrimitive();
            if (primitive.isString()) return new ResourceLocation(primitive.getAsString());
        }
        return null;
    }

    @Override
    public JsonElement serialize(ResourceLocation location, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(location.toString());
    }
}
