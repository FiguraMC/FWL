package org.figuramc.fwl.text.effects;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.serialization.FWLSerializer;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.figuramc.fwl.text.effects.ConstantApplier.constant;
import static org.figuramc.fwl.utils.MathUtils.lerp;

public class GradientApplier {

    public static class Vec4 implements Applier<Vector4f> {
        private final Applier<Vector4f> to;

        public Vec4(Applier<Vector4f> to) {
            this.to = to;
        }

        @Override
        public Vector4f get(FWLStyle current, Vector4f from, int index, int length) {
            float progress = (float) index / length;
            Vector4f to = this.to.get(current, from, index, length);
            return lerp(from, to, progress);
        }

        @Override
        public String getType() {
            return "gradient";
        }
    }

    public static class Vec2 implements Applier<Vector2f> {
        private final Applier<Vector2f> to;

        public Vec2(Applier<Vector2f> to) {
            this.to = to;
        }

        @Override
        public Vector2f get(FWLStyle current, Vector2f from, int index, int length) {
            float progress = (float) index / length;
            Vector2f to = this.to.get(current, from, index, length);
            return lerp(from, to, progress);
        }

        @Override
        public String getType() {
            return "gradient";
        }
    }

    public static class FloatValue implements Applier<Float> {
        private final Applier<Float> to;

        public FloatValue(Applier<Float> to) {
            this.to = to;
        }

        @Override
        public Float get(FWLStyle current, Float from, int index, int length) {
            float progress = (float) index / length;
            float to = this.to.get(current, from, index, length);
            return lerp(from, to, progress);
        }

        @Override
        public String getType() {
            return "gradient";
        }
    }

    public static Vec4 gradient4(Applier<Vector4f> to) {
        return new Vec4(to);
    }

    public static Vec4 gradient4(float r, float g, float b, float a) {
        return new Vec4(constant(r, g, b, a));
    }

    public static Vec4 gradient4(Vector4f color) {
        return new Vec4(constant(color));
    }

    public static Vec2 gradient2(Applier<Vector2f> to) {
        return new Vec2(to);
    }

    public static Vec2 gradient2(float x, float y) {
        return new Vec2(constant(x, y));
    }

    public static Vec2 gradient2(Vector2f vec) {
        return new Vec2(constant(vec));
    }

    public static FloatValue gradient(Applier<Float> to) {
        return new FloatValue(to);
    }

    public static FloatValue gradient(float val) {
        return new FloatValue(constant(val));
    }

    @SuppressWarnings("unchecked")
    public static Applier<?> deserialize(JsonElement element, Class<?> fieldClass) {
        JsonObject gradientObject = element.getAsJsonObject();
        Applier<?> to = FWLSerializer.parseExpression(gradientObject.get("to"), fieldClass);
        if (fieldClass == Float.class) return gradient((Applier<Float>) to);
        else if (fieldClass == Vector2f.class) return gradient2((Applier<Vector2f>) to);
        else if (fieldClass == Vector4f.class) return gradient4((Applier<Vector4f>) to);
        throw new IllegalArgumentException("Unsupported field type: %s".formatted(fieldClass.getSimpleName()));
    }
}
