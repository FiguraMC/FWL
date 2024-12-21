package org.figuramc.fwl.gui.widgets.containers;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2d;

import java.util.Iterator;
import java.util.List;

public interface FWLContainerWidget extends FWLWidget, ContainerEventHandler {
    void addWidget(FWLWidget widget);
    void removeWidget(FWLWidget widget);

    void lock();
    void unlock();

    Iterator<FWLWidget> renderableWidgets();
    Iterator<FWLWidget> interactableWidgets();

    @Override
    List<FWLWidget> children();

    @Nullable
    @Override
    FWLWidget getFocused();

    void setFocused(@Nullable FWLWidget child);

    @NotNull
    default Vector2d getInteractionMousePos(double mouseX, double mouseY) {
        return new Vector2d(mouseX, mouseY);
    }

    @Override
    default void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        lock();
        Iterator<FWLWidget> widgets = renderableWidgets();
        while (widgets.hasNext())
            widgets.next().render(graphics, mouseX, mouseY, delta);
        unlock();
    }

    @Override
    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY)) return false;
        Vector2d mouse = getInteractionMousePos(mouseX, mouseY);
        lock();
        Iterator<FWLWidget> widgets = interactableWidgets();
        boolean result = false;
        while (widgets.hasNext()) {
            FWLWidget widget = widgets.next();
            if (widget.mouseClicked(mouse.x, mouse.y, button)) {
                setFocused(widget);
                result = true; break;
            }
        }
        if (!result) setFocused(null);
        unlock();
        return result;
    }

    @Override
    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        Vector2d mouse = getInteractionMousePos(mouseX, mouseY);
        lock();
        Iterator<FWLWidget> widgets = interactableWidgets();
        boolean result = false;
        while (widgets.hasNext()) {
            FWLWidget widget = widgets.next();
            if (widget.mouseReleased(mouse.x, mouse.y, button)) {
                result = true; break;
            }
        }
        unlock();
        return result;
    }

    @Override
    default boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        lock();
        Vector2d mouse = getInteractionMousePos(mouseX, mouseY);
        Iterator<FWLWidget> widgets = interactableWidgets();
        boolean result = false;
        while (widgets.hasNext()) {
            FWLWidget widget = widgets.next();
            if (widget.mouseDragged(mouse.x, mouse.y, button, deltaX, deltaY)) {
                result = true; break;
            }
        }
        unlock();
        return result;
    }

    @Override
    default boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (!isMouseOver(mouseX, mouseY)) return false;
        Vector2d mouse = getInteractionMousePos(mouseX, mouseY);
        lock();
        Iterator<FWLWidget> widgets = interactableWidgets();
        boolean result = false;
        while (widgets.hasNext()) {
            FWLWidget widget = widgets.next();
            if (widget.mouseScrolled(mouse.x, mouse.y, amount)) {
                result = true; break;
            }
        }
        unlock();
        return result;
    }

    @Override
    default void mouseMoved(double mouseX, double mouseY) {
        lock();
        Vector2d mouse = getInteractionMousePos(mouseX, mouseY);
        Iterator<FWLWidget> widgets = interactableWidgets();
        while(widgets.hasNext()) widgets.next().mouseMoved(mouse.x, mouse.y);
        unlock();
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
        lock();
        Iterator<FWLWidget> widgets = interactableWidgets();
        while (widgets.hasNext()) widgets.next().tick();
        unlock();
    }
}
