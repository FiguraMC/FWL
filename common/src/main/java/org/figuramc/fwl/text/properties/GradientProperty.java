package org.figuramc.fwl.text.properties;

import org.figuramc.fwl.text.style.CompiledStyle;
import org.figuramc.fwl.text.fomponents.BaseFomponent;

public record GradientProperty<T>(Property<T> start, Property<T> end, Lerper<T> lerper) implements Property<T> {
    @Override
    public CompiledStyle.CompiledProperty<T> compile(int currentIndex, BaseFomponent fomponent) {
        int len = fomponent.length();
        CompiledStyle.CompiledProperty<T> compiledStart = start.compile(currentIndex, fomponent);
        CompiledStyle.CompiledProperty<T> compiledEnd = end.compile(currentIndex, fomponent);
        return (i, p) -> lerper.lerp((i + p - currentIndex) / len, compiledStart.fetch(i, p), compiledEnd.fetch(i, p));
    }
    @Override public boolean varies() { return true; }

    @FunctionalInterface
    public interface Lerper<T> {
        T lerp(float delta, T start, T end);
    }
}