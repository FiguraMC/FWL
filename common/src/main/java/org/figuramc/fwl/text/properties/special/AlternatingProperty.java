package org.figuramc.fwl.text.properties.special;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.figuramc.fwl.text.properties.CacheableProperty;

public class AlternatingProperty extends CacheableProperty<Boolean> {

    public static final AlternatingProperty INSTANCE = new AlternatingProperty();

    private AlternatingProperty() {
        super((i) -> i % 2 == 1);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive("alternating");
    }
}
