package org.figuramc.fwl.text.components;

import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.providers.headless.HeadlessStyleProvider;
import org.figuramc.fwl.text.providers.headless.ProviderBuilder;
import org.figuramc.fwl.text.sinks.OffsetSink;
import org.figuramc.fwl.text.sinks.StyledSink;

import java.util.Iterator;

public abstract class AbstractComponent {
    private HeadlessStyleProvider styleProvider;

    protected abstract Iterator<AbstractComponent> siblings();

    public AbstractComponent() {
        styleProvider = new ProviderBuilder(FWLStyle.style()).build();
    }

    public AbstractComponent(FWLStyle style) {
        styleProvider = new ProviderBuilder(style).build();
    }

    public AbstractComponent(HeadlessStyleProvider provider) {
        styleProvider = provider;
    }

    public FWLStyle getStyle(int index) {
        FWLStyle parentStyle = getSelfStyle(index);
        return parentStyle.applyFrom(getSiblingStyle(index));
    }

    protected FWLStyle getSelfStyle(int index) {
        return styleProvider.get(index, length());
    }

    protected FWLStyle getSiblingStyle(int index) {
        if (index < 0 || index >= length()) return FWLStyle.EMPTY;
        int offset = 0;
        Iterator<AbstractComponent> siblings = siblings();
        while (siblings.hasNext() && index - offset >= 0) {
            int i = index - offset;
            AbstractComponent component = siblings.next();
            int len = component.length();
            if (i < len) return component.getStyle(i);
            else offset += len;
        }
        return FWLStyle.EMPTY;
    }

    public AbstractComponent setStyle(FWLStyle style) {
        styleProvider = new ProviderBuilder(style).build();
        return this;
    }

    public AbstractComponent setStyle(HeadlessStyleProvider provider) {
        styleProvider = provider;
        return this;
    }


    protected abstract int selfLength();

    public int length() {
        int l = selfLength();
        Iterator<AbstractComponent> siblings = siblings();
        while (siblings.hasNext()) l += siblings.next().length();
        return l;
    }

    protected abstract boolean visitSelf(StyledSink sink);

    public boolean visit(StyledSink sink) {
        if (!visitSelf(sink)) return false;
        Iterator<AbstractComponent> siblings = siblings();
        int offset = selfLength();
        while (siblings.hasNext()) {
            AbstractComponent component = siblings.next();
            if (!component.visit(new OffsetSink(this::getSelfStyle, sink, offset))) return false;
            offset += component.length();
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        visit((i, p, codepoint) -> {
            sb.appendCodePoint(codepoint);
            return true;
        });
        return sb.toString();
    }
}
