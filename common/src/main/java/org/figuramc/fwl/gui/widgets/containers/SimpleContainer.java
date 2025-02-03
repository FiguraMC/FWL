package org.figuramc.fwl.gui.widgets.containers;

import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.utils.Rectangle;

import java.util.Iterator;

public class SimpleContainer extends AbstractFWLContainerWidget {
    private Rectangle boundaries = new Rectangle(0, 0, 0, 0);

    @Override
    public Rectangle boundaries() {
        return boundaries;
    }

    @Override
    public void addWidget(FWLWidget widget) {
        super.addWidget(widget);
        recalculateBoundaries();
    }

    @Override
    public void removeWidget(FWLWidget widget) {
        super.removeWidget(widget);
        recalculateBoundaries();
    }

    private void recalculateBoundaries() {
        Iterator<FWLWidget> widgets = this.interactableWidgets();
        boolean first = true;
        float minX = 0, maxX = 0;
        float minY = 0, maxY = 0;
        while (widgets.hasNext()) {
            FWLWidget widget = widgets.next();
            Rectangle bounds = widget.boundaries();
            if (first) {
                minX = bounds.left();
                maxX = bounds.right();
                minY = bounds.top();
                maxY = bounds.bottom();
                first = false;
            }
            else {
                minX = Math.min(minX, bounds.left());
                maxX = Math.max(maxX, bounds.right());
                minY = Math.min(minY, bounds.top());
                maxY = Math.max(maxY, bounds.bottom());
            }
        }
        boundaries = new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }
}
