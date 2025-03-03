package org.figuramc.fwl.text.effects;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.serialization.FWLSerializer;
import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.figuramc.fwl.text.effects.ConstantApplier.constant;
import static org.figuramc.fwl.utils.MathUtils.lerp;

public abstract class ShakeApplier<V> implements Applier<V> {
    private static final Random random = new Random();
    protected final Applier<V> minOffset, maxOffset;

    protected ShakeApplier(Applier<V> minOffset, Applier<V> maxOffset) {
        this.minOffset = minOffset;
        this.maxOffset = maxOffset;
    }

    public static class Vec4 extends ShakeApplier<Vector4f> {

        public Vec4(Applier<Vector4f> minOffset, Applier<Vector4f> maxOffset) {
            super(minOffset, maxOffset);
        }

        @Override
        public Vector4f get(FWLStyle current, Vector4f initialValue, int index, int length) {
            Vector4f minOffset = this.minOffset.get(current, initialValue, index, length);
            Vector4f maxOffset = this.maxOffset.get(current, initialValue, index, length);
            return initialValue.add(new Vector4f(
                    lerp(minOffset.x, maxOffset.x, random.nextFloat()),
                    lerp(minOffset.y, maxOffset.y, random.nextFloat()),
                    lerp(minOffset.z, maxOffset.z, random.nextFloat()),
                    lerp(minOffset.w, maxOffset.w, random.nextFloat())
            ));
        }

        @Override
        protected Class<?> fieldType() {
            return Vector4f.class;
        }
    }
    public static class Vec2 extends ShakeApplier<Vector2f> {
        public Vec2(Applier<Vector2f> minOffset, Applier<Vector2f> maxOffset) {
            super(minOffset, maxOffset);
        }

        @Override
        public Vector2f get(FWLStyle current, Vector2f initialValue, int index, int length) {
            Vector2f minOffset = this.minOffset.get(current, initialValue, index, length);
            Vector2f maxOffset = this.maxOffset.get(current, initialValue, index, length);
            return initialValue.add(new Vector2f(
                    lerp(minOffset.x, maxOffset.x, random.nextFloat()),
                    lerp(minOffset.y, maxOffset.y, random.nextFloat())
            ));
        }

        @Override
        protected Class<?> fieldType() {
            return Vector2f.class;
        }
    }
    public static class FloatValue extends ShakeApplier<Float> {
        public FloatValue(Applier<Float> minOffset, Applier<Float> maxOffset) {
            super(minOffset, maxOffset);
        }

        @Override
        public Float get(FWLStyle current, Float initialValue, int index, int length) {
            float progress = random.nextFloat();
            Float minOffset = this.minOffset.get(current, initialValue, index, length);
            Float maxOffset = this.maxOffset.get(current, initialValue, index, length);
            return initialValue + lerp(minOffset, maxOffset, progress);
        }

        @Override
        protected Class<?> fieldType() {
            return Float.class;
        }
    }
    @Override
    public String getType() {
        return "shake";
    }

    protected abstract Class<?> fieldType();

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

    public static JsonObject serialize(Applier<?> applier) {
        if (applier instanceof ShakeApplier<?> shake) {
            JsonObject shakeObject = new JsonObject();
            shakeObject.add("min", FWLSerializer.expressionToJson(shake.minOffset, shake.fieldType()));
            shakeObject.add("max", FWLSerializer.expressionToJson(shake.maxOffset, shake.fieldType()));
            return shakeObject;
        }
        throw new ClassCastException("Unable to cast %s to ShakeApplier".formatted(applier.getClass().getSimpleName()));
    }

    @SuppressWarnings("unchecked")
    public static Applier<?> deserialize(JsonElement element, Class<?> fieldClass) {
        JsonObject gradientObject = element.getAsJsonObject();
        Applier<?> min = FWLSerializer.parseExpression(gradientObject.get("min"), fieldClass);
        Applier<?> max = FWLSerializer.parseExpression(gradientObject.get("max"), fieldClass);
        if (fieldClass == Float.class) return shake((Applier<Float>) min, (Applier<Float>) max);
        else if (fieldClass == Vector2f.class) return shake2((Applier<Vector2f>) min, (Applier<Vector2f>) max);
        else if (fieldClass == Vector4f.class) return shake4((Applier<Vector4f>) min, (Applier<Vector4f>) max);
        throw new IllegalArgumentException("Unsupported field type: %s".formatted(fieldClass.getSimpleName()));
    }
}
