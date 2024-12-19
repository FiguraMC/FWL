package org.figuramc.fwl.utils;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.Stack;

public class Scissors {
    private static final Stack<Rectangle> scissors = new Stack<>();

    /**
     * More accurate scissors, that uses floats. NOT COMPATIBLE WITH SCISSORS FUNCTION IN GuiGraphics.
     */
    public static void enableScissors(GuiGraphics graphics, float x, float y, float width, float height) {
        Rectangle scissor = new Rectangle(x, y, width, height).transformed(graphics.pose().last().pose());
        if (!scissors.isEmpty()) {
            scissor = Rectangle.intersection(scissor, scissors.peek());
        }
        scissors.push(scissor);
        applyScissors(graphics);
    }

    public static void disableScissors(GuiGraphics graphics) {
        scissors.pop();
        applyScissors(graphics);
    }

    private static void applyScissors(GuiGraphics graphics) {
        graphics.flush();
        if (!scissors.isEmpty()) {
            Rectangle area = scissors.peek();
            Window window = Minecraft.getInstance().getWindow();
            int i = window.getHeight();
            double d = window.getGuiScale();
            double e = area.left() * d;
            double f = i - area.bottom() * d;
            double g = area.width() * d;
            double h = area.height() * d;
            RenderSystem.enableScissor((int)e, (int)f, Math.max(0, (int)g), Math.max(0, (int)h));
        }
        else {
            RenderSystem.disableScissor();
        }
    }
}
