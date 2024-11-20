package org.lexize.lwl.gui.widgets.descriptors;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

public class RadioButtonDescriptor {
    private int stateTicks;
    private WidgetState buttonState;

    private float x, y, width, height;
    private @Nullable Vector2f clickPos;
    private boolean selected;

    public RadioButtonDescriptor(float x, float y, float width, float height, boolean selected) {
        this(x,y,width,height, selected, null, null, 0);
    }

    public RadioButtonDescriptor(float x, float y, float width, float height, boolean selected, @Nullable Vector2f clickPos, @Nullable WidgetState buttonState, int stateTicks) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.selected = selected;
        this.clickPos = clickPos;
        this.buttonState = buttonState != null ? buttonState : WidgetState.DEFAULT;
        this.stateTicks = stateTicks;
    }

    public int stateTicks() {
        return stateTicks;
    }

    public RadioButtonDescriptor setStateTicks(int stateTicks) {
        this.stateTicks = stateTicks;
        return this;
    }

    public WidgetState state() {
        return buttonState;
    }

    public RadioButtonDescriptor setState(WidgetState widgetState) {
        this.buttonState = widgetState;
        return this;
    }

    public float x() {
        return x;
    }

    public RadioButtonDescriptor setX(float x) {
        this.x = x;
        return this;
    }

    public float y() {
        return y;
    }

    public RadioButtonDescriptor setY(float y) {
        this.y = y;
        return this;
    }

    public float width() {
        return width;
    }

    public RadioButtonDescriptor setWidth(float width) {
        this.width = width;
        return this;
    }

    public float height() {
        return height;
    }

    public RadioButtonDescriptor setHeight(float height) {
        this.height = height;
        return this;
    }

    public @Nullable Vector2f clickPos() {
        return clickPos;
    }

    public RadioButtonDescriptor setClickPos(@Nullable Vector2f clickPos) {
        this.clickPos = clickPos;
        return this;
    }

    public boolean selected() {
        return selected;
    }

    public RadioButtonDescriptor setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }
}
