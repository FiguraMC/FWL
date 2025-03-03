package org.figuramc.fwl.gui.widgets.misc;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.text.components.AbstractComponent;
import org.figuramc.fwl.utils.Rectangle;
import org.figuramc.fwl.utils.RenderUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Label implements FWLWidget {
    private float x, y;

    private AbstractComponent text;

    private Integer color = null;
    private boolean shadow = false;

    public Label(float x, float y, @NotNull AbstractComponent text) {
        this.x = x;
        this.y = y;
        setText(text);
    }

    @Override
    public Rectangle boundaries() {
        float width = RenderUtils.textWidth(text);
        float height = RenderUtils.textHeight(text);
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        RenderUtils.renderText(graphics, text, x, y, 0);
        if (boundaries().pointIn(mouseX, mouseY))
            RenderUtils.renderTextTooltip(graphics, text, x, y, mouseX, mouseY);
    }

    @Override
    public void tick() {

    }

    public float x() {
        return x;
    }

    public Label setX(float x) {
        this.x = x;
        return this;
    }

    public float y() {
        return y;
    }

    public Label setY(float y) {
        this.y = y;
        return this;
    }

    public AbstractComponent text() {
        return text;
    }

    public Label setText(AbstractComponent text) {
        this.text = Objects.requireNonNull(text, "Label text can't be null");
        return this;
    }

    public Integer color() {
        return color;
    }

    public Label setColor(Integer color) {
        this.color = color;
        return this;
    }

    public boolean shadow() {
        return shadow;
    }

    public Label setShadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }
}
