package org.figuramc.fwl.gui.widgets.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.figuramc.fwl.gui.themes.FWLTheme;
import org.figuramc.fwl.gui.widgets.descriptors.button.ButtonTypes;
import org.figuramc.fwl.text.components.AbstractComponent;
import org.figuramc.fwl.utils.RenderUtils;
import org.jetbrains.annotations.Nullable;

import static org.figuramc.fwl.FWL.fwl;
import static org.figuramc.fwl.utils.RenderUtils.textHeight;
import static org.figuramc.fwl.utils.RenderUtils.textWidth;

public class TextButton extends Button implements NarratableEntry {
    private AbstractComponent message;
    public TextButton(float x, float y, float width, float height, AbstractComponent message) {
        super(x, y, width, height);
        this.message = message;
    }

    @Override
    public void renderButton(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        if (message != null) {
            float textWidth = textWidth(message);
            float x = desc.x() + (desc.width() / 2) - (textWidth / 2);
            float y = desc.y() + (desc.height() / 2) - (textHeight(message) / 2);
            RenderUtils.renderText(graphics, message, x, y, 0);
        }
    }

    private Integer getTextColorForType(FWLTheme theme, String type) {
        if (desc.disabled()) return theme.getColor("text", type, "disabled");
        if (desc.clicked()) return theme.getColor("text", type, "clicked");
        if (desc.hovered()) return theme.getColor("text", type, "hovered");
        return theme.getColor("text", type);
    }

    private Integer getTextColorForType(FWLTheme theme, String type, int fallback) {
        if (desc.disabled()) return theme.getColorOrDefault(fallback, "text", type, "disabled");
        if (desc.clicked()) return theme.getColorOrDefault(fallback, "text", type, "clicked");
        if (desc.hovered()) return theme.getColorOrDefault(fallback, "text", type, "hovered");
        return theme.getColorOrDefault(fallback, "text", type);
    }

    public AbstractComponent message() {
        return message;
    }

    @Override
    public TextButton setCallback(OnClick callback) {
        super.setCallback(callback);
        return this;
    }

    public TextButton setMessage(AbstractComponent message) {
        this.message = message;
        return this;
    }

    @Override
    public TextButton setTooltip(@Nullable AbstractComponent tooltip) {
        super.setTooltip(tooltip);
        return this;
    }

    @Override
    public TextButton setX(float x) {
        super.setX(x);
        return this;
    }

    @Override
    public TextButton setY(float y) {
        super.setY(y);
        return this;
    }

    @Override
    public TextButton setWidth(float width) {
        super.setWidth(width);
        return this;
    }

    @Override
    public TextButton setHeight(float height) {
        super.setHeight(height);
        return this;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.FOCUSED;
    }

    @Override
    public void updateNarration(NarrationElementOutput builder) {
        if (message != null)
            builder.add(NarratedElementType.TITLE, message.toString());
    }
}
