package org.figuramc.fwl.gui.widgets;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import org.figuramc.fwl.utils.Rectangle;
import org.lwjgl.glfw.GLFW;

import static com.mojang.blaze3d.platform.InputConstants.isKeyDown;

public interface FWLWidget extends GuiEventListener, Renderable, Tickable, NarratableEntry {
    @Override
    default NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    default void updateNarration(NarrationElementOutput builder) {

    }

    default int interactionPriority() {
        return 0;
    }

    default FWLWidget setInteractionPriority(int priority) {
        return this;
    }

    default int compareInteractionPriority(FWLWidget b) {
        return interactionPriority() - b.interactionPriority();
    }

    static int getCurrentModifiers() {
        long window = Minecraft.getInstance().getWindow().getWindow();
        int mod = (isKeyDown(window, GLFW.GLFW_KEY_LEFT_SHIFT) || isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SHIFT)) ? GLFW.GLFW_MOD_SHIFT : 0;
        mod |= (isKeyDown(window, GLFW.GLFW_KEY_LEFT_CONTROL) || isKeyDown(window, GLFW.GLFW_KEY_RIGHT_CONTROL)) ? GLFW.GLFW_MOD_CONTROL : 0;
        mod |= (isKeyDown(window, GLFW.GLFW_KEY_LEFT_ALT) || isKeyDown(window, GLFW.GLFW_KEY_RIGHT_ALT)) ? GLFW.GLFW_MOD_ALT : 0;
        mod |= (isKeyDown(window, GLFW.GLFW_KEY_LEFT_SUPER) || isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SUPER)) ? GLFW.GLFW_MOD_SUPER : 0;
        return mod;
    }

    static boolean isShiftDown() {
        long window = Minecraft.getInstance().getWindow().getWindow();
        return isKeyDown(window, GLFW.GLFW_KEY_LEFT_SHIFT) || isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    static boolean isAltDown() {
        long window = Minecraft.getInstance().getWindow().getWindow();
        return isKeyDown(window, GLFW.GLFW_KEY_LEFT_ALT) || isKeyDown(window, GLFW.GLFW_KEY_RIGHT_ALT);
    }

    static void setClipboard(String value) {
        Minecraft.getInstance().keyboardHandler.setClipboard(value);
    }

    static String getClipboard() {
        return Minecraft.getInstance().keyboardHandler.getClipboard();
    }

    @Override
    default boolean isMouseOver(double mouseX, double mouseY) {
        return boundaries().pointIn(mouseX, mouseY);
    }

    Rectangle boundaries();
}
