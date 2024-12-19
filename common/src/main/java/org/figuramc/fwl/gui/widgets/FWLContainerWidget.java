package org.figuramc.fwl.gui.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.List;

import static org.figuramc.fwl.utils.ArrayListUtils.sortedAdd;

public abstract class FWLContainerWidget implements FWLWidget, ContainerEventHandler {
    private FWLWidget focused;


    private final ArrayList<FWLWidget> interactableWidgets = new ArrayList<>();
    private final ArrayList<FWLWidget> renderableWidgets = new ArrayList<>();

    // Widgets lock. In case if widgets is added/removed during any operation that requires iterating through main lists,
    // they are being added to awaitingAdd/awaitingRemoved list, that is being cleared when after unlock.
    private boolean lock;
    private final ArrayList<FWLWidget> awaitingAdd = new ArrayList<>();
    private final ArrayList<FWLWidget> awaitingRemove = new ArrayList<>();

    @NotNull
    public List<FWLWidget> children() {
        return interactableWidgets;
    }

    @Override
    public void setFocused(boolean focused) {
        if (!focused) setFocused(null);
    }

    public void setFocused(FWLWidget widget) {
        if (focused != widget) {
            if (focused != null) focused.setFocused(false);
            if (widget != null) widget.setFocused(true);
            focused = widget;
        }
    }

    public FWLWidget getFocused() {
        return focused;
    }

    protected void addWidget(FWLWidget widget) {
        if (lock) {
            awaitingAdd.add(widget);
            return;
        }
        sortedAdd(interactableWidgets, widget, FWLWidget::compareInteractionPriority);
        sortedAdd(renderableWidgets, widget, FWLWidget::compareRenderPriority);
    }

    protected void removeWidget(FWLWidget widget) {
        if (lock) {
            awaitingRemove.add(widget);
            return;
        }
        interactableWidgets.remove(widget);
        renderableWidgets.remove(widget);
    }

    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        lock();
        for (FWLWidget widget: renderableWidgets)
            widget.render(graphics, mouseX, mouseY, delta);
        unlock();
    }

    protected void lock() {
        lock = true;
    }

    protected void unlock() {
        lock = false;
        for (FWLWidget widget: awaitingAdd) {
            addWidget(widget);
        }

        for (FWLWidget widget: awaitingRemove) {
            removeWidget(widget);
        }

        awaitingAdd.clear();
        awaitingRemove.clear();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY)) return false;
        Vector2d mouse = getInteractionMousePos(mouseX, mouseY);
        lock();
        boolean result = false;
        for (FWLWidget widget: children()) {
            if (widget.mouseClicked(mouse.x, mouse.y, button)) {
                setFocused(widget);
                result = true; break;
            }
        }
        unlock();
        return result;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        Vector2d mouse = getInteractionMousePos(mouseX, mouseY);
        lock();
        boolean result = false;
        for (FWLWidget widget: children()) {
            if (widget.mouseReleased(mouse.x, mouse.y, button)) {
                result = true; break;
            }
        }
        unlock();
        return result;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        lock();
        Vector2d mouse = getInteractionMousePos(mouseX, mouseY);
        boolean result = false;
        for (FWLWidget widget: children()) {
            if (widget.mouseDragged(mouse.x, mouse.y, button, deltaX, deltaY)) {
                result = true; break;
            }
        }
        unlock();
        return result;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (!isMouseOver(mouseX, mouseY)) return false;
        Vector2d mouse = getInteractionMousePos(mouseX, mouseY);
        lock();
        boolean result = false;
        for (FWLWidget widget: children()) {
            if (widget.mouseScrolled(mouse.x, mouse.y, amount)) {
                result = true; break;
            }
        }
        unlock();
        return result;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        lock();
        Vector2d mouse = getInteractionMousePos(mouseX, mouseY);
        for (FWLWidget widget: children()) widget.mouseMoved(mouse.x, mouse.y);
        unlock();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        FWLWidget widget = getFocused();
        if (widget != null) return widget.keyPressed(keyCode, scanCode, modifiers);
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        FWLWidget widget = getFocused();
        if (widget != null) return widget.keyReleased(keyCode, scanCode, modifiers);
        return false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        FWLWidget widget = getFocused();
        if (widget != null) return widget.charTyped(chr, modifiers);
        return false;
    }

    @Override
    public void tick() {
        lock();
        for (FWLWidget widget: children()) widget.tick();
        unlock();
    }

    public Vector2d getInteractionMousePos(double mouseX, double mouseY) {
        return new Vector2d(mouseX, mouseY);
    }
}
