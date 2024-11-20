package org.lexize.lwl.gui.widgets;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;

public interface LWLWidget extends GuiEventListener, Renderable, Tickable, NarratableEntry {
    @Override
    default NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    default void updateNarration(NarrationElementOutput builder) {

    }
}
