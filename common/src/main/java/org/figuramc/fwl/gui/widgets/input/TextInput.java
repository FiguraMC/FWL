package org.figuramc.fwl.gui.widgets.input;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.figuramc.fwl.gui.themes.ColorTypes;
import org.figuramc.fwl.gui.themes.FWLTheme;
import org.figuramc.fwl.gui.widgets.FWLWidget;
import org.figuramc.fwl.gui.widgets.Update;
import org.figuramc.fwl.gui.widgets.descriptors.input.TextInputDescriptor;
import org.figuramc.fwl.text.components.AbstractComponent;
import org.figuramc.fwl.utils.Rectangle;
import org.figuramc.fwl.utils.RenderUtils;
import org.figuramc.fwl.utils.Scissors;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

import static org.figuramc.fwl.FWL.fwl;
import static org.figuramc.fwl.text.components.LiteralComponent.literal;
import static org.figuramc.fwl.utils.RenderUtils.textWidth;
import static org.figuramc.fwl.utils.MathUtils.clamp;
import static org.figuramc.fwl.utils.TextUtils.findCursorJumpAfter;
import static org.figuramc.fwl.utils.TextUtils.findCursorJumpBefore;
import static org.lwjgl.glfw.GLFW.*;

public class TextInput implements FWLWidget, Update {
    private final TextInputDescriptor desc;

    private int startCursorPos, endCursorPos;
    private String value;
    protected AbstractComponent bakedText;
    private boolean immutable;

    private Callback changeCallback;
    private TextBaker textBaker;
    private Consumer<TextInput> updater;
    private boolean useCallback = true;

    private int cursorBlinkCounter = 0;
    private float textOffset = 0;

    public TextInput(float x, float y, float width, float height) {
        this(x, y, width, height, "");
    }

    public TextInput(float x, float y, float width, float height, @NotNull String value) {
        desc = new TextInputDescriptor(x, y, width, height);
        setValue(value);
    }

    public TextInput setValue(String value) {
        boolean valueIsDifferent = !value.equals(this.value);
        this.value = Objects.requireNonNull(value, "Initial input value has to be not null");
        if (changeCallback != null && valueIsDifferent && useCallback) changeCallback.onValueChange(this.value);
        bakedText = textBaker != null ? textBaker.getBakedText(value) : literal(value);
        return this;
    }

    @Override
    public Rectangle boundaries() {
        return desc.boundaries();
    }

    @Override
    public void setFocused(boolean focused) {
        desc.setFocused(focused);
    }

    @Override
    public boolean isFocused() {
        return desc.focused();
    }

    public TextInput setImmutable(boolean immutable) {
        this.immutable = immutable;
        return this;
    }

    public boolean immutable() {
        return immutable;
    }

    public int startCursorPos() {
        return startCursorPos;
    }

    public int endCursorPos() {
        return endCursorPos;
    }

    public TextInput setStartCursorPos(int pos) {
        this.startCursorPos = clamp(pos, 0, value.length());
        cursorBlinkCounter = 0;
        return this;
    }

    public TextInput setEndCursorPos(int pos) {
        this.endCursorPos = clamp(pos, 0, value.length());
        cursorBlinkCounter = 0;
        return this;
    }

    public TextInput setCursorPos(int pos) {
        int dst = clamp(pos, 0, value.length());
        startCursorPos = endCursorPos = dst;
        cursorBlinkCounter = 0;
        return this;
    }

    public Callback valueChangeCallback() {
        return changeCallback;
    }

    public TextInput setChangeCallback(Callback changeCallback) {
        this.changeCallback = changeCallback;
        return this;
    }

    public TextBaker textBaker() {
        return textBaker;
    }

    public TextInput setTextBaker(TextBaker textBaker) {
        this.textBaker = textBaker;
        return this;
    }

    public boolean useCallback() {
        return useCallback;
    }

    public TextInput setUseCallback(boolean useCallback) {
        this.useCallback = useCallback;
        return this;
    }

    @Override
    public void update() {
        if (updater != null) updater.accept(this);
    }

    public Consumer<TextInput> updater() {
        return updater;
    }

    public TextInput setUpdater(Consumer<TextInput> updater) {
        this.updater = updater;
        return this;
    }

    @Override
    public void render(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        Font font = Minecraft.getInstance().font;

        FWLTheme theme = fwl().currentTheme();

        float x = desc.x(), y = desc.y(), width = desc.width(), height = desc.height();

        theme.renderTextInput(graphics, delta, desc);

        float textYPos = y + ((height - font.lineHeight) / 2);
        float textX1 = x + 2;
        float textWidth = width - 4;

        Scissors.enableScissors(graphics, textX1, y, textWidth, height);

        float selectionWidth = textWidth(selectedText(), 1);
        float selectionX1 = textWidth(textBeforeSelection(), 1);
        float selectionX2 = selectionX1 + selectionWidth;
        float contentWidth = textWidth(value, 1);

        float selectionX = startCursorPos > endCursorPos ? selectionX1 : selectionX2;
        float currentCursorX = (selectionX + textOffset);
        if (currentCursorX >= textWidth) {
            textOffset = textWidth - selectionX - 1;
        }
        else if (currentCursorX < 0) {
            textOffset = textOffset - currentCursorX;
        }
        textOffset = clamp(textOffset, -Math.max(0, (contentWidth + 1) - textWidth), 0);

        float cursorX1 = selectionX1 + textX1 + textOffset;
        float cursorY1 = textYPos - 1;
        float cursorY2 = cursorY1 + font.lineHeight + 1;

        boolean renderCursor = isFocused() && (Math.max(0, cursorBlinkCounter - 5) / 10) % 2 == 0;

        int cursorColor = 0xFFAAAAAA;

        if (startCursorPos == endCursorPos) {
            if (renderCursor) RenderUtils.fill(graphics, cursorX1, cursorY1, cursorX1 + 1, cursorY2, 0, cursorColor);
        }
        else {
            int selectionColor = theme.getColor(ColorTypes.SECONDARY);

            float cursorX2 = cursorX1 + selectionWidth;
            RenderUtils.fill(graphics, cursorX1, cursorY1, cursorX2, cursorY2, 0, selectionColor);
            if (renderCursor) {
                float cursorX = startCursorPos > endCursorPos ? cursorX1 : cursorX2;
                RenderUtils.fill(graphics, cursorX, cursorY1, cursorX + 1, cursorY2, 0, cursorColor);
            }
        }
        RenderUtils.renderText(graphics, bakedText, textX1 + textOffset, textYPos, 0);
        Scissors.disableScissors(graphics);
        if (desc.boundaries().pointIn(mouseX, mouseY)) {
            RenderUtils.renderTextTooltip(graphics, bakedText, textX1 + textOffset, textYPos, mouseX, mouseY);
        }
    }

    @Override
    public void tick() {
        cursorBlinkCounter++;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (isFocused()) {
            boolean isControlDown = (modifiers & GLFW_MOD_CONTROL) == GLFW_MOD_CONTROL;
            boolean isShiftDown = (modifiers & GLFW_MOD_SHIFT) == GLFW_MOD_SHIFT;
            boolean isAltDown = (modifiers & GLFW_MOD_ALT) == GLFW_MOD_ALT;

            switch (keyCode) {
                case GLFW_KEY_LEFT -> {
                    int eDst = Math.max(0, isControlDown ? findCursorJumpBefore(value, endCursorPos) : endCursorPos - 1);
                    int sDst = Math.max(0, isControlDown ? findCursorJumpBefore(value, startCursorPos) : startCursorPos - 1);
                    if (isShiftDown) {
                        if (isAltDown) setStartCursorPos(sDst);
                        else setEndCursorPos(eDst);
                    }
                    else {
                        if (startCursorPos != endCursorPos) setCursorPos(Math.min(startCursorPos, endCursorPos));
                        else setCursorPos(sDst);
                    }
                    return true;
                }

                case GLFW_KEY_RIGHT -> {
                    int eDst = Math.min(value.length(), isControlDown ? findCursorJumpAfter(value, endCursorPos) : endCursorPos + 1);
                    int sDst = Math.min(value.length(), isControlDown ? findCursorJumpAfter(value, startCursorPos) : startCursorPos + 1);
                    if (isShiftDown) {
                        if (isAltDown) setStartCursorPos(sDst);
                        else setEndCursorPos(eDst);
                    }
                    else {
                        if (startCursorPos != endCursorPos) setCursorPos(Math.max(startCursorPos, endCursorPos));
                        else setCursorPos(eDst);
                    }
                    return true;
                }

                case GLFW_KEY_C -> {
                    if (!isControlDown) return false;
                    String valueForCopy = selectedText();
                    FWLWidget.setClipboard(valueForCopy);
                    return true;
                }

                case GLFW_KEY_V -> {
                    if (immutable || !isControlDown) return false;
                    String clipboard = FWLWidget.getClipboard();
                    setValue(textBeforeSelection() + clipboard + textAfterSelection());
                    setCursorPos(endCursorPos + clipboard.length());
                    return true;
                }

                case GLFW_KEY_X -> {
                    if (!isControlDown) return false;
                    String valueForCopy = selectedText();
                    FWLWidget.setClipboard(valueForCopy);
                    if (!immutable) {
                        setValue(textBeforeSelection() + textAfterSelection());
                        setCursorPos(Math.min(startCursorPos, endCursorPos));
                    }
                    return true;
                }

                case GLFW_KEY_A -> {
                    if (isControlDown) {
                        startCursorPos = 0;
                        endCursorPos = value.length();
                        return true;
                    }
                }

                case GLFW_KEY_BACKSPACE -> {
                    if (immutable) return false;
                    String left = textBeforeSelection();
                    String right = textAfterSelection();
                    if (startCursorPos == endCursorPos) {
                        int dst = Math.max(isControlDown ? findCursorJumpBefore(left, endCursorPos) : left.length() - 1, 0);
                        String newLeft = left.substring(0, dst);
                        setValue(newLeft + right);
                        setCursorPos(newLeft.length());
                    }
                    else {
                        setValue(left + right);
                        setCursorPos(endCursorPos);
                    }
                }

                case GLFW_KEY_DELETE -> {
                    if (immutable) return false;
                    String left = textBeforeSelection();
                    String right = textAfterSelection();
                    if (startCursorPos == endCursorPos) {
                        int dst = Math.max(isControlDown ? findCursorJumpAfter(right, endCursorPos - left.length()) : 1, 0);
                        String newRight = right.substring(Math.min(dst, right.length()));
                        setValue(left + newRight);
                    }
                    else {
                        setValue(left + right);
                    }
                    setCursorPos(endCursorPos);
                }
            }
        }
        return false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (isFocused() && !immutable) {
            String before = textBeforeSelection();
            String after = textAfterSelection();
            String leftPart = before + chr;
            setValue(before + chr + after);
            setCursorPos(leftPart.length());
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return isMouseOver(mouseX, mouseY);
    }

    private String selectedText() {
        int cursor1 = clamp(Math.min(startCursorPos, endCursorPos), 0, value.length());
        int cursor2 = clamp(Math.max(startCursorPos, endCursorPos), 0, value.length());
        return value.substring(cursor1, cursor2);
    }

    private String textBeforeSelection() {
        int cursor = clamp(Math.min(startCursorPos, endCursorPos), 0, value.length());
        return value.substring(0, cursor);
    }

    private String textAfterSelection() {
        int cursor = clamp(Math.max(startCursorPos, endCursorPos), 0, value.length());
        return value.substring(cursor);
    }

    public interface Callback {
        void onValueChange(String value);
    }

    public interface TextBaker {
        AbstractComponent getBakedText(String value);
    }
}
