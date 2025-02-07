package org.figuramc.fwl.utils;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MathUtils {
    public static int clamp(int v, int min, int max) {
        return Math.max(Math.min(v, max), min);
    }

    public static float clamp(float v, float min, float max) {
        return Math.max(Math.min(v, max), min);
    }

    public static double clamp(double v, double min, double max) {
        return Math.max(Math.min(v, max), min);
    }

    public static int lerp(int a, int b, float v) {
        return (int)(a + v * (b - a));
    }

    public static float lerp(float a, float b, float v) {
        return a + v * (b - a);
    }

    public static double lerp(double a, double b, double v) {
        return a + v * (b - a);
    }

    public static Vector2f lerp(Vector2f a, Vector2f b, float v) {
        return new Vector2f(
                lerp(a.x, b.x, v),
                lerp(a.y, b.y, v)
        );
    }

    public static Vector3f lerp(Vector3f a, Vector3f b, float v) {
        return new Vector3f(
                lerp(a.x, b.x, v),
                lerp(a.y, b.y, v),
                lerp(a.z, b.z, v)
        );
    }

    public static Vector4f lerp(Vector4f a, Vector4f b, float v) {
        return new Vector4f(
                lerp(a.x, b.x, v),
                lerp(a.y, b.y, v),
                lerp(a.z, b.z, v),
                lerp(a.w, b.w, v)
        );
    }

    public static float rlerp(int a, int b, float v) {
        return (v - a) / (b - a);
    }

    public static float rlerp(float a, float b, float v) {
        return (v - a) / (b - a);
    }

    public static double rlerp(double a, double b, double v) {
        return (v - a) / (b - a);
    }

    public static int wrapModulo(int a, int b) {
        int r = a % b;
        return r < 0 ? b + r : r;
    }
}
