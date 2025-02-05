package org.figuramc.fwl.text.sinks;

import org.figuramc.fwl.text.providers.StyleProvider;

public interface StyledSink {
    boolean accept(int index, StyleProvider provider, int codepoint);
}
