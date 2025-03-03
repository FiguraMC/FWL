package org.figuramc.fwl.gui.widgets.scrollable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.figuramc.fwl.FWL;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.gui.widgets.descriptors.ClickableDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.Orientation;
import org.figuramc.fwl.gui.widgets.descriptors.ScrollBarDescriptor;
import org.figuramc.fwl.utils.Rectangle;

import static org.figuramc.fwl.FWL.fwl;
import static org.figuramc.fwl.utils.MathUtils.clamp;

public class ScrollBar implements FWLWidget {
    // Implementation
    private final ScrollBarDescriptor desc;
    private float visibleAreaSize;
    private float contentSize;
    private float progress;
    private float scrollStep = 50;
    private Callback callback;

    public ScrollBar(float x, float y, float width, float height, float visibleAreaSize, float contentSize, float progress, Orientation barOrientation) {
        this.visibleAreaSize = visibleAreaSize;
        this.contentSize = contentSize;
        this.progress = progress;
        float coveredPartSize = Math.min(visibleAreaSize, contentSize) / contentSize;
        float descProgress = clamp(progress / (contentSize - visibleAreaSize), 0, 1);
        this.desc = new ScrollBarDescriptor(x, y, width, height, coveredPartSize, descProgress, barOrientation);
    }

    @Override
    public void setFocused(boolean focused) {
        desc.setFocused(focused);
    }

    @Override
    public boolean isFocused() {
        return desc.focused();
    }

    public Callback callback() {
        return callback;
    }

    public ScrollBar setCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    public float visibleAreaSize() {
        return visibleAreaSize;
    }

    public ScrollBar setVisibleAreaSize(float visibleAreaSize) {
        this.visibleAreaSize = visibleAreaSize;
        recalculateCoveredSize();
        return this;
    }

    public float contentSize() {
        return contentSize;
    }

    public ScrollBar setContentSize(float contentSize) {
        this.contentSize = contentSize;
        recalculateCoveredSize();
        return this;
    }

    public float progress() {
        return progress;
    }

    public float scrollStep() {
        return scrollStep;
    }

    public ScrollBar setScrollStep(float scrollStep) {
        this.scrollStep = scrollStep;
        return this;
    }

    public float progressPercentage() {
        return progress / scrollableArea();
    }

    public ScrollBar setProgress(float progress) {
        float prev = this.progress;
        this.progress = clamp(progress, 0, scrollableArea());
        if (prev != progress) {
            if (callback != null) callback.onValueChange(this.progress);
            desc.setProgress(progressPercentage());
        }
        return this;
    }

    private float coveredPartSize() {
        return Math.min(visibleAreaSize, contentSize) / contentSize;
    }

    private void recalculateCoveredSize() {
        desc.setCoveredPartSize(coveredPartSize());
    }

    public ScrollBar setHeight(float height) {
        desc.setHeight(height);
        return this;
    }

    public float height() {
        return desc.height();
    }

    public ScrollBar setWidth(float width) {
        desc.setWidth(width);
        return this;
    }

    public float width() {
        return desc.width();
    }

    public ScrollBar setY(float y) {
        desc.setY(y);
        return this;
    }

    public float y() {
        return desc.y();
    }

    public ScrollBar setX(float x) {
        desc.setX(x);
        return this;
    }

    public float x() {
        return desc.x();
    }

    private float scrollableArea() {
        return contentSize - visibleAreaSize;
    }
    @Override
    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        boolean hovered = desc.mouseIn(mouseX, mouseY);
        desc.setHovered(hovered);
        if (hovered) desc.setHoverPos(mouseX, mouseY);
        fwl().currentTheme().renderScrollBar(graphics, delta, desc);
    }

    @Override
    public void tick() {
        desc.tick();
    }

    private float calculateProgressByMousePos(float mouseX, float mouseY) {
        float pos;
        float minPos;
        float size;
        if (desc.orientation() == Orientation.VERTICAL) {
            float h = desc.height();
            pos = mouseY;
            minPos = desc.y() + (h * coveredPartSize() / 2);
            size = desc.height() - (h * coveredPartSize());
        }
        else {
            float w = desc.width();
            pos = mouseX;
            minPos = desc.x() + (w * coveredPartSize() / 2);
            size = desc.width() - (w * coveredPartSize());
        }
        return (pos - minPos) / size;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float x = (float) mouseX, y = (float) mouseY;
        if (desc.mouseIn(x, y) && !desc.disabled()) {
            //float progress = (float) (desc.orientation() == Orientation.VERTICAL ? (mouseY - desc.y()) / desc.height() : (mouseX - desc.x()) / desc.width());
            float progress = calculateProgressByMousePos(x, y);
            setProgress(progress * scrollableArea());
            desc.setClicked(true);
            return true;
        }
        return false;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        float x = (float) mouseX, y = (float) mouseY;
        if (desc.clicked()) {
            //float progress = (float) (desc.orientation() == Orientation.VERTICAL ? (mouseY - desc.y()) / desc.height() : (mouseX - desc.x()) / desc.width());
            float progress = calculateProgressByMousePos(x, y);
            setProgress(progress * scrollableArea());
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (desc.clicked()) {
            desc.setClicked(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (desc.orientation().allowScroll(FWLWidget.isShiftDown())) {
            setProgress(progress - ((float) amount * scrollStep));
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

    public interface Callback {
        void onValueChange(float value);
    }
}
