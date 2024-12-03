package org.figuramc.fwl.gui.widgets.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.figuramc.fwl.gui.themes.FWLTheme;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.joml.Vector2f;
import org.figuramc.fwl.FWL;
import org.figuramc.fwl.gui.widgets.descriptors.button.CheckboxDescriptor;
import org.lwjgl.glfw.GLFW;

public class Checkbox implements FWLWidget {
    private boolean keyboardClick;
    private int clicked = -1;
    private final CheckboxDescriptor desc;
    private ChangeCallback callback;

    public Checkbox(float x, float y, float width, float height, boolean checked) {
        this.desc = new CheckboxDescriptor(x,y,width,height,checked);
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

    public Checkbox setCallback(ChangeCallback callback) {
        this.callback = callback;
        return this;
    }

    public ChangeCallback callback() {
        return callback;
    }

    public boolean checked() {
        return desc.checked();
    }

    public void setChecked(boolean checked) {
        if (checked != desc.checked()) {
            desc.setChecked(checked);
            if (callback != null) callback.onValueChange(true);
        }
    }

    @Override
    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        desc.setHovered(desc.mouseIn(mouseX, mouseY));
        desc.setHoverPos(mouseX, mouseY);
        FWLTheme theme = FWL.peekTheme();
        theme.renderCheckbox(graphics, delta, desc);
    }

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
            if (!desc.disabled()) setChecked(!checked());
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
            if (desc.mouseIn(x, y)) setChecked(!checked());
            return true;
        }
        return false;
    }

    @Override
    public ScreenRectangle getRectangle() {
        return desc.getRectangle();
    }

    public interface ChangeCallback {
        void onValueChange(boolean checked);
    }
}
