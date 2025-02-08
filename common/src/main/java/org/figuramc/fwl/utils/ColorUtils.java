package org.figuramc.fwl.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.util.FastColor;
import org.joml.Vector4f;

public class ColorUtils {



    public static int argb(int a, int r, int g, int b) {
        return FastColor.ARGB32.color(Math.max(0, a), Math.max(0, r), Math.max(0, g), Math.max(0, b));
    }

    public static Vector4f fromRgb(int argb) {
        return new Vector4f(
                FastColor.ARGB32.red(argb) / 255f,
                FastColor.ARGB32.green(argb) / 255f,
                FastColor.ARGB32.blue(argb) / 255f,
                1f
        );
    }

    @SuppressWarnings("ConstantConditions")
    public static Vector4f fromString(String s) throws IllegalArgumentException {
        return switch (s) {
            case "black" -> fromRgb(ChatFormatting.BLACK.getColor());
            case "dark_blue" -> fromRgb(ChatFormatting.DARK_BLUE.getColor());
            case "dark_green" -> fromRgb(ChatFormatting.DARK_GREEN.getColor());
            case "dark_aqua" -> fromRgb(ChatFormatting.DARK_AQUA.getColor());
            case "dark_red" -> fromRgb(ChatFormatting.DARK_RED.getColor());
            case "dark_purple" -> fromRgb(ChatFormatting.DARK_PURPLE.getColor());
            case "gold" -> fromRgb(ChatFormatting.GOLD.getColor());
            case "gray" -> fromRgb(ChatFormatting.GRAY.getColor());
            case "dark_gray" -> fromRgb(ChatFormatting.DARK_GRAY.getColor());
            case "blue" -> fromRgb(ChatFormatting.BLUE.getColor());
            case "green" -> fromRgb(ChatFormatting.GREEN.getColor());
            case "aqua" -> fromRgb(ChatFormatting.AQUA.getColor());
            case "red" -> fromRgb(ChatFormatting.RED.getColor());
            case "light_purple" -> fromRgb(ChatFormatting.LIGHT_PURPLE.getColor());
            case "yellow" -> fromRgb(ChatFormatting.YELLOW.getColor());
            case "white" -> fromRgb(ChatFormatting.WHITE.getColor());
            default -> {
                if (s.charAt(0) != '#')
                    throw new IllegalArgumentException("Unknown color: \"" + s + "\"");
                try {
                    yield switch (s.length()) {
                        case 4 -> new Vector4f(
                                Integer.parseInt(s, 1, 2, 16) / 15f,
                                Integer.parseInt(s, 2, 3, 16) / 15f,
                                Integer.parseInt(s, 3, 4, 16) / 15f,
                                1f
                        );
                        case 5 -> new Vector4f(
                                Integer.parseInt(s, 1, 2, 16) / 15f,
                                Integer.parseInt(s, 2, 3, 16) / 15f,
                                Integer.parseInt(s, 3, 4, 16) / 15f,
                                Integer.parseInt(s, 4, 5, 16) / 15f
                        );
                        case 7 -> new Vector4f(
                                Integer.parseInt(s, 1, 3, 16) / 255f,
                                Integer.parseInt(s, 3, 5, 16) / 255f,
                                Integer.parseInt(s, 5, 7, 16) / 255f,
                                1f
                        );
                        case 9 -> new Vector4f(
                                Integer.parseInt(s, 1, 3, 16) / 255f,
                                Integer.parseInt(s, 3, 5, 16) / 255f,
                                Integer.parseInt(s, 5, 7, 16) / 255f,
                                Integer.parseInt(s, 7, 9, 16) / 255f
                        );
                        default -> throw new IllegalArgumentException("Invalid hex color: \"" + s + "\"");
                    };
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Invalid hex color: \"" + s + "\"");
                }
            }
        };
    }


}
