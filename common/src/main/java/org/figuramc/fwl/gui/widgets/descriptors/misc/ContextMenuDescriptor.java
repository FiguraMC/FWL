package org.figuramc.fwl.gui.widgets.descriptors.misc;

import org.figuramc.fwl.gui.widgets.descriptors.ClickableDescriptor;

public class ContextMenuDescriptor extends ClickableDescriptor {
    public static final String WIDGET_TYPE = "context_menu";

    private int entriesCount;

    public ContextMenuDescriptor(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public int entriesCount() {
        return entriesCount;
    }

    public ContextMenuDescriptor setEntriesCount(int entriesCount) {
        this.entriesCount = entriesCount;
        return this;
    }

    @Override
    public String widgetType() {
        return WIDGET_TYPE;
    }
}
