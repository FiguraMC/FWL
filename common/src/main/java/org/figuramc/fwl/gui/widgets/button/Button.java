package org.figuramc.fwl.gui.widgets.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.figuramc.fwl.FWL;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.gui.widgets.descriptors.button.ButtonDescriptor;
import org.figuramc.fwl.utils.Rectangle;
import org.lwjgl.glfw.GLFW;

public abstract class Button implements FWLWidget {
    private boolean keyboardClick;
    private int clicked = -1;
    protected final ButtonDescriptor desc;
    private OnClick callback;

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

    @Override
    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        desc.setHovered(desc.mouseIn(mouseX, mouseY));
        desc.setHoverPos(mouseX, mouseY);
        FWL.peekTheme().renderButton(graphics, delta, desc);
        renderButton(graphics, mouseX, mouseY, delta);
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
