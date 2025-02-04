package org.figuramc.fwl.gui.widgets.input.handlers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import org.figuramc.fwl.gui.widgets.input.TextInput;

import java.util.function.Consumer;

public class IntegerInputHandler implements TextInput.Callback, TextInput.TextBaker {

    private Consumer<Integer> valueConsumer;
    private Component errorText;
    private boolean unsigned = false;
    private int radix = 10;

    public Consumer<Integer> valueConsumer() {
        return valueConsumer;
    }

    public IntegerInputHandler setValueConsumer(Consumer<Integer> valueConsumer) {
        this.valueConsumer = valueConsumer;
        return this;
    }

    public boolean unsigned() {
        return unsigned;
    }

    public IntegerInputHandler setUnsigned(boolean unsigned) {
        this.unsigned = unsigned;
        return this;
    }

    public int radix() {
        return radix;
    }

    public IntegerInputHandler setRadix(int radix) {
        this.radix = radix;
        return this;
    }

    @Override
    public void onValueChange(String value) {
        try {
            errorText = null;
            if (valueConsumer == null) return;
            int iValue;
            if (unsigned) iValue = Integer.parseUnsignedInt(value, radix);
            else iValue = Integer.parseInt(value, radix);
            valueConsumer.accept(iValue);
        } catch (NumberFormatException exception) {
            errorText = Component.literal("Unable to parse an integer");
        } catch (Exception e) {
            errorText = Component.literal("Unexpected exception: " + e.getMessage());
        }
    }

    @Override
    public Component getBakedText(String value) {
        if (errorText != null) {
            Style textStyle = Style.EMPTY
                    .withColor(ChatFormatting.RED)
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, errorText));
            return Component.literal(value).setStyle(textStyle);
        }
        else return Component.literal(value);
    }

}
