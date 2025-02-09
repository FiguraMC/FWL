package org.figuramc.fwl.text.components;

import org.figuramc.fwl.FWL;
import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.providers.headless.EffectProvider;
import org.figuramc.fwl.text.providers.headless.ProviderBuilder;
import org.figuramc.fwl.text.sinks.OffsetSink;
import org.figuramc.fwl.text.sinks.StyledSink;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractComponent {
    protected final ArrayList<AbstractComponent> siblings = new ArrayList<>();
    private @Nullable EffectProvider styleProvider;

    public final List<AbstractComponent> siblings() {
        return siblings;
    }

    public AbstractComponent() {
        styleProvider = null;
    }

    public AbstractComponent(FWLStyle style) {
        styleProvider = new ProviderBuilder(style).build();
    }

    public AbstractComponent(@Nullable EffectProvider provider) {
        styleProvider = provider;
    }

    public FWLStyle getStyle(int index) {
        FWLStyle parentStyle = getSelfStyle(index);
        return parentStyle.applyFrom(getSiblingStyle(index));
    }

    protected FWLStyle getSelfStyle(int index) {
        return styleProvider != null ? styleProvider.get(index, length()) : FWLStyle.EMPTY;
    }

    protected FWLStyle getSiblingStyle(int index) {
        if (index < 0 || index >= length()) return FWLStyle.empty();
        int offset = 0;
        Iterator<AbstractComponent> siblings = siblings().iterator();
        while (siblings.hasNext() && index - offset >= 0) {
            AbstractComponent component = siblings.next();
            int i = index - offset;
            int len = component.length();
            if (i < len) return component.getStyle(i);
            else offset += len;
        }
        return FWLStyle.empty();
    }

    public AbstractComponent setStyle(FWLStyle style) {
        styleProvider = new ProviderBuilder(style).build();
        return this;
    }

    public AbstractComponent setStyle(EffectProvider provider) {
        styleProvider = provider;
        return this;
    }

    protected abstract int selfLength();

    public int length() {
        int l = selfLength();
        for (AbstractComponent sibling: siblings) {
            l += sibling.length();
        }
        return l;
    }

    protected abstract boolean visitSelf(StyledSink sink);

    public boolean visit(StyledSink sink) {
        if (!visitSelf(sink)) return false;
        int offset = selfLength();
        for (AbstractComponent component: siblings){
            if (!component.visit(new OffsetSink(this::getSelfStyle, sink, offset))) return false;
            offset += component.length();
        }
        return true;
    }

    public AbstractComponent append(AbstractComponent other) {
        siblings.add(other);
        return this;
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

    public abstract String type();
}
