package org.figuramc.fwl.text.fomponents;

import org.figuramc.fwl.text.FomponentVisitor;
import org.figuramc.fwl.text.style.CompiledStyle;
import org.figuramc.fwl.text.style.FomponentStyle;

import java.util.List;

public class LiteralFomponent extends BaseFomponent {

    private final String text;
    private final int numCodepoints;

    public LiteralFomponent(String text, FomponentStyle style, List<BaseFomponent> siblings) {
        super(style, siblings);
        this.text = text;
        this.numCodepoints = text.codePointCount(0, text.length());
    }

    @Override
    protected int selfLength() {
        return numCodepoints;
    }

    @Override
    protected int selfVisit(FomponentVisitor visitor, CompiledStyle style, int currentIndex) {
        var iterator = text.codePoints().iterator();
        while (iterator.hasNext()) visitor.accept(currentIndex++, style, iterator.nextInt());
        return currentIndex;
    }
}
