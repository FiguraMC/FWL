package org.figuramc.fwl.text.fomponents;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.figuramc.fwl.text.FomponentVisitor;
import org.figuramc.fwl.text.style.CompiledStyle;
import org.figuramc.fwl.text.style.FomponentStyle;

import java.util.ArrayList;
import java.util.List;

public class LiteralFomponent extends BaseFomponent {

    private final String text;
    private final int numCodepoints;

    public LiteralFomponent(String text) {
        this(text, FomponentStyle.style());
    }
    public LiteralFomponent(String text, FomponentStyle style) {
        this(text, style, new ArrayList<>());
    }
    public LiteralFomponent(String text, FomponentStyle style, ArrayList<BaseFomponent> siblings) {
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

    @Override
    protected JsonObject getBaseJsonObject() {
        JsonObject base = new JsonObject();
        base.addProperty("type", "text");
        base.addProperty("text", text);
        return base;
    }

    public static LiteralFomponent literal(String text) { return new LiteralFomponent(text); }
    public static LiteralFomponent literal(String text, FomponentStyle style) { return new LiteralFomponent(text, style); }

}
