package org.lexize.lwl.gui.screens;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lexize.lwl.gui.widgets.LWLWidget;
import org.lexize.lwl.gui.widgets.Renderable;
import org.lexize.lwl.gui.widgets.Tickable;
import org.lexize.lwl.gui.widgets.VanillaRenderer;

import java.util.ArrayList;
import java.util.List;

public abstract class LWLScreen extends Screen {
    private final Screen prevScreen;

    private final ArrayList<Renderable> renderableWidgets = new ArrayList<>();
    private final ArrayList<Tickable> tickableWidgets = new ArrayList<>();
    private GuiEventListener focusedChild;

    protected LWLScreen(Component title, Screen prevScreen) {
        super(title);
        this.prevScreen = prevScreen;
    }

    public <T extends LWLWidget> void addWidget(T widget) {
        renderableWidgets.add(widget);
        children.add(widget);
        narratables.add(widget);
        tickableWidgets.add(widget);
    }

    public void addRenderableWidget(Renderable widget) {
        renderableWidgets.add(widget);
    }

    public void addTickableWidget(Tickable widget) {
        tickableWidgets.add(widget);
    }

    public void addInteractableWidget(GuiEventListener widget) {
        children.add(widget);
    }

    @Override
    protected <T extends net.minecraft.client.gui.components.Renderable> T addRenderableOnly(T drawable) {
        renderableWidgets.add(new VanillaRenderer(drawable));
        return drawable;
    }

    @Override
    protected <T extends GuiEventListener & net.minecraft.client.gui.components.Renderable & NarratableEntry> T addRenderableWidget(T drawableElement) {
        renderableWidgets.add(new VanillaRenderer(drawableElement));
        children.add(drawableElement);
        return drawableElement;
    }

    @Override
    protected void init() {
        renderableWidgets.clear();
        children.clear();
        tickableWidgets.clear();
    }

    @Override
    public void tick() {
        tickableWidgets.forEach(Tickable::tick);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        Window w = minecraft.getWindow();
        MouseHandler m = minecraft.mouseHandler;
        float mX = (float) (m.xpos() * w.getGuiScaledWidth() / (float )w.getScreenWidth());
        float mY = (float) (m.ypos() * w.getGuiScaledHeight() / (float) w.getScreenHeight());
        render(graphics, mX, mY, delta);
    }

    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        renderBackground(graphics);
        for (Renderable widget: renderableWidgets) {
            widget.render(graphics, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (GuiEventListener widget: children) {
            if (widget.mouseClicked(mouseX, mouseY, button)) {
                setFocused(widget);
                return true;
            }
        }
        setFocused(null);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (GuiEventListener widget: children) {
            if (widget.mouseReleased(mouseX, mouseY, button)) return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (GuiEventListener widget: children) {
            if (widget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        for (GuiEventListener widget: children) {
            if (widget.mouseScrolled(mouseX, mouseY, amount)) return true;
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (GuiEventListener widget: children) {
            if (widget.keyPressed(keyCode, scanCode, modifiers)) return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        for (GuiEventListener widget: children) {
            if (widget.keyReleased(keyCode, scanCode, modifiers)) return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for (GuiEventListener widget: children) {
            if (widget.charTyped(chr, modifiers)) return true;
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return children;
    }

    @Override
    public void onClose() {
        minecraft.setScreen(prevScreen);
    }
}
