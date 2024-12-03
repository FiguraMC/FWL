package org.figuramc.fwl.utils;

public class MathUtils {
    public static float clamp(float v, float min, float max) {
        return Math.max(Math.min(v, max), min);
    }

    public static double clamp(double v, double min, double max) {
        return Math.max(Math.min(v, max), min);
    }
}
