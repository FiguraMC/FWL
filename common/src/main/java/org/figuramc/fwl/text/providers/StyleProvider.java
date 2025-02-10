package org.figuramc.fwl.text.providers;

import org.figuramc.fwl.text.FWLStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StyleProvider {
    @Nullable FWLStyle get(int index);

    default @NotNull FWLStyle getOrEmpty(int index) {
        FWLStyle style = get(index);
        return style == null ? FWLStyle.EMPTY : style;
    }
}
