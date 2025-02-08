package org.figuramc.fwl.text.properties.special;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.figuramc.fwl.text.properties.NonVaryingProperty;

public class AlternatingProperty extends NonVaryingProperty<Boolean> {

    public static final AlternatingProperty INSTANCE = new AlternatingProperty();

    private AlternatingProperty() {
        super((i) -> i % 2 == 1);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive("alternating");
    }
}
