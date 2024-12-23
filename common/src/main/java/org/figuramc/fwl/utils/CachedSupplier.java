package org.figuramc.fwl.utils;

import java.util.Objects;

public abstract class CachedSupplier<S, V> {
    protected S cachedSource;
    protected V value;

    public CachedSupplier() {

    }

    public CachedSupplier(S initialSource, V initialValue) {
        this.cachedSource = initialSource;
        this.value = initialValue;
    }

    public V get(S source) {
        if (!Objects.equals(source, cachedSource)) {
            this.cachedSource = source;
            value = supply(source);
        }
        return value;
    }

    protected abstract V supply(S source);
}
