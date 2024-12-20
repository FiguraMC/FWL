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

    public static int wrapModulo(int a, int b) {
        int r = a % b;
        return r < 0 ? b + r : r;
    }
}
