package org.figuramc.fwl.gui.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface FWLContainerWidget extends FWLWidget, ContainerEventHandler {
    @NotNull
    List<FWLWidget> children();

    void setFocused(FWLWidget widget);
    FWLWidget getFocused();

    default void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        for (FWLWidget widget: children()) widget.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (FWLWidget widget: children()) {
            if (widget.mouseClicked(mouseX, mouseY, button)) {
                setFocused(widget);
                return true;
            }
        }
        setFocused(null);
        return false;
    }

    @Override
    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (FWLWidget widget: children()) {
            if (widget.mouseReleased(mouseX, mouseY, button)) return true;
        }
        return false;
    }

    @Override
    default boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (FWLWidget widget: children()) {
            if (widget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) return true;
        }
        return false;
    }

    @Override
    default boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        for (FWLWidget widget: children()) {
            if (widget.mouseScrolled(mouseX, mouseY, amount)) return true;
        }
        return false;
    }

    @Override
    default void mouseMoved(double mouseX, double mouseY) {
        for (FWLWidget widget: children()) widget.mouseMoved(mouseX, mouseY);
    }

    @Override
    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        FWLWidget widget = getFocused();
        if (widget != null) return widget.keyPressed(keyCode, scanCode, modifiers);
        return false;
    }

    @Override
    default boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        FWLWidget widget = getFocused();
        if (widget != null) return widget.keyReleased(keyCode, scanCode, modifiers);
        return false;
    }

    @Override
    default boolean charTyped(char chr, int modifiers) {
        FWLWidget widget = getFocused();
        if (widget != null) return widget.charTyped(chr, modifiers);
        return false;
    }

    @Override
    default void tick() {
        for (FWLWidget widget: children()) widget.tick();
    }
}
