package org.figuramc.fwl.gui.screens;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.figuramc.fwl.gui.widgets.*;
import org.figuramc.fwl.gui.widgets.containers.FWLContainerWidget;
import org.figuramc.fwl.utils.Rectangle;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.figuramc.fwl.utils.ArrayListUtils.sortedAdd;

public abstract class FWLScreen extends Screen implements FWLContainerWidget {
    private final Screen prevScreen;

    private final ArrayList<FWLWidget> renderableWidgets = new ArrayList<>();
    private final ArrayList<FWLWidget> interactableWidgets = new ArrayList<>();

    private FWLWidget focused;

    // Widgets lock. In case if widgets is added/removed during any operation that requires iterating through main lists,
    // they are being added to awaitingAdd/awaitingRemoved list, that is being cleared when after unlock.
    private boolean lock;
    private final ArrayList<FWLWidget> awaitingAdd = new ArrayList<>();
    private final ArrayList<FWLWidget> awaitingRemove = new ArrayList<>();

    protected FWLScreen(Component title, Screen prevScreen) {
        super(title);
        this.prevScreen = prevScreen;
    }

    @Override
    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        renderBackground(graphics);
        FWLContainerWidget.super.render(graphics, mouseX, mouseY, delta);
    }

    public void addWidget(FWLWidget widget) {
        if (lock) {
            awaitingAdd.add(widget);
            return;
        }
        sortedAdd(interactableWidgets, widget, FWLWidget::compareInteractionPriority);
        sortedAdd(renderableWidgets, widget, FWLWidget::compareRenderPriority);
    }

    public void removeWidget(FWLWidget widget) {
        if (lock) {
            awaitingRemove.add(widget);
            return;
        }
        interactableWidgets.remove(widget);
        renderableWidgets.remove(widget);
    }

    @Override
    public void lock() {
        lock = true;
    }

    @Override
    public void unlock() {
        lock = false;
        for (FWLWidget widget: awaitingAdd) {
            addWidget(widget);
        }

        for (FWLWidget widget: awaitingRemove) {
            removeWidget(widget);
        }

        awaitingAdd.clear();
        awaitingRemove.clear();
    }

    @Override
    public Iterator<FWLWidget> interactableWidgets() {
        return interactableWidgets.iterator();
    }

    @Override
    public Iterator<FWLWidget> renderableWidgets() {
        return renderableWidgets.iterator();
    }

    @Override
    protected void init() {
        renderableWidgets.clear();
        interactableWidgets.clear();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        Window w = minecraft.getWindow();
        MouseHandler m = minecraft.mouseHandler;
        float mX = (float) (m.xpos() * w.getGuiScaledWidth() / (float )w.getScreenWidth());
        float mY = (float) (m.ypos() * w.getGuiScaledHeight() / (float) w.getScreenHeight());
        render(graphics, mX, mY, delta);
    }

    @Nullable
    @Override
    public FWLWidget getFocused() {
        return focused;
    }

    public void setFocused(@Nullable FWLWidget child) {
        if (focused != child) {
            if (focused != null) focused.setFocused(false);
            if (child != null) child.setFocused(true);
            focused = child;
        }
    }

    @Override
    public List<FWLWidget> children() {
        return interactableWidgets;
    }

    @Override
    public void onClose() {
        minecraft.setScreen(prevScreen);
    }


    @Override
    public Rectangle boundaries() {
        return null;
    }
}
