package org.figuramc.fwl.gui.widgets.input;

import net.minecraft.client.gui.GuiGraphics;
import org.figuramc.fwl.FWL;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.gui.widgets.descriptors.Orientation;
import org.figuramc.fwl.gui.widgets.descriptors.input.SliderDescriptor;
import org.figuramc.fwl.utils.Rectangle;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

import static org.figuramc.fwl.utils.MathUtils.clamp;
import static org.figuramc.fwl.utils.MathUtils.rlerp;

public class Slider implements FWLWidget {
    private final SliderDescriptor desc;

    private Consumer<Float> callback;
    private boolean useCallback = true;
    private boolean updateOnMove = false;

    public Slider(float x, float y, float width, float height, float progress, @Nullable Orientation orientation) {
        desc = new SliderDescriptor(x, y, width, height, progress, orientation);
    }

    public Slider(float x, float y, float width, float height, float progress) {
        this(x, y, width, height, progress, null);
    }

    public Slider(float x, float y, float width, float height) {
        this(x, y, width, height, 0);
    }

    @Override
    public Rectangle boundaries() {
        return desc.boundaries();
    }

    @Override
    public void setFocused(boolean focused) {
        desc.setFocused(focused);
    }

    @Override
    public boolean isFocused() {
        return desc.focused();
    }

    public Slider setHeight(float height) {
        desc.setHeight(height);
        return this;
    }

    public float height() {
        return desc.height();
    }

    public Slider setWidth(float width) {
        desc.setWidth(width);
        return this;
    }

    public float width() {
        return desc.width();
    }

    public Slider setY(float y) {
        desc.setY(y);
        return this;
    }

    public float y() {
        return desc.y();
    }

    public Slider setX(float x) {
        desc.setX(x);
        return this;
    }

    public float x() {
        return desc.x();
    }

    public Slider setSteps(int steps) {
        desc.setSteps(steps);
        return this;
    }

    public int steps() {
        return desc.steps();
    }

    public Slider setOrientation(Orientation orientation) {
        desc.setOrientation(orientation);
        return this;
    }

    public Orientation orientation() {
        return desc.orientation();
    }

    public Slider setProgress(float progress) {
        desc.setProgress(progress);
        if (callback != null && useCallback) callback.accept(progress);
        return this;
    }

    public float progress() {
        return desc.progress();
    }

    public Consumer<Float> callback() {
        return callback;
    }

    public Slider setCallback(Consumer<Float> callback) {
        this.callback = callback;
        return this;
    }

    public boolean isUsingCallback() {
        return useCallback;
    }

    public Slider setUseCallback(boolean update) {
        this.useCallback = update;
        return this;
    }

    public boolean updateOnRender() {
        return updateOnMove;
    }

    public Slider setUpdateOnMove(boolean updateOnMove) {
        this.updateOnMove = updateOnMove;
        return this;
    }

    private float relativeProgress(float mouseX, float mouseY) {
        Rectangle bounds = desc.boundaries();
        Orientation orient = desc.orientation();
        float mn = orient.switchBy(bounds.left(), bounds.top());
        float mx = orient.switchBy(bounds.right(), bounds.bottom());
        float v = orient.switchBy(mouseX, mouseY);
        return clamp(rlerp(mn, mx, v), 0, 1);
    }

    private float roundToSteps(float progress) {
        int steps = desc.steps() - 1;
        if (steps > 0 && !FWLWidget.isShiftDown()) return Math.round((progress * steps )) / (float) steps;
        return progress;
    }

    @Override
    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        boolean hovered = desc.mouseIn(mouseX, mouseY);
        desc.setHovered(hovered);
        if (hovered) desc.setHoverPos(mouseX, mouseY);
        FWL.fwl().currentTheme().renderSlider(graphics, delta, desc);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float mX = (float) mouseX, mY = (float) mouseY;
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && desc.mouseIn(mX, mY)) {
            float progress = roundToSteps(relativeProgress(mX, mY));
            desc.setClicked(true);
            desc.setClickPos(mX, mY);
            if (updateOnMove) setProgress(progress);
            else desc.setProgress(progress);
            return true;
        }
        return false;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        float mX = (float) mouseX, mY = (float) mouseY;
        if (desc.clicked()) {
            float progress = roundToSteps(relativeProgress(mX, mY));
            if (updateOnMove) setProgress(progress);
            else desc.setProgress(progress);
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (desc.clicked()) {
            float progress = roundToSteps(relativeProgress((float) mouseX, (float) mouseY));
            desc.setClicked(false);
            setProgress(progress);
            return true;
        }
        return false;
    }

    @Override
    public void tick() {

    }
}
