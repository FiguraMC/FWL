package org.lexize.lwl.gui.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.joml.Vector2f;
import org.lexize.lwl.LWL;
import org.lexize.lwl.gui.themes.LWLTheme;
import org.lexize.lwl.gui.widgets.descriptors.CheckboxDescriptor;
import org.lwjgl.glfw.GLFW;

public class Checkbox implements LWLWidget {
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

    @Override
    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        desc.setHovered(desc.mouseIn(mouseX, mouseY));
        LWLTheme theme = LWL.peekTheme();
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
                desc.setClickPos(new Vector2f(x, y));
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
            if (!desc.disabled()) {
                desc.setChecked(!desc.checked());
                if (callback != null) callback.onValueChange(desc.checked());
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
                desc.setClickPos(new Vector2f(x, y));
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
            if (desc.mouseIn(x, y)) {
                desc.setChecked(!desc.checked());
                if (callback != null) callback.onValueChange(desc.checked());
            }
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
