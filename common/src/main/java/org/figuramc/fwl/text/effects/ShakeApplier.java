package org.figuramc.fwl.text.effects;

import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.Property;
import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.figuramc.fwl.text.effects.ConstantApplier.constant;
import static org.figuramc.fwl.utils.MathUtils.lerp;

public class ShakeApplier {
    private static final Random random = new Random();

    public static class Vec4 implements Applier<Vector4f> {
        private final Applier<Vector4f> minOffset, maxOffset;

        public Vec4(Applier<Vector4f> minOffset, Applier<Vector4f> maxOffset) {
            this.minOffset = minOffset;
            this.maxOffset = maxOffset;
        }

        @Override
        public Vector4f get(FWLStyle current, Vector4f initialValue, int index, int length) {
            float progress = random.nextFloat();
            Vector4f minOffset = this.minOffset.get(current, initialValue, index, length);
            Vector4f maxOffset = this.maxOffset.get(current, initialValue, index, length);
            return initialValue.add(lerp(minOffset, maxOffset, progress));
        }
    }

    public static class Vec2 implements Applier<Vector2f> {
        private final Applier<Vector2f> minOffset, maxOffset;

        public Vec2(Applier<Vector2f> minOffset, Applier<Vector2f> maxOffset) {
            this.minOffset = minOffset;
            this.maxOffset = maxOffset;
        }

        @Override
        public Vector2f get(FWLStyle current, Vector2f initialValue, int index, int length) {
            float progress = random.nextFloat();
            Vector2f minOffset = this.minOffset.get(current, initialValue, index, length);
            Vector2f maxOffset = this.maxOffset.get(current, initialValue, index, length);
            return initialValue.add(lerp(minOffset, maxOffset, progress));
        }
    }

    public static class FloatValue implements Applier<Float> {
        private final Applier<Float> minOffset, maxOffset;

        public FloatValue(Applier<Float> minOffset, Applier<Float> maxOffset) {
            this.minOffset = minOffset;
            this.maxOffset = maxOffset;
        }

        @Override
        public Float get(FWLStyle current, Float initialValue, int index, int length) {
            float progress = random.nextFloat();
            Float minOffset = this.minOffset.get(current, initialValue, index, length);
            Float maxOffset = this.maxOffset.get(current, initialValue, index, length);
            return initialValue + lerp(minOffset, maxOffset, progress);
        }
    }

    public static Vec4 shake4(Applier<Vector4f> min, Applier<Vector4f> max) {
        return new Vec4(min, max);
    }

    public static Vec2 shake2(Applier<Vector2f> min, Applier<Vector2f> max) {
        return new Vec2(min, max);
    }

    public static FloatValue shake(Applier<Float> min, Applier<Float> max) {
        return new FloatValue(min, max);
    }

    public static FloatValue shake(float min, float max) {
        return new FloatValue(constant(min), constant(max));
    }
}
