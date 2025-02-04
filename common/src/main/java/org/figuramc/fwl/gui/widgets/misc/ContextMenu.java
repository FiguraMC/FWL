package org.figuramc.fwl.gui.widgets.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.figuramc.fwl.gui.screens.FWLScreen;
import org.figuramc.fwl.gui.themes.ColorTypes;
import org.figuramc.fwl.gui.themes.FWLTheme;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.gui.widgets.containers.FWLContainerWidget;
import org.figuramc.fwl.gui.widgets.descriptors.misc.ContextMenuDescriptor;
import org.figuramc.fwl.utils.Rectangle;
import org.figuramc.fwl.utils.RenderUtils;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.function.Function;

import static org.figuramc.fwl.FWL.fwl;
import static org.figuramc.fwl.utils.MathUtils.clamp;
import static org.figuramc.fwl.utils.MathUtils.wrapModulo;

public class ContextMenu implements FWLWidget {
    public static final int MAX_CONTEXT_ENTRIES_UNTIL_SCROLL = 8;
    private final ContextMenuDescriptor desc;
    private final FWLContainerWidget parent;
    private final ArrayList<Entry> entries = new ArrayList<>();
    private int currentEntry = -1;
    private int currentScroll = 0;
    private int priority;

    public ContextMenu(FWLContainerWidget parent, float x, float y) {
        this.parent = parent;
        this.desc = new ContextMenuDescriptor(x, y, 0, 0);
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

    public ContextMenu addEntry(Entry entry) {
        entries.add(entry);
        update();
        return this;
    }

    public ContextMenu removeEntry(Entry entry) {
        entries.remove(entry);
        update();
        return this;
    }

    private void update() {
        Font font = Minecraft.getInstance().font;
        int width = 0;
        int height = entryHeight() * Math.min(entries.size(), MAX_CONTEXT_ENTRIES_UNTIL_SCROLL);
        for (Entry entry: entries) {
            width = Math.max(width, font.width(entry.text()));
        }
        desc.setWidth(width + 4);
        desc.setHeight(height + 4);
        desc.setEntriesCount(entries.size());
    }

    @Override
    public int renderPriority() {
        return priority;
    }

    @Override
    public int interactionPriority() {
        return priority;
    }

    @Override
    public ContextMenu setInteractionPriority(int priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        FWLTheme theme = fwl().currentTheme();

        int lineHeight = entryHeight();

        int color = theme.getColorOrDefault(ColorTypes.TEXT, 0xFFFFFFFF);
        int accentColor = theme.getColorOrDefault(ColorTypes.SECONDARY, 0xFFFFAAAA);

        float x = desc.x(), y = desc.y(), width = desc.width(), height = desc.height();

        float x2 = x + width;

        theme.renderContextMenu(graphics, delta, desc);
        float textX1 = x + 2;

        int entriesCount = entries.size();
        for (int i = 0; i < Math.min(entriesCount, MAX_CONTEXT_ENTRIES_UNTIL_SCROLL); i++) {
            Entry entry = entries.get(i + currentScroll);
            float textY = y + (i * lineHeight) + 2;
            int textColor;
            if (currentEntry == i || (mouseX >= x && mouseX <= x2 && mouseY >= textY && mouseY <= textY + lineHeight)) textColor = accentColor;
            else textColor = color;
            RenderUtils.renderText(graphics, entry.text(), textX1, textY, 0, 1, textColor, false);
        }
        if (entriesCount > 8) {
            int unrenderedEntries = entriesCount - 8;
            float barHeight = (8f / entriesCount) * height;
            float barYStep = (height - barHeight) / unrenderedEntries;
            float barY = y + (barYStep * currentScroll);
            RenderUtils.fill(graphics, x2 - 2, barY, x2, barY + barHeight, 0, accentColor);
        }
    }

    private void addToScroll(int addition) {
        currentScroll = clamp(currentScroll + addition, 0, Math.max(0, entries.size() - MAX_CONTEXT_ENTRIES_UNTIL_SCROLL));
    }

    @Override
    public void tick() {}

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_UP -> {
                if (currentEntry == -1) currentEntry = 0;
                else currentEntry = wrapModulo(currentEntry - 1, entries.size());
                if (currentEntry < currentScroll) addToScroll(-1);
            }

            case GLFW.GLFW_KEY_DOWN -> {
                if (currentEntry == -1) currentEntry = 0;
                else currentEntry = (currentEntry + 1) % entries.size();
                if (currentEntry > currentScroll + 7) addToScroll(1);
            }

            case GLFW.GLFW_KEY_ENTER -> {
                boolean keepOpen = false;
                if (currentEntry != -1) {
                    Entry entry = entries.get(currentEntry);
                    keepOpen = entry.onInteraction(modifiers);
                }
                if (!keepOpen) parent.removeWidget(this);
            }

            case GLFW.GLFW_KEY_ESCAPE -> {
                parent.removeWidget(this);
            }
        }
        return true;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        currentEntry = -1;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean keepOpen = false;
        if (isMouseOver(mouseX, mouseY)) {
            int index = (int) (mouseY - (desc.y() + 2)) / entryHeight();
            if (index < entries.size()) {
                Entry entry = entries.get(index);
                int modifiers = FWLWidget.getCurrentModifiers();
                keepOpen = entry.onInteraction(modifiers);
            }
        }
        if (!keepOpen) parent.removeWidget(this);
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        addToScroll((int) amount);
        return true;
    }

    public static int entryHeight() {
        return Minecraft.getInstance().font.lineHeight + 1;
    }

    public interface Entry {
        /**
         * Returns text that will be rendered as an entry text.
         * @return text
         */
        Component text();

        /**
         * Interaction callback
         * @param modifiers modifiers of interaction
         * @return true if context menu should remain open
         */
        boolean onInteraction(int modifiers);
    }

    public static class StandardEntry implements Entry {
        private final Component text;
        private final Function<Integer, Boolean> callback;

        public StandardEntry(@NotNull Component text, @NotNull Function<Integer, Boolean> callback) {
            this.text = text;
            this.callback = callback;
        }

        @Override
        public Component text() {
            return text;
        }

        @Override
        public boolean onInteraction(int modifiers) {
            return callback.apply(modifiers);
        }
    }
}
