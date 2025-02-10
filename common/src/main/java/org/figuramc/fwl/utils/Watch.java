package org.figuramc.fwl.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Watch {
    private Instant initial;
    public Watch() {
        reset();
    }

    public void reset() {
        initial = Instant.now();
    }

    public long timeSince(ChronoUnit unit) {
        Instant now = Instant.now();
        return unit.between(initial, now);
    }
}
