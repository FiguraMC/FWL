package org.figuramc.fwl.text.components.special;

import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.components.AbstractComponent;
import org.figuramc.fwl.text.components.ContainerComponent;
import org.figuramc.fwl.text.sinks.StyledSink;
import org.joml.Vector4f;

import java.util.Objects;

import static org.figuramc.fwl.utils.MathUtils.lerp;

public class GradientComponent extends ContainerComponent {
    private Vector4f colorA = new Vector4f(1f), colorB = new Vector4f(1f);
    @Override
    protected FWLStyle getSelfStyle(int index, float progress) {
        float weight = (index + progress) / length();
        return FWLStyle.EMPTY.withColor(
            new Vector4f(
                    lerp(colorA.x, colorB.x, weight),
                    lerp(colorA.y, colorB.y, weight),
                    lerp(colorA.z, colorB.z, weight),
                    lerp(colorA.w, colorB.w, weight)
            )
        );
    }

    @Override
    protected int selfLength() {
        return 0;
    }

    @Override
    protected boolean visitSelf(StyledSink sink) {
        return true;
    }

    public Vector4f colorA() {
        return colorA;
    }

    public GradientComponent setColorA(Vector4f colorA) {
        this.colorA = Objects.requireNonNull(colorA);
        return this;
    }

    public Vector4f colorB() {
        return colorB;
    }

    public GradientComponent setColorB(Vector4f colorB) {
        this.colorB = Objects.requireNonNull(colorB);
        return this;
    }

    @Override
    public GradientComponent append(AbstractComponent other) {
        super.append(other);
        return this;
    }
}
