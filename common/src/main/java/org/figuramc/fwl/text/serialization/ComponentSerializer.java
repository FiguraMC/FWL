package org.figuramc.fwl.text.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.figuramc.fwl.text.components.AbstractComponent;

public interface ComponentSerializer<C extends AbstractComponent> {
    JsonObject serialize(C component);
}
