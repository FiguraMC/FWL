package org.figuramc.fwl.text;

import org.figuramc.fwl.text.style.CompiledStyle;

@FunctionalInterface
public interface FomponentVisitor {
    void accept(int index, CompiledStyle style, int codepoint);
}
