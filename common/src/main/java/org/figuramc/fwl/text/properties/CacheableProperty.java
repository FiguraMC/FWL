package org.figuramc.fwl.text.properties;

import org.figuramc.fwl.text.style.CompiledStyle;
import org.figuramc.fwl.text.fomponents.BaseFomponent;

public abstract class CacheableProperty<T> implements Property<T> {
    private final CompiledStyle.CompiledProperty<T> compiledProperty;
    protected CacheableProperty(CompiledStyle.CompiledProperty<T> compiledProperty) {
        this.compiledProperty = compiledProperty;
    }
    @Override public CompiledStyle.CompiledProperty<T> compile(int currentIndex, BaseFomponent fomponent) { return compiledProperty; }
    @Override public final boolean cacheable() { return true; }
}