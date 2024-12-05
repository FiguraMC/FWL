package org.figuramc.fwl.gui.widgets.descriptors;

import org.figuramc.fwl.gui.widgets.descriptors.button.ClickableDescriptor;
import org.figuramc.fwl.utils.BiTickCounter;
import org.jetbrains.annotations.Nullable;

public class ScrollBarDescriptor extends ClickableDescriptor {
    public static final String WIDGET_TYPE = "scrollbar";

    private float progress;
    private Orientation orientation;
    private float coveredPartSize;

    public ScrollBarDescriptor(float x, float y, float width, float height, float coveredPartSize) {
        this(x, y, width, height, coveredPartSize, 0,null);
    }

    public ScrollBarDescriptor(float x, float y, float width, float height, float coveredPartSize, float progress) {
        this(x, y, width, height, coveredPartSize, progress,null);
    }

    public ScrollBarDescriptor(float x, float y, float width, float height, float coveredPartSize, float progress, @Nullable Orientation orientation) {
        super(x, y, width, height);
        this.progress = progress;
        this.orientation = orientation != null ? orientation : Orientation.VERTICAL;
        this.coveredPartSize = coveredPartSize;
    }

    public float progress() {
        return progress;
    }

    public ScrollBarDescriptor setProgress(float progress) {
        this.progress = progress;
        return this;
    }

    public Orientation orientation() {
        return orientation;
    }

    public ScrollBarDescriptor setOrientation(Orientation orientation) {
        this.orientation = orientation;
        return this;
    }

    public float coveredPartSize() {
        return coveredPartSize;
    }

    public ScrollBarDescriptor setCoveredPartSize(float coveredPartSize) {
        this.coveredPartSize = coveredPartSize;
        return this;
    }

    @Override
    public String widgetType() {
        return WIDGET_TYPE;
    }
}
