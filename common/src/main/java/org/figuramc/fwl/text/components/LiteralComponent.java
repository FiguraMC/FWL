package org.figuramc.fwl.text.components;

import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.providers.headless.HeadlessStyleProvider;
import org.figuramc.fwl.text.sinks.StyledSink;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.stream.IntStream;

public class LiteralComponent extends ContainerComponent {
    private @NotNull String text;

    public LiteralComponent(@NotNull String text) {
        super();
        this.text = Objects.requireNonNull(text);
    }

    public LiteralComponent(@NotNull String text, FWLStyle style) {
        super(style);
        this.text = Objects.requireNonNull(text);
    }

    public LiteralComponent(@NotNull String text, HeadlessStyleProvider provider) {
        super(provider);
        this.text = Objects.requireNonNull(text);
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

    public static LiteralComponent literal(String text) {
        return new LiteralComponent(text);
    }

    public static LiteralComponent literal(String text, FWLStyle style) {
        return new LiteralComponent(text, style);
    }

    public static LiteralComponent literal(String text, HeadlessStyleProvider styleProvider) {
        return new LiteralComponent(text, styleProvider);
    }
}
