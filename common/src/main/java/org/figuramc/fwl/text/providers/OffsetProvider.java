package org.figuramc.fwl.text.providers;

import org.figuramc.fwl.text.FWLStyle;

public class OffsetProvider implements StyleProvider {
    private final StyleProvider parentProvider;
    private final StyleProvider thisProvider;
    private final int offset;

    public OffsetProvider(StyleProvider parentProvider, StyleProvider thisProvider, int offset) {
        this.parentProvider = parentProvider;
        this.thisProvider = thisProvider;
        this.offset = offset;
    }

    @Override
    public FWLStyle get(int index) {
        return parentProvider.get(index).applyFrom(thisProvider.get(index - offset));
    }
}
