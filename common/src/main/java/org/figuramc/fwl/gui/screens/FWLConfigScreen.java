package org.figuramc.fwl.gui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.figuramc.fwl.gui.widgets.input.TextInput;
import org.figuramc.fwl.gui.widgets.misc.ContextMenu;
import org.figuramc.fwl.gui.widgets.scrollable.ScrollBar;
import org.figuramc.fwl.gui.widgets.button.Checkbox;
import org.figuramc.fwl.gui.widgets.button.RadioButton;
import org.figuramc.fwl.gui.widgets.button.radio_button.RadioButtonGroupHandler;
import org.figuramc.fwl.FWL;
import org.figuramc.fwl.gui.themes.FWLBreeze;
import org.figuramc.fwl.gui.widgets.button.TextButton;
import org.figuramc.fwl.gui.widgets.descriptors.Orientation;
import org.figuramc.fwl.gui.widgets.scrollable.ScrollableArea;
import org.figuramc.fwl.gui.widgets.tabs.SideViewSwitcher;
import org.figuramc.fwl.gui.widgets.tabs.pages.SimplePage;
import org.figuramc.fwl.utils.Rectangle;

import static net.minecraft.network.chat.Component.literal;
import static org.figuramc.fwl.FWL.fwl;

public class FWLConfigScreen extends FWLScreen {
    private FWLBreeze breeze = new FWLBreeze();
    private TextButton button1, button2, contextTestButton;
    private Checkbox checkbox1, checkbox2, checkbox3, checkbox4;
    private RadioButton radio1, radio2, radio3, radio4;
    private ScrollBar scrollBar1, scrollBar2;
    private ScrollableArea area1;
    private TextInput input1;
    private SideViewSwitcher sideViewSwitcher;

    public FWLConfigScreen(Screen prevScreen) {
        super(Component.translatable("fwl.config_screen"), prevScreen);
    }

    public FWLConfigScreen() {
        this((Screen) null);
    }

    @Override
    protected void init() {
        super.init();
        addWidget(button1 = new TextButton(10, 10, 125, 20, literal("Button")));
        addWidget(button2 = new TextButton(10, 35, 125, 20, literal("Disabled button")));

        addWidget(checkbox1 = new Checkbox(145, 15, 10, 10, false));
        addWidget(checkbox2 = new Checkbox(165, 15, 10, 10, true));
        addWidget(checkbox3 = new Checkbox(145, 40, 10, 10, false));
        addWidget(checkbox4 = new Checkbox(165, 40, 10, 10, true));

        addWidget(radio1 = new RadioButton(185, 15, 10, 10, false));
        addWidget(radio2 = new RadioButton(205, 15, 10, 10, true));
        addWidget(radio3 = new RadioButton(185, 40, 10, 10, false));
        addWidget(radio4 = new RadioButton(205, 40, 10, 10, true));

        addWidget(contextTestButton = new TextButton(225, 10, 125, 20, literal("Entry 1")).setCallback(
                (x, y, b) -> {
                    Rectangle bounds = contextTestButton.boundaries();
                    ContextMenu menu = new ContextMenu(FWLConfigScreen.this, bounds.left(), bounds.bottom())
                            .addEntry(new ContextMenu.StandardEntry(literal("Entry 1"), (m) -> {
                                contextTestButton.setMessage(literal("Entry 1"));
                                return false;
                            }))
                            .addEntry(new ContextMenu.StandardEntry(literal("Entry 2"), (m) -> {
                                contextTestButton.setMessage(literal("Entry 2"));
                                return false;
                            }))
                            .addEntry(new ContextMenu.StandardEntry(literal("Entry 3"), (m) -> {
                                contextTestButton.setMessage(literal("Entry 3"));
                                return false;
                            }))
                            .setInteractionPriority(100);
                    addWidget(menu);
                    setFocused(menu);
                }
        ));
        addWidget(input1 = new TextInput(225, 40, 125, 20, "This is a very long text that is for sure won't wit the text input width because this is done intentionally in order to test this thing"));

        addWidget(area1 = new ScrollableArea(10, 80, 100, 100));
        addWidget(scrollBar1 = new ScrollBar(110, 80, 10, 100, 100, 200, 0, Orientation.VERTICAL).setScrollStep(10));
        addWidget(scrollBar2 = new ScrollBar(10, 180, 100, 10, 100, 200, 0, Orientation.HORIZONTAL).setScrollStep(10));
        scrollBar1.setCallback(area1::setOffsetY);
        scrollBar2.setCallback(area1::setOffsetX);

        for (int i = 0; i < 20; i++) {
            float bX = 100 * (i % 2);
            float bY = 20 * (float)(i / 2);
            TextButton button = new TextButton(area1.x() + bX, area1.y() + bY, 100, 20, literal("Button %s".formatted(i)));
            area1.addWidget(button);
        }

        addWidget(sideViewSwitcher = new SideViewSwitcher(145, 70, 200, 100)
                .addEntry(new SimplePage(literal("Page 1"), new TextButton(0, 0, 100, 20, literal("Button"))))
                .addEntry(new SimplePage(literal("Page 2"), new Checkbox(0, 0, 10, 10, true)))
                .addEntry(new SimplePage(literal("Page 3"), new TextInput(0, 0, 150, 20)))
                .addEntry(new SimplePage(literal("Page 4"), null))
                .setCollapsed(true)
        );

        button2.setEnabled(false);
        checkbox3.setEnabled(false);
        checkbox4.setEnabled(false);
        radio3.setEnabled(false);
        radio4.setEnabled(false);

        RadioButtonGroupHandler.createHandlersAndApply((i) -> System.out.printf("Current active radio button: %s%n", i), radio1, radio2);
    }
}
