package org.figuramc.fwl.utils;

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
