package org.figuramc.fwl.gui.widgets.tabs;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.figuramc.fwl.gui.themes.FWLTheme;
import org.figuramc.fwl.gui.widgets.descriptors.BoundsDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.BoundsDescriptor.Side;
import org.figuramc.fwl.gui.widgets.descriptors.ClickableDescriptor;
import org.figuramc.fwl.gui.widgets.tabs.pages.PageEntry;
import org.figuramc.fwl.utils.Rectangle;
import org.figuramc.fwl.utils.RenderUtils;
import org.figuramc.fwl.utils.Scissors;

import java.util.ArrayList;

import static org.figuramc.fwl.FWL.fwl;
import static org.figuramc.fwl.utils.MathUtils.clamp;
import static org.figuramc.fwl.utils.RenderUtils.*;
import static org.figuramc.fwl.utils.TextUtils.substr;


public class SideViewSwitcher extends ViewSwitcher {

    private boolean collapsed;
    private boolean clicked;

    private float entryHeight = 12;
    private float expandedWidth = 50;

    private float scrollStep = 10;
    private float tabListScroll;

    private final BoundsDescriptor pageBounds;
    private final BoundsDescriptor sideBarBounds;
    private final ArrayList<BoundsDescriptor> entries = new ArrayList<>();

    public SideViewSwitcher(float x, float y, float width, float height) {
        super(x, y, width, height);
        pageBounds = new BoundsDescriptor(0 ,0 ,0 ,0, "view_switcher/side", 0, 0, 0, 0);
        pageBounds.setEnabled(Side.LEFT, false);
        sideBarBounds = new BoundsDescriptor(0 ,0 ,0 ,0, "view_switcher/side/bar", 0, 0, 0, 0);
        sideBarBounds.setInner(Side.RIGHT, true);
        updateBounds();
    }

    @Override
    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        FWLTheme theme = fwl().currentTheme();

        float x = x(), y = y(), height = height();
        float entryWidth = entryWidth(), entryHeight = entryHeight();

        renderBackground(theme, graphics, delta);

        Scissors.enableScissors(graphics, x, y + 7, entryWidth, height - 7);
        Component tooltip = null;
        for (int i = 0; i < entries.size(); i++) {
            float entryY = y + (entryHeight * i) + 8 - tabListScroll;
            PageEntry entry = pages.get(i);
            BoundsDescriptor desc = entries.get(i);
            float yAddition = i == 0 ? 1 : 0;
            Rectangle bounds = new Rectangle(x, entryY - yAddition, entryWidth, entryHeight + yAddition);
            boolean hovered = bounds.pointIn(mouseX, mouseY);

            desc.setEnabled(Side.TOP, i == 0);
            desc.setBounds(bounds);
            desc.setHovered(hovered);
            desc.setHoverPos(mouseX, mouseY);

            theme.fillBounds(graphics, delta, desc);
            theme.renderBounds(graphics, delta, desc);

            renderIconAndText(graphics, entry, delta, entryY, entryWidth, hovered, desc.hoveredTicks());
            if (hovered) {
                tooltip = entry.getTooltip();
            }
        }
        Scissors.disableScissors(graphics);
        renderBounds(theme, graphics, delta);

        super.render(graphics, mouseX, mouseY, delta);

        if (tooltip != null) RenderUtils.renderTooltip(graphics, tooltip.getVisualOrderText(), mouseX, mouseY, 1);
    }

    private void renderBackground(FWLTheme theme, GuiGraphics graphics, float delta) {
        theme.fillBounds(graphics, delta, sideBarBounds);
        theme.fillBounds(graphics, delta, pageBounds);
    }

    private void renderBounds(FWLTheme theme, GuiGraphics graphics, float delta) {
        theme.renderBounds(graphics, delta, sideBarBounds);
        theme.renderBounds(graphics, delta, pageBounds);
    }

    private void renderIconAndText(GuiGraphics graphics, PageEntry entry, float delta, float entryY, float entryWidth, boolean hovered, int hoveredTicks) {
        float iconX = x + 2, iconY = entryY + 1, iconSize = entryHeight - 4;
        if (collapsed) {
            renderIconOrFirstLetter(graphics, entry, iconX, iconY, iconSize, iconSize);
        }
        else {
            boolean rendered = entry.renderIcon(graphics, iconX, iconY, iconSize, iconSize);
            Component text = entry.getTitle();
            float textScale = Math.min((entryHeight - 2) / lineHeight(), 1);
            float textHeight = lineHeight() * textScale;
            float textX = rendered ? iconX + iconSize + 1 : iconX;
            float textY = entryY + (entryHeight - textHeight) / 2;
            float maxTextWidth = (rendered ? entryWidth - (iconSize + 1) : entryWidth) - 4;
            float textWidth = textWidth(text, textScale);
            float widthDiff = textWidth - maxTextWidth;

            Scissors.enableScissors(graphics, textX, entryY, maxTextWidth, entryHeight);
            if (widthDiff <= 0 || !hovered) renderText(graphics, text, textX, textY, 0, textScale, 0xFFFFFFFF, false);
            else {
                float scrollTicks = widthDiff / 2f;
                float scrollProgress = scrollProgress(scrollTicks + 15, scrollTicks, hoveredTicks + delta);
                float textXOffset = widthDiff * scrollProgress;
                renderText(graphics, text, textX - textXOffset, textY, 0, textScale, 0xFFFFFFFF, false);
            }
            Scissors.disableScissors(graphics);
        }
    }

    private void renderIconOrFirstLetter(GuiGraphics graphics, PageEntry entry, float x, float y, float width, float height) {
        Component title = entry.getTitle();
        if (!entry.renderIcon(graphics, x, y, width, height)) {
            FormattedCharSequence seq = substr(title.getVisualOrderText(), 0, 1);
            float scale = height / (lineHeight() - 1);
            float w = textWidth(seq, scale);
            float xOffset = (width - w) / 2;
            renderText(graphics, seq, x + xOffset, y, 0, scale, 0xFFFFFFFF, false);
        }
    }

    private BoundsDescriptor emptyEntryBounds() {
        BoundsDescriptor desc = new BoundsDescriptor(0, 0, 0, 0, "view_switcher/side/entry", 0, 0, 0, 0);
        desc.setEnabled(Side.TOP, false);
        desc.setEnabled(Side.LEFT, false);
        desc.setEnabled(Side.RIGHT, false);
        desc.setInner(Side.BOTTOM, true);
        return desc;
    }

    private int getEntryByY(float mouseY) {
        float minY = y + 8;
        float relative = mouseY - minY + tabListScroll;
        return (int)(relative / entryHeight());
    }

    private int getEntryByY(double mouseY) {
        float minY = y + 8;
        double relative = mouseY - minY + tabListScroll;
        return (int)(relative / entryHeight());
    }

    private float getMaxScroll() {
        return Math.max((entryHeight * entries.size()) - (sideBarBounds.height() - 8), 0);
    }

    @Override
    public boolean mouseInPage(double mouseX, double mouseY) {
        return pageBounds.boundaries().pointIn(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (sideBarBounds.boundaries().pointIn(mouseX, mouseY)) {
            float minY = y + 8;
            if (mouseY < minY) {
                setCollapsed(!collapsed);
                return true;
            }
            int entryIndex = getEntryByY(mouseY);
            if (entryIndex >= 0 && entryIndex < pages.size()) {
                BoundsDescriptor bounds = entries.get(entryIndex);
                setActivePage(entryIndex);
                bounds.setClicked(true);
                clicked = true;
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (clicked) {
            entries.forEach(e -> e.setClicked(false));
            clicked = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (sideBarBounds.boundaries().pointIn(mouseX, mouseY)) {
            tabListScroll = (float) clamp(tabListScroll - (amount * scrollStep), 0, getMaxScroll());
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public SideViewSwitcher addEntry(PageEntry page) {
        entries.add(emptyEntryBounds());
        super.addEntry(page);
        return this;
    }

    @Override
    public SideViewSwitcher addEntry(int i, PageEntry page) {
        entries.add(i, emptyEntryBounds());
        super.addEntry(i, page);
        return this;
    }

    @Override
    public boolean removeEntry(PageEntry page) {
        int i = pages.indexOf(page);
        if (i != -1) entries.remove(i);
        return super.removeEntry(page);
    }

    @Override
    public PageEntry removeEntry(int i) {
        entries.remove(i);
        return super.removeEntry(i);
    }

    @Override
    public SideViewSwitcher setX(float x) {
        super.setX(x);
        updateBounds();
        return this;
    }

    @Override
    public SideViewSwitcher setY(float y) {
        super.setY(y);
        updateBounds();
        return this;
    }

    @Override
    public SideViewSwitcher setWidth(float width) {
        super.setWidth(width);
        updateBounds();
        return this;
    }

    @Override
    public SideViewSwitcher setHeight(float height) {
        super.setHeight(height);
        updateBounds();
        return this;
    }

    public float scrollStep() {
        return scrollStep;
    }

    public SideViewSwitcher setScrollStep(float scrollStep) {
        this.scrollStep = scrollStep;
        return this;
    }

    public float entryWidth() {
        return collapsed ? entryHeight : expandedWidth;
    }

    public float entryHeight() {
        return entryHeight;
    }

    public SideViewSwitcher setEntryHeight(float entryHeight) {
        this.entryHeight = entryHeight;
        return this;
    }

    public float expandedWidth() {
        return expandedWidth;
    }

    public SideViewSwitcher setExpandedWidth(float expandedWidth) {
        this.expandedWidth = expandedWidth;
        updateBounds();
        return this;
    }

    public boolean collapsed() {
        return collapsed;
    }

    public SideViewSwitcher setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
        updateBounds();
        return this;
    }

    public BoundsDescriptor boundsDescriptor() {
        return pageBounds;
    }

    private void updateBounds() {
        float entryWidth = entryWidth();
        sideBarBounds.setBounds(x, y, entryWidth, height);
        pageBounds.setBounds(x + entryWidth, y, width - entryWidth, height);
    }

    @Override
    public float pageXOffset() {
        return entryWidth() + 1 + x;
    }

    @Override
    public float pageYOffset() {
        return 1 + y;
    }

    @Override
    public float pageWidth() {
        return width - entryWidth() - 2;
    }

    @Override
    public float pageHeight() {
        return height - 2;
    }

    @Override
    public void tick() {
        super.tick();
        entries.forEach(ClickableDescriptor::tick);
    }
}
