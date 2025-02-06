package org.figuramc.fwl.text.components;

import org.figuramc.fwl.text.FWLStyle;
import org.figuramc.fwl.text.sinks.OffsetSink;
import org.figuramc.fwl.text.sinks.StyledSink;

import java.util.Iterator;

public abstract class AbstractComponent {

    protected abstract Iterator<AbstractComponent> siblings();
    
    public FWLStyle getStyle(int index, float progress) {
        FWLStyle parentStyle = getSelfStyle(index, progress);
        return parentStyle.applyFrom(getSiblingStyle(index, progress));
    }

    protected abstract FWLStyle getSelfStyle(int index, float progress);

    protected FWLStyle getSiblingStyle(int index, float progress) {
        if (index < 0 || index >= length()) return FWLStyle.EMPTY;
        int offset = 0;
        Iterator<AbstractComponent> siblings = siblings();
        while (siblings.hasNext() && index - offset >= 0) {
            int i = index - offset;
            AbstractComponent component = siblings.next();
            int len = component.length();
            if (i < len) return component.getStyle(i, progress);
            else offset += len;
        }
        return FWLStyle.EMPTY;
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
