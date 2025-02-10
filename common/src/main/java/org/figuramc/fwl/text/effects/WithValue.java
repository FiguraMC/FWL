package org.figuramc.fwl.text.effects;

import org.figuramc.fwl.text.FWLStyle;

public class WithValue<V> implements Applier<V> {
    private final Applier<V> applier;
    private final Applier<V> value;

    public WithValue(Applier<V> applier, Applier<V> value) {
        this.applier = applier;
        this.value = value;
    }

    @Override
    public V get(FWLStyle current, V initalValue, int index, int length) {
        V value = this.value.get(current, initalValue, index, length);
        return applier.get(current, value, index, length);
    }

    @Override
    public String getType() {
        return null;
    }

    public Applier<V> applier() {
        return applier;
    }

    public Applier<V> value() {
        return value;
    }

    public static <V> WithValue<V> withValue(Applier<V> value, Applier<V> applier) {
        return new WithValue<>(applier, value);
    }
}
