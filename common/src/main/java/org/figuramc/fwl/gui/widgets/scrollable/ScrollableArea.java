package org.figuramc.fwl.gui.widgets.scrollable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.figuramc.fwl.gui.themes.FWLTheme;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.utils.Rectangle;
import org.figuramc.fwl.utils.Scissors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ScrollableArea extends ScrollableWidget {
    private float x, y, width, height;
    private float offsetX, offsetY;
    private FWLWidget focused;
    private boolean dragging;

    public ScrollableArea(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        Scissors.enableScissors(graphics, x, y, width, height);
        super.render(graphics, mouseX, mouseY, delta);
        Scissors.disableScissors(graphics);
    }

    @Override
    public void addWidget(FWLWidget widget) {
        super.addWidget(widget);
    }

    @Override
    public float offsetX() {
        return offsetX;
    }

    @Override
    public float offsetY() {
        return offsetY;
    }

    public ScrollableArea setOffsetX(float offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    public ScrollableArea setOffsetY(float offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    @Override
    public boolean isDragging() {
        return dragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener child) {
        if (child instanceof FWLWidget fwl) setFocused(fwl);
    }

    public float x() {
        return x;
    }

    public ScrollableArea setX(float x) {
        this.x = x;
        return this;
    }

    public float y() {
        return y;
    }

    public ScrollableArea setY(float y) {
        this.y = y;
        return this;
    }

    public float width() {
        return width;
    }

    public ScrollableArea setWidth(float width) {
        this.width = width;
        return this;
    }

    public float height() {
        return height;
    }

    public ScrollableArea setHeight(float height) {
        this.height = height;
        return this;
    }

    @Override
    public Rectangle boundaries() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= x && mouseY >= y &&
                mouseX <= x + width && mouseY <= y + height;
    }
}
