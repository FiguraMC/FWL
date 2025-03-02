package org.figuramc.fwl.gui.widgets.containers;

import net.minecraft.client.gui.components.events.GuiEventListener;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.figuramc.fwl.utils.ArrayListUtils.sortedAdd;

public abstract class AbstractFWLContainerWidget implements FWLContainerWidget {
    private FWLWidget focused;
    private boolean dragging;

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
    public Iterator<FWLWidget> interactableWidgets() {
        return interactableWidgets.iterator();
    }

    @Override
    public Iterator<FWLWidget> renderableWidgets() {
        return renderableWidgets.iterator();
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

    @Override
    public void setFocused(@Nullable GuiEventListener child) {
        if (focused != child && child instanceof FWLWidget widget) {
            if (focused != null) focused.setFocused(false);
            widget.setFocused(true);
            focused = widget;
        }
    }

    public FWLWidget getFocused() {
        return focused;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    @Override
    public boolean isDragging() {
        return dragging;
    }

    public void addWidget(FWLWidget widget) {
        if (widget == null) return;
        if (lock) {
            awaitingAdd.add(widget);
            return;
        }
        sortedAdd(interactableWidgets, widget, FWLWidget::compareInteractionPriority);
        sortedAdd(renderableWidgets, widget, FWLWidget::compareRenderPriority);
    }

    public void removeWidget(FWLWidget widget) {
        if (widget == null) return;
        if (lock) {
            awaitingRemove.add(widget);
            return;
        }
        interactableWidgets.remove(widget);
        renderableWidgets.remove(widget);
    }

    public void lock() {
        lock = true;
    }

    public void unlock() {
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

    public Vector2d getInteractionMousePos(double mouseX, double mouseY) {
        return new Vector2d(mouseX, mouseY);
    }
}
