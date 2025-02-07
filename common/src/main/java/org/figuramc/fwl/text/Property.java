package org.figuramc.fwl.text;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Property<V> {
    private final Class<? extends V> clazz;
    private final FWLStyle.PropertyGetter<V> getter;
    private final FWLStyle.PropertySetter<V> setter;

    public Property(FWLStyle.PropertyGetter<V> getter, FWLStyle.PropertySetter<V> setter, Class<? extends V> clazz) {
        this.getter = getter;
        this.setter = setter;
        this.clazz = clazz;
    }

    public V get(FWLStyle style) {
        return getter.get(style);
    }

    public FWLStyle set(FWLStyle style, V value) {
        return setter != null ? setter.set(style, value) : style;
    }
}
