package org.lexize.lwl.widgets.descriptors;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

public class ButtonDescriptor {
    private int stateTicks;
    private WidgetState state;

    private float x, y, width, height;
    private @Nullable Vector2f clickPos;
    private ResourceLocation type;

    public ButtonDescriptor(float x, float y, float width, float height) {
        this(x,y,width,height, null, null, null, 0);
    }

    public ButtonDescriptor(float x, float y, float width, float height, @Nullable ResourceLocation type, @Nullable Vector2f clickPos, @Nullable WidgetState buttonState, int stateTicks) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type != null ? type : ButtonTypes.PRIMARY;
        this.clickPos = clickPos;
        this.state = buttonState != null ? buttonState : WidgetState.DEFAULT;
        this.stateTicks = stateTicks;
    }

    public int stateTicks() {
        return stateTicks;
    }

    public ButtonDescriptor setStateTicks(int stateTicks) {
        this.stateTicks = stateTicks;
        return this;
    }

    public WidgetState state() {
        return state;
    }

    public ButtonDescriptor setState(WidgetState state) {
        this.state = state;
        return this;
    }

    public float x() {
        return x;
    }

    public ButtonDescriptor setX(float x) {
        this.x = x;
        return this;
    }

    public float y() {
        return y;
    }

    public ButtonDescriptor setY(float y) {
        this.y = y;
        return this;
    }

    public float width() {
        return width;
    }

    public ButtonDescriptor setWidth(float width) {
        this.width = width;
        return this;
    }

    public float height() {
        return height;
    }

    public ButtonDescriptor setHeight(float height) {
        this.height = height;
        return this;
    }

    public @Nullable Vector2f clickPos() {
        return clickPos;
    }

    public ButtonDescriptor setClickPos(@Nullable Vector2f clickPos) {
        this.clickPos = clickPos;
        return this;
    }

    public ResourceLocation type() {
        return type;
    }

    public ButtonDescriptor setType(ResourceLocation type) {
        this.type = type;
        return this;
    }

    public boolean mouseIn(float x, float y) {
        return x >= this.x && x <= this.x + width &&
                y >= this.y && y <= this.y + height;
    }
}
