package org.figuramc.fwl.gui.widgets.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import org.figuramc.fwl.FWL;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.gui.widgets.descriptors.ClickableDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.button.ButtonDescriptor;
import org.figuramc.fwl.text.components.AbstractComponent;
import org.figuramc.fwl.utils.Rectangle;
import org.figuramc.fwl.utils.RenderUtils;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import static org.figuramc.fwl.FWL.fwl;

public abstract class Button implements FWLWidget {
    private boolean keyboardClick;
    private int clicked = -1;
    protected final ButtonDescriptor desc;
    private OnClick callback;

    private @Nullable AbstractComponent tooltip;

    public Button(float x, float y, float width, float height) {
        desc = new ButtonDescriptor(x, y, width, height);
    }

    @Override
    public void setFocused(boolean focused) {
        desc.setFocused(focused);
    }

    @Override
    public boolean isFocused() {
        return desc.focused();
    }

    public void setEnabled(boolean enabled) {
        desc.setDisabled(!enabled);
    }

    public boolean enabled() {
        return !desc.disabled();
    }

    public OnClick callback() {
        return callback;
    }

    public Button setCallback(OnClick callback) {
        this.callback = callback;
        return this;
    }

    public @Nullable AbstractComponent tooltip() {
        return tooltip;
    }

    public Button setTooltip(@Nullable AbstractComponent tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public float x() {
        return desc.x();
    }

    public Button setX(float x) {
        desc.setX(x);
        return this;
    }

    public float y() {
        return desc.y();
    }

    public Button setY(float y) {
        desc.setY(y);
        return this;
    }

    public float width() {
        return desc.width();
    }

    public Button setWidth(float width) {
        desc.setWidth(width);
        return this;
    }

    public float height() {
        return desc.height();
    }

    public Button setHeight(float height) {
        desc.setHeight(height);
        return this;
    }

    @Override
    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        boolean hovered = desc.mouseIn(mouseX, mouseY);
        desc.setHovered(hovered);
        if (hovered) desc.setHoverPos(mouseX, mouseY);
        fwl().currentTheme().renderButton(graphics, delta, desc);
        renderButton(graphics, mouseX, mouseY, delta);
        if (tooltip != null && hovered) RenderUtils.renderTooltip(graphics, tooltip, mouseX, mouseY);
    }

    public abstract void renderButton(GuiGraphics graphics, float mouseX, float mouseY, float delta);

    @Override
    public void tick() {
        desc.tick();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (isFocused()) {
            if (clicked == -1 && keyCode == GLFW.GLFW_KEY_SPACE || keyCode == GLFW.GLFW_KEY_ENTER) {
                clicked = keyCode;
                keyboardClick = true;
                desc.setClicked(true);
                float x = desc.x() + (desc.width() / 2), y = desc.y() + (desc.height() / 2);
                desc.setClickPos(x, y);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyboardClick && clicked == keyCode) {
            desc.setClicked(false);
            clicked = -1;
            if (!desc.disabled() && callback != null) {
                int button = modifiers == GLFW.GLFW_MOD_SHIFT ? GLFW.GLFW_MOUSE_BUTTON_RIGHT : GLFW.GLFW_MOUSE_BUTTON_LEFT;
                float x = desc.x() + (desc.width() / 2), y = desc.y() + (desc.height() / 2);
                callback.onClick(x, y, button);
            }
            keyboardClick = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float x = (float) mouseX, y = (float) mouseY;
        if (desc.mouseIn(x, y)) {
            if (!desc.disabled() && !keyboardClick && clicked == -1) {
                clicked = button;
                desc.setClicked(true);
                desc.setClickPos(x, y);
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        float x = (float) mouseX, y = (float) mouseY;
        if (!desc.disabled() && !keyboardClick && clicked == button) {
            desc.setClicked(false);
            clicked = -1;
            if (desc.mouseIn(x, y) && callback != null) callback.onClick(x, y, button);
            return true;
        }
        return false;
    }

    @Override
    public ScreenRectangle getRectangle() {
        return desc.screenRectangle();
    }

    @Override
    public Rectangle boundaries() {
        return desc.boundaries();
    }

    public interface OnClick {
        void onClick(float x, float y, int button);
    }
}
