package org.figuramc.fwl.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.util.FastColor;
import org.joml.Vector4f;

public class ColorUtils {



    public static int argb(int a, int r, int g, int b) {
        return FastColor.ARGB32.color(Math.max(0, a), Math.max(0, r), Math.max(0, g), Math.max(0, b));
    }

    public static Vector4f fromArgb(int argb) {
        return new Vector4f(
                FastColor.ARGB32.red(argb) / 255f,
                FastColor.ARGB32.green(argb) / 255f,
                FastColor.ARGB32.blue(argb) / 255f,
                FastColor.ARGB32.alpha(argb) / 255f
        );
    }

    @SuppressWarnings("ConstantConditions")
    public static Vector4f fromString(String s) throws IllegalArgumentException {
        return switch (s) {
            case "black" -> fromArgb(ChatFormatting.BLACK.getColor());
            case "dark_blue" -> fromArgb(ChatFormatting.DARK_BLUE.getColor());
            case "dark_green" -> fromArgb(ChatFormatting.DARK_GREEN.getColor());
            case "dark_aqua" -> fromArgb(ChatFormatting.DARK_AQUA.getColor());
            case "dark_red" -> fromArgb(ChatFormatting.DARK_RED.getColor());
            case "dark_purple" -> fromArgb(ChatFormatting.DARK_PURPLE.getColor());
            case "gold" -> fromArgb(ChatFormatting.GOLD.getColor());
            case "gray" -> fromArgb(ChatFormatting.GRAY.getColor());
            case "dark_gray" -> fromArgb(ChatFormatting.DARK_GRAY.getColor());
            case "blue" -> fromArgb(ChatFormatting.BLUE.getColor());
            case "green" -> fromArgb(ChatFormatting.GREEN.getColor());
            case "aqua" -> fromArgb(ChatFormatting.AQUA.getColor());
            case "red" -> fromArgb(ChatFormatting.RED.getColor());
            case "light_purple" -> fromArgb(ChatFormatting.LIGHT_PURPLE.getColor());
            case "yellow" -> fromArgb(ChatFormatting.YELLOW.getColor());
            case "white" -> fromArgb(ChatFormatting.WHITE.getColor());
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
