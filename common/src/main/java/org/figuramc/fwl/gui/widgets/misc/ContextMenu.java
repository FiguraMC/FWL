package org.figuramc.fwl.gui.widgets.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.figuramc.fwl.FWL;
import org.figuramc.fwl.gui.screens.FWLScreen;
import org.figuramc.fwl.gui.themes.ColorTypes;
import org.figuramc.fwl.gui.themes.FWLTheme;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.utils.Rectangle;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.function.Function;

import static org.figuramc.fwl.utils.MathUtils.wrapModulo;

public class ContextMenu implements FWLWidget {
    private FWLScreen parent;
    private float x, y;
    private final ArrayList<Entry> entries = new ArrayList<>();
    private float width;
    private float height;
    private boolean focused;
    private int currentEntry = -1;
    private int priority;

    public ContextMenu(FWLScreen screen, float x, float y) {
        this.parent = screen;
        this.x = x;
        this.y = y;
    }

    @Override
    public Rectangle boundaries() {
        return new Rectangle(x, y, width + 8, height + 8);
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    public ContextMenu addEntry(Entry entry) {
        entries.add(entry);
        updateSize();
        return this;
    }

    public ContextMenu removeEntry(Entry entry) {
        entries.remove(entry);
        updateSize();
        return this;
    }

    private void updateSize() {
        Font font = Minecraft.getInstance().font;
        width = 0;
        height = font.lineHeight * entries.size();
        for (Entry entry: entries) {
            width = Math.max(width, font.width(entry.text()));
        }
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
        FWLTheme theme = FWL.peekTheme();
        Font font = Minecraft.getInstance().font;

        int lineHeight = font.lineHeight;

        int color = theme.getColor(ColorTypes.TEXT);
        int accentColor = theme.getColor(ColorTypes.SECONDARY);
        int borderColor = theme.getColor(ColorTypes.BORDER);

        float scaling = 2f / FWLTheme.getWindowScaling();

        float x1 = x;
        float y1 = y;
        float x2 = x + width + 4;
        float y2 = y + height + 4;
        PoseStack stack = graphics.pose();
        stack.pushPose();
        stack.translate(0, 0, 1);
        theme.renderPane(graphics, x1, y1, x2, y2, null);
        float textX1 = x1 + 2;

        int entriesCount = entries.size();
        for (int i = 0; i < entriesCount; i++) {
            Entry entry = entries.get(i);
            float textY = y + (i * lineHeight) + 2;
            int textColor;
            if (currentEntry == i || (mouseX >= x1 && mouseX <= x2 && mouseY >= textY && mouseY <= textY + lineHeight)) textColor = accentColor;
            else textColor = color;
            FWLTheme.renderText(graphics, entry.text(), textX1, textY, 0, 1, textColor);
            if (i < entriesCount - 1) {
                float lineY = textY + lineHeight - 1;
                FWLTheme.fill(graphics, x1, lineY, x2, lineY + scaling, 0.0f, borderColor);
            }
        }
        stack.popPose();

    }

    @Override
    public void tick() {}

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_UP -> {
                if (currentEntry == -1) currentEntry = 0;
                else currentEntry = wrapModulo(currentEntry - 1, entries.size());
            }

            case GLFW.GLFW_KEY_DOWN -> {
                if (currentEntry == -1) currentEntry = 0;
                else currentEntry = (currentEntry + 1) % entries.size();
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
            Font font = Minecraft.getInstance().font;
            int index = (int) (mouseY - (y + 2)) / font.lineHeight;
            if (index < entries.size()) {
                Entry entry = entries.get(index);
                int modifiers = FWLWidget.getCurrentModifiers();
                keepOpen = entry.onInteraction(modifiers);
            }
        }
        if (!keepOpen) parent.removeWidget(this);
        return true;
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
