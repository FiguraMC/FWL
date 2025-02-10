package org.figuramc.fwl.gui.themes.configs;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import org.figuramc.fwl.gui.themes.FWLBreeze;
import org.figuramc.fwl.gui.themes.FWLTheme;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.gui.widgets.Resizeable;
import org.figuramc.fwl.gui.widgets.containers.AbstractFWLContainerWidget;
import org.figuramc.fwl.gui.widgets.descriptors.Orientation;
import org.figuramc.fwl.gui.widgets.misc.Label;
import org.figuramc.fwl.gui.widgets.scrollable.ScrollBar;
import org.figuramc.fwl.gui.widgets.scrollable.ScrollableArea;
import org.figuramc.fwl.text.components.AbstractComponent;
import org.figuramc.fwl.utils.Pair;
import org.figuramc.fwl.utils.Rectangle;
import org.figuramc.fwl.utils.RenderUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class FWLAbstractConfig<T extends FWLTheme> extends AbstractFWLContainerWidget implements Resizeable {
    private final float SCROLLBAR_WIDTH = 10;

    private float width, height;
    private final T parent;

    private ScrollBar mainScrollBar;
    private ScrollableArea scrollableArea;

    private boolean focused;

    private final ArrayList<Pair<Label, FWLWidget>> fields = new ArrayList<>();

    public FWLAbstractConfig(T parent, float width, float height) {
        this.parent = parent;
        this.width = width;
        this.height = height;
    }

    protected void init() {
        addWidget(scrollableArea = new ScrollableArea(0, 0, width - SCROLLBAR_WIDTH, height));

        float currentY = 10;
        for (Pair<AbstractComponent, FieldConstructor<T>> pair: getConstructors()) {
            AbstractComponent text = pair.a();
            FieldConstructor<T> constructor = pair.b();
            float textWidth = RenderUtils.textWidth(text);
            float textHeight = RenderUtils.textHeight(text);
            float widgetYOffset = (textHeight - 20) / 2;
            float textX = 10;
            float textY = currentY + 1;

            float widgetX = textX + textWidth + 10;
            float widgetY = currentY;

            Label label = new Label(textX, textY, text);
            FWLWidget widget = constructor.getWidget(parent, widgetX, widgetY);

            scrollableArea.addWidget(label);
            scrollableArea.addWidget(widget);

            fields.add(new Pair<>(label, widget));

            currentY += Math.max(widget.boundaries().height(), textHeight) + 10;
        }

        addWidget(mainScrollBar = new ScrollBar(width - SCROLLBAR_WIDTH, 0, SCROLLBAR_WIDTH, height, height, currentY, 0, Orientation.VERTICAL));
        mainScrollBar.setCallback(scrollableArea::setOffsetY);
    }

    @Override
    public void resize(float width, float height) {
        this.width = width;
        this.height = height;

        scrollableArea.setWidth(width - SCROLLBAR_WIDTH).setHeight(height);
        mainScrollBar.setX(width - SCROLLBAR_WIDTH).setHeight(height);
    }

    @Override
    public Rectangle boundaries() {
        return new Rectangle(0, 0, width, height);
    }

    protected abstract Pair<AbstractComponent, FieldConstructor<T>>[] getConstructors();

    public interface FieldConstructor<T> {
        FWLWidget getWidget(T parent, float x, float y);
    }
}
