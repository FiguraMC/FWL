package org.figuramc.fwl.gui.widgets.button.radio_button;

import org.figuramc.fwl.gui.widgets.button.RadioButton;
import org.jetbrains.annotations.Nullable;

public class RadioButtonGroupHandler implements RadioButton.ChangeCallback {
    private final RadioButton[] otherButtons;
    private final int index;
    private ChangeCallback callback;
    public RadioButtonGroupHandler(RadioButton[] otherButtons, int index, @Nullable ChangeCallback callback) {
        this.otherButtons = otherButtons;
        this.index = index;
        this.callback = callback;
    }

    @Override
    public void onValueChange(boolean active) {
        if (active) {
            for (RadioButton button: otherButtons) {
                button.setActive(false);
            }
            if (callback != null) callback.activeChanged(index);
        }
    }

    public static RadioButtonGroupHandler[] createHandlers(@Nullable ChangeCallback callback, RadioButton... buttons) {
        RadioButtonGroupHandler[] handlers = new RadioButtonGroupHandler[buttons.length];
        for (int i = 0; i < buttons.length; i++) {
            RadioButton[] otherButtons = new RadioButton[buttons.length-1];
            for (int j = 0; j < buttons.length - 1; j++) {
                RadioButton button = buttons[j >= i ? j + 1 : j];
                otherButtons[j] = button;
            }
            handlers[i] = new RadioButtonGroupHandler(otherButtons, i, callback);
        }
        return handlers;
    }

    public static void createHandlersAndApply(@Nullable ChangeCallback callback, RadioButton... buttons) {
        RadioButtonGroupHandler[] handlers = createHandlers(callback, buttons);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setCallback(handlers[i]);
        }
    }

    public interface ChangeCallback {
        void activeChanged(int index);
    }
}
