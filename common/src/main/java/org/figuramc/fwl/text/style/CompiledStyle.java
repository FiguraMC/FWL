package org.figuramc.fwl.text.style;

import org.figuramc.fwl.text.properties.Property;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable style, compiled from a FomponentStyle + some context.
 */
public class CompiledStyle {

    private final @Nullable CompiledProperty<?>[] compiledProperties;

    public static final CompiledStyle EMPTY = new CompiledStyle(new CompiledProperty[StyleField.count()]);

    public CompiledStyle(@Nullable CompiledProperty<?>[] properties) {
        if (properties.length != StyleField.count()) throw new IllegalArgumentException("Array must have correct size");
        this.compiledProperties = properties;
    }

    // Extend the parent with the child
    public CompiledStyle extend(CompiledStyle child) {
        if (this == EMPTY) return child;
        if (child == EMPTY) return this;
        CompiledStyle result = new CompiledStyle(new CompiledProperty[StyleField.count()]);
        for (int i = 0; i < StyleField.count(); i++)
            result.compiledProperties[i] = child.compiledProperties[i] != null ? child.compiledProperties[i] : this.compiledProperties[i];
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(StyleField<T> field, int index) {
        @Nullable CompiledProperty<T> prop = (CompiledProperty<T>) compiledProperties[field.index];
        if (prop == null) return field.defaultValue;
        return prop.fetch(index);
    }

    @FunctionalInterface
    public interface CompiledProperty<T> {
        T fetch(int index);
    }

}
