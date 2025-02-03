package org.figuramc.fwl.gui.widgets.tabs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.gui.widgets.Resizeable;
import org.figuramc.fwl.gui.widgets.tabs.pages.PageEntry;
import org.figuramc.fwl.utils.Rectangle;
import org.figuramc.fwl.utils.Scissors;

import java.util.ArrayList;

import static org.figuramc.fwl.utils.MathUtils.clamp;

public abstract class ViewSwitcher implements FWLWidget {
    protected float x, y, width, height;
    private boolean focused;

    protected final ArrayList<PageEntry> pages = new ArrayList<>();

    private int pageIndex = -1;
    private FWLWidget page;
    private float prevWidth, prevHeight;

    public ViewSwitcher(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public PageEntry selectedPage() {
        return pages.get(pageIndex);
    }

    public int currentPage() {
        return pageIndex;
    }

    public ViewSwitcher setActivePage(int index) {
        updateActivePage(index);
        return this;
    }

    private void updateActivePage(int index) {
        int ind = clamp(index, 0, pages.size() - 1);
        if (pageIndex != ind) {
            pageIndex = ind;
            PageEntry entry = selectedPage();
            float width = pageWidth(), height = pageHeight();
            if (page != null) page.setFocused(false);
            page = entry.getPage(width, height);
            if (page != null) page.setFocused(focused);
            prevWidth = width;
            prevHeight = height;
        }
    }

    public ViewSwitcher addEntry(PageEntry page) {
        pages.add(page);
        updateActivePage(clamp(pageIndex, 0, pages.size() - 1));
        return this;
    }

    public ViewSwitcher addEntry(int i, PageEntry page) {
        pages.add(i, page);
        updateActivePage(clamp(pageIndex, 0, pages.size() - 1));
        return this;
    }

    public boolean removeEntry(PageEntry page) {
        boolean res = pages.remove(page);
        updateActivePage(clamp(pageIndex, 0, pages.size() - 1));
        return res;
    }

    public PageEntry removeEntry(int i) {
        PageEntry res = pages.remove(i);
        updateActivePage(clamp(pageIndex, 0, pages.size() - 1));
        return res;
    }

    @Override
    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        if (page != null) {
            resizeIfSizeChanged();
            float pageX = pageXOffset(), pageY = pageYOffset(), pageWidth = pageWidth(), pageHeight = pageHeight();
            PoseStack stack = graphics.pose();
            stack.pushPose();
            stack.translate(pageX, pageY, 0);
            Scissors.enableScissors(graphics, 0 , 0, pageWidth, pageHeight);
            page.render(graphics, mouseX - pageX, mouseY - pageY, delta);
            Scissors.disableScissors(graphics);
            stack.popPose();
        }
    }

    @Override
    public void tick() {
        if (page != null) page.tick();
    }

    @Override
    public Rectangle boundaries() {
        return new Rectangle(x, y, width, height);
    }

    public float pageXOffset() {
        return x;
    }
    public float pageYOffset() {
        return y;
    }
    public abstract float pageWidth();
    public abstract float pageHeight();

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
        if (page != null) page.setFocused(focused);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (page != null) page.mouseMoved(mouseX - pageXOffset(), mouseY - pageYOffset());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseInPage(mouseX, mouseY)) {
            if (page != null) page.mouseClicked(mouseX - pageXOffset() , mouseY - pageYOffset(), button);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (mouseInPage(mouseX, mouseY)) {
            if (page != null) page.mouseReleased(mouseX - pageXOffset() , mouseY - pageYOffset(), button);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (mouseInPage(mouseX, mouseY)) {
            if (page != null) page.mouseDragged(mouseX - pageXOffset() , mouseY - pageYOffset(), button, deltaX, deltaY);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseInPage(mouseX, mouseY)) {
            if (page != null) page.mouseScrolled(mouseX - pageXOffset() , mouseY - pageYOffset(), amount);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return focused && page != null && page.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return focused && page != null && page.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return focused && page != null && page.charTyped(chr, modifiers);
    }

    public abstract boolean mouseInPage(double mouseX, double mouseY);

    public float x() {
        return x;
    }

    public ViewSwitcher setX(float x) {
        this.x = x;
        return this;
    }

    public float y() {
        return y;
    }

    public ViewSwitcher setY(float y) {
        this.y = y;
        return this;
    }

    public float width() {
        return width;
    }

    public ViewSwitcher setWidth(float width) {
        this.width = width;
        return this;
    }

    public float height() {
        return height;
    }

    public ViewSwitcher setHeight(float height) {
        this.height = height;
        return this;
    }

    protected void resizeIfSizeChanged() {
        float width = pageWidth(), height = pageHeight();
        if (prevWidth != width || prevHeight != height) {
            if (page != null && page instanceof Resizeable res) res.resize(width, height);
            prevWidth = width;
            prevHeight = height;
        }
    }
}
