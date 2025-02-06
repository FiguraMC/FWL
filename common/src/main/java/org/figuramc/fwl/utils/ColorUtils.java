package org.figuramc.fwl.utils;

import net.minecraft.util.FastColor;

public class ColorUtils {
    public static int argb(int a, int r, int g, int b) {
        return FastColor.ARGB32.color(Math.max(0, a), Math.max(0, r), Math.max(0, g), Math.max(0, b));
    }


}
