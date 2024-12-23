package org.figuramc.fwl.gui.widgets.tabs.pages;

import net.minecraft.network.chat.Component;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SimplePage implements PageEntry {
    private Component title;
    private FWLWidget page;

    public SimplePage(@NotNull Component title, FWLWidget page) {
        this.page = page;
        setTitle(title);
    }

    @Override
    public FWLWidget getPage(float width, float height) {
        return page;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public @Nullable Component getTooltip() {
        return null;
    }

    public Component title() {
        return title;
    }

    public SimplePage setTitle(Component title) {
        this.title = Objects.requireNonNull(title, "Title of page entry can't be null");
        return this;
    }

    public FWLWidget page() {
        return page;
    }

    public SimplePage setPage(FWLWidget page) {
        this.page = page;
        return this;
    }
}
