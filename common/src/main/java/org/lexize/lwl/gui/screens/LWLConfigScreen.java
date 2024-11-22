package org.lexize.lwl.gui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lexize.lwl.LWL;
import org.lexize.lwl.gui.themes.LWLBreeze;
import org.lexize.lwl.gui.widgets.button.Checkbox;
import org.lexize.lwl.gui.widgets.button.RadioButton;
import org.lexize.lwl.gui.widgets.button.TextButton;

import static net.minecraft.network.chat.Component.literal;

public class LWLConfigScreen extends LWLScreen {
    private LWLBreeze breeze = new LWLBreeze();
    private TextButton button1, button2;
    private Checkbox checkbox1, checkbox2, checkbox3, checkbox4;
    private RadioButton radio1, radio2, radio3, radio4;

    public LWLConfigScreen(Screen prevScreen) {
        super(Component.translatable("lwl.config_screen"), prevScreen);
    }

    public LWLConfigScreen() {
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
        button2.setEnabled(false);
        checkbox3.setEnabled(false);
        checkbox4.setEnabled(false);
        radio3.setEnabled(false);
        radio4.setEnabled(false);
    }

    @Override
    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        LWL.pushTheme(breeze);
        super.render(graphics, mouseX, mouseY, delta);
        LWL.popTheme();
    }
}
