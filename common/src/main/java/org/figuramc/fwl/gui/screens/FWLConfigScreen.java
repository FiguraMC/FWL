package org.figuramc.fwl.gui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.figuramc.fwl.gui.widgets.ScrollBar;
import org.figuramc.fwl.gui.widgets.button.Checkbox;
import org.figuramc.fwl.gui.widgets.button.RadioButton;
import org.figuramc.fwl.gui.widgets.button.radio_button.RadioButtonGroupHandler;
import org.figuramc.fwl.FWL;
import org.figuramc.fwl.gui.themes.FWLBreeze;
import org.figuramc.fwl.gui.widgets.button.TextButton;
import org.figuramc.fwl.gui.widgets.descriptors.Orientation;

import static net.minecraft.network.chat.Component.literal;

public class FWLConfigScreen extends FWLScreen {
    private FWLBreeze breeze = new FWLBreeze();
    private TextButton button1, button2;
    private Checkbox checkbox1, checkbox2, checkbox3, checkbox4;
    private RadioButton radio1, radio2, radio3, radio4;
    private ScrollBar scrollBar1, scrollBar2;

    public FWLConfigScreen(Screen prevScreen) {
        super(Component.translatable("fwl.config_screen"), prevScreen);
    }

    public FWLConfigScreen() {
        this((Screen) null);
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(button1 = new TextButton(10, 10, 125, 20, literal("Button")));
        addRenderableWidget(button2 = new TextButton(10, 35, 125, 20, literal("Disabled button")));
        addRenderableWidget(checkbox1 = new Checkbox(145, 15, 10, 10, false));
        addRenderableWidget(checkbox2 = new Checkbox(165, 15, 10, 10, true));
        addRenderableWidget(checkbox3 = new Checkbox(145, 40, 10, 10, false));
        addRenderableWidget(checkbox4 = new Checkbox(165, 40, 10, 10, true));
        addRenderableWidget(radio1 = new RadioButton(185, 15, 10, 10, false));
        addRenderableWidget(radio2 = new RadioButton(205, 15, 10, 10, true));
        addRenderableWidget(radio3 = new RadioButton(185, 40, 10, 10, false));
        addRenderableWidget(radio4 = new RadioButton(205, 40, 10, 10, true));
        addRenderableWidget(scrollBar1 = new ScrollBar(225, 10, 10, 50, 50, 500, 0, Orientation.VERTICAL));
        addRenderableWidget(scrollBar2 = new ScrollBar(10, 60, 215, 10, 50, 500, 0, Orientation.HORIZONTAL));
        button2.setEnabled(false);
        checkbox3.setEnabled(false);
        checkbox4.setEnabled(false);
        radio3.setEnabled(false);
        radio4.setEnabled(false);
        RadioButtonGroupHandler.createHandlersAndApply((i) -> System.out.printf("Current active radio button: %s%n", i), radio1, radio2);
    }

    @Override
    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        FWL.pushTheme(breeze);
        super.render(graphics, mouseX, mouseY, delta);
        FWL.popTheme();
    }
}
