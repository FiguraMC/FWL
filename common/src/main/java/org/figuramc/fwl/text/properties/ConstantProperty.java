package org.figuramc.fwl.text.properties;

public class ConstantProperty<T> extends NonVaryingProperty<T> {
    public ConstantProperty(T value) {
        super((i, p) -> value);
    }
}