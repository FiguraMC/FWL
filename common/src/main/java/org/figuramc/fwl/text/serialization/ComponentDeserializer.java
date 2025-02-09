package org.figuramc.fwl.text.serialization;

import com.google.gson.JsonElement;
import org.figuramc.fwl.text.components.AbstractComponent;

public interface ComponentDeserializer<C extends AbstractComponent> {
    C deserialize(JsonElement element);
}
