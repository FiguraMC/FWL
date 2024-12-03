package org.figuramc.fwl.gui.widgets;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import org.lwjgl.glfw.GLFW;

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

    default boolean hasShiftDown() {
        long window = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    default boolean hasAltDown() {
        long window = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_ALT) || InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_ALT);
    }
}
