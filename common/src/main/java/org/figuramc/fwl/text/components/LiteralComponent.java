package org.figuramc.fwl.text.components;

import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.sinks.StyledSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.IntStream;

public class LiteralComponent extends ContainerComponent {
    private FWLStyle style;
    private @NotNull String text;

    public LiteralComponent(@NotNull String text) {
        this.text = Objects.requireNonNull(text);
    }

    @Override
    protected FWLStyle getSelfStyle(int index, float progress) {
        return style == null ? FWLStyle.EMPTY : style;
    }

    @Override
    protected int selfLength() {
        return text.length();
    }

    @Override
    protected boolean visitSelf(StyledSink sink) {
        IntStream codepointStream = text.chars();
        Iterator<Integer> iterator = codepointStream.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            if (!sink.accept(i, this::getSelfStyle, iterator.next())) return false;
            i += 1;
        }
        return true;
    }

    @Override
    public LiteralComponent append(AbstractComponent other) {
        super.append(other);
        return this;
    }

    public LiteralComponent setText(@NotNull String text) {
        this.text = Objects.requireNonNull(text);
        return this;
    }

    public @NotNull String getText() {
        return text;
    }

    public LiteralComponent setStyle(FWLStyle style) {
        this.style = style;
        return this;
    }

    public @Nullable FWLStyle getStyle() {
        return style;
    }
}
