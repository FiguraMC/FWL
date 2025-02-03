package org.figuramc.fwl.gui.screens;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.figuramc.fwl.FWL;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.gui.widgets.input.TextInput;
import org.figuramc.fwl.gui.widgets.input.handlers.IntegerInputHandler;
import org.figuramc.fwl.gui.widgets.misc.ContextMenu;
import org.figuramc.fwl.gui.widgets.scrollable.ScrollBar;
import org.figuramc.fwl.gui.widgets.button.Checkbox;
import org.figuramc.fwl.gui.widgets.button.RadioButton;
import org.figuramc.fwl.gui.widgets.button.radio_button.RadioButtonGroupHandler;
import org.figuramc.fwl.gui.themes.FWLBreeze;
import org.figuramc.fwl.gui.widgets.button.TextButton;
import org.figuramc.fwl.gui.widgets.descriptors.Orientation;
import org.figuramc.fwl.gui.widgets.scrollable.ScrollableArea;
import org.figuramc.fwl.gui.widgets.tabs.SideViewSwitcher;
import org.figuramc.fwl.gui.widgets.tabs.pages.PageEntry;
import org.figuramc.fwl.gui.widgets.tabs.pages.SimplePage;
import org.figuramc.fwl.utils.Rectangle;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.network.chat.Component.literal;

public class FWLConfigScreen extends FWLScreen {
    private SideViewSwitcher configSwitcher;

    public FWLConfigScreen(Screen prevScreen) {
        super(Component.translatable("fwl.config_screen"), prevScreen);
    }

    public FWLConfigScreen() {
        this((Screen) null);
    }

    @Override
    protected void init() {
        super.init();
        configSwitcher = new SideViewSwitcher(0, 0, width, height)
                .setEntryHeight(16)
                .setExpandedWidth(100)
                .addEntry(new PageEntry() {
                    @Override
                    public FWLWidget getPage(float width, float height) {
                        return FWL.fwl().currentTheme().settingsRootWidget(width, height);
                    }

                    @Override
                    public Component getTitle() {
                        return Component.translatable("fwl.config.theme_settings");
                    }

                    @Override
                    public @Nullable Component getTooltip() {
                        return Component.translatable("fwl.config.theme_settings.tooltip");
                    }
                });
        addWidget(configSwitcher);
    }
}
