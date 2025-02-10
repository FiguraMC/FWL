package org.figuramc.fwl.text;

import org.figuramc.fwl.text.sinks.StyledSink;

public interface FWLCharSequence {
    boolean accept(StyledSink sink);
}
