package org.figuramc.fwl.text.sinks;

import org.figuramc.fwl.text.providers.OffsetProvider;
import org.figuramc.fwl.text.providers.StyleProvider;

public class OffsetSink implements StyledSink {
    private final StyleProvider parentProvider;
    private final StyledSink parentSink;
    private final int offset;

    public OffsetSink(StyleProvider parentStyleProvider, StyledSink parentSink, int offset) {
        this.parentProvider = parentStyleProvider;
        this.parentSink = parentSink;
        this.offset = offset;
    }

    @Override
    public boolean accept(int index, StyleProvider provider, int codepoint) {
        parentSink.accept(index + offset, new OffsetProvider(parentProvider, provider, offset), codepoint);
        return true;
    }
}
