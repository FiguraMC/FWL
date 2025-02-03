package org.figuramc.fwl.gui.widgets.descriptors.input;

import org.figuramc.fwl.gui.widgets.descriptors.ClickableDescriptor;
import org.figuramc.fwl.gui.widgets.descriptors.Orientation;
import org.figuramc.fwl.gui.widgets.descriptors.ScrollBarDescriptor;
import org.jetbrains.annotations.Nullable;

public class SliderDescriptor extends ClickableDescriptor {
    public static final String WIDGET_TYPE = "slider";

    private float progress;
    private Orientation orientation;
    private int steps;

    public SliderDescriptor(float x, float y, float width, float height, float progress, @Nullable Orientation orientation) {
        super(x, y, width, height);
        this.progress = progress;
        this.orientation = orientation != null ? orientation : Orientation.HORIZONTAL;
    }

    public SliderDescriptor(float x, float y, float width, float height, float progress) {
        this(x, y, width, height, progress, null);
    }

    public SliderDescriptor(float x, float y, float width, float height) {
        this(x, y, width, height, 0);
    }



    public float progress() {
        return progress;
    }

    public SliderDescriptor setProgress(float progress) {
        this.progress = progress;
        return this;
    }

    public Orientation orientation() {
        return orientation;
    }

    public SliderDescriptor setOrientation(@Nullable Orientation orientation) {
        this.orientation = orientation != null ? orientation : Orientation.HORIZONTAL;
        return this;
    }

    public int steps() {
        return steps;
    }

    public SliderDescriptor setSteps(int steps) {
        this.steps = steps;
        return this;
    }

    @Override
    public String widgetType() {
        return WIDGET_TYPE;
    }
}
