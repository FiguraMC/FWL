package org.figuramc.fwl.text.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.figuramc.fwl.text.effects.Applier;

public interface EffectSerializer<A extends Applier<?>> {
    JsonElement serialize(A applier);
}
