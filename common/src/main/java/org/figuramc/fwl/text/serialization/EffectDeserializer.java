package org.figuramc.fwl.text.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.figuramc.fwl.text.effects.Applier;

public interface EffectDeserializer<A extends Applier<?>> {
    A deserialize(JsonElement effectObject, Class<?> fieldType);
}
