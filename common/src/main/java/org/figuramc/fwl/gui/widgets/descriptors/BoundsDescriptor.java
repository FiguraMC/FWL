package org.figuramc.fwl.gui.widgets.descriptors;

import org.figuramc.fwl.utils.Rectangle;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BoundsDescriptor extends ClickableDescriptor {
    public boolean top = true, bottom = true, left = true, right = true;
    public boolean topInner = false, bottomInner = false, leftInner = false, rightInner = false;

    private Rectangle widgetBounds;
    private String widgetType;

    public BoundsDescriptor(float x, float y, float width, float height, @NotNull String widgetType, float wX, float wY, float wWidth, float wHeight) {
        super(x, y, width, height);
        setWidgetType(widgetType);
        setWidgetBounds(wX, wY, wWidth, wHeight);
    }

    public BoundsDescriptor(float x, float y, float width, float height, @NotNull String widgetType, Rectangle wBounds) {
        this(x, y, width, height, widgetType, wBounds.x(), wBounds.y(), wBounds.width(), wBounds.height());
    }


    public BoundsDescriptor(float x, float y, float width, float height, FWLDescriptor parent) {
        this(x, y, width, height, parent.widgetType(), parent.boundaries());
    }

    public BoundsDescriptor(float x, float y, float width, float height) {
        this(x, y, width, height, "unknown", x, y, width, height);
    }

    public BoundsDescriptor setWidgetType(@NotNull String widgetType) {
        this.widgetType = Objects.requireNonNull(widgetType, "Widget type can't be null");
        return this;
    }

    @Override
    public String widgetType() {
        return widgetType;
    }

    public BoundsDescriptor setWidgetBounds(float x, float y, float width, float height) {
        widgetBounds = new Rectangle(x, y, width, height);
        return this;
    }

    public BoundsDescriptor setWidgetBounds(Rectangle bounds) {
        widgetBounds = bounds;
        return this;
    }

    public BoundsDescriptor setBounds(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return setWidgetBounds(x, y, width, height);
    }

    public BoundsDescriptor setBounds(Rectangle bounds) {
        this.x = bounds.x();
        this.y = bounds.y();
        this.width = bounds.width();
        this.height = bounds.height();
        return setWidgetBounds(bounds);
    }

    public Rectangle widgetBounds() {
        return widgetBounds;
    }

    public boolean isPresent(Side side) {
        return switch (side) {
            case TOP -> top;
            case BOTTOM -> bottom;
            case LEFT -> left;
            case RIGHT -> right;
        };
    }

    public boolean isInner(Side side) {
        return switch (side) {
            case TOP -> topInner;
            case BOTTOM -> bottomInner;
            case LEFT -> leftInner;
            case RIGHT -> rightInner;
        };
    }

    public BoundsDescriptor setEnabled(Side side, boolean state) {
        switch (side) {
            case TOP -> top = state;
            case BOTTOM -> bottom = state;
            case LEFT -> left = state;
            case RIGHT -> right = state;
        }
        return this;
    }

    public BoundsDescriptor setInner(Side side, boolean state) {
        switch (side) {
            case TOP -> topInner = state;
            case BOTTOM -> bottomInner = state;
            case LEFT -> leftInner = state;
            case RIGHT -> rightInner = state;
        }
        return this;
    }

    public enum Side {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT;
    }
}
