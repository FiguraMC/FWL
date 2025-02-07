package org.figuramc.fwl.text.effects;

import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.Property;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class ConstantApplier<V> implements Applier<V> {
    private final V value;

    public ConstantApplier(V value) {
        this.value = value;
    }


    @Override
    public V get(FWLStyle current, V initalValue, int index, int length) {
        return value;
    }

    public static Applier<Boolean> constant(boolean value) {
        return new ConstantApplier<>(value);
    }

    public static Applier<Float> constant(float value) {
        return new ConstantApplier<>(value);
    }

    public static Applier<Vector2f> constant(float x, float y) {
        return constant(new Vector2f(x, y));
    }

    public static Applier<Vector2f> constant(Vector2f value) {
        return new ConstantApplier<>(value);
    }

    public static Applier<Vector4f> constant(float x, float y, float z, float w) {
        return constant(new Vector4f(x, y, z, w));
    }

    public static Applier<Vector4f> constant(Vector4f value) {
        return new ConstantApplier<>(value);
    }
}
