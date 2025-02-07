package org.figuramc.fwl.text.providers.headless;

import org.figuramc.fwl.text.FWLStyle;

public interface HeadlessStyleProvider {
    FWLStyle get(int index, int length);
}
