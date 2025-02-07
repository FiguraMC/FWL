package org.figuramc.fwl.text.fomponents;

import org.figuramc.fwl.text.FomponentVisitor;
import org.figuramc.fwl.text.style.CompiledStyle;
import org.figuramc.fwl.text.style.FomponentStyle;

import java.util.List;

public abstract class BaseFomponent {

    private final FomponentStyle style;
    private final List<BaseFomponent> siblings;
    private int length = -1;

    public BaseFomponent(FomponentStyle style, List<BaseFomponent> siblings) {
        this.style = style;
        this.siblings = siblings;
    }

    protected abstract int selfLength();
    protected abstract int selfVisit(FomponentVisitor visitor, CompiledStyle style, int currentIndex); // Return the new index!

    public int length() {
        if (length == -1) {
            length = selfLength();
            for (BaseFomponent sibling : siblings)
                length += sibling.length;
        }
        return length;
    }

    public void visit(FomponentVisitor visitor) {
        this.visit(visitor, CompiledStyle.EMPTY, 0);
    }

    private int visit(FomponentVisitor visitor, CompiledStyle parentStyle, int currentIndex) {
        CompiledStyle compiled = parentStyle.extend(this.style.compile(currentIndex, this));
        currentIndex = selfVisit(visitor, compiled, currentIndex);
        for (BaseFomponent sibling : siblings)
            currentIndex = sibling.visit(visitor, compiled, currentIndex);
        return currentIndex;
    }

}
