package org.figuramc.fwl.text.properties;

import com.google.gson.JsonElement;
import org.figuramc.fwl.utils.JsonUtils;

public class ConstantProperty<T> extends CacheableProperty<T> {
    private final T value;
    public ConstantProperty(T value) {
        super((i) -> value);
        this.value = value;
    }

    @Override
    public JsonElement toJson() {
        return JsonUtils.fromObject(value);
    }
}