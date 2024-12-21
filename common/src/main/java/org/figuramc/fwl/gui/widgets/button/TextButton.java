package org.figuramc.fwl.gui.widgets.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.figuramc.fwl.FWL;
import org.figuramc.fwl.gui.themes.FWLTheme;
import org.figuramc.fwl.gui.widgets.descriptors.button.ButtonTypes;

import static org.figuramc.fwl.FWL.fwl;

public class TextButton extends Button implements NarratableEntry {
    private Component message;
    public TextButton(float x, float y, float width, float height, Component message) {
        super(x, y, width, height);
        this.message = message;
    }

    @Override
    public void renderButton(GuiGraphics graphics, float mouseX, float mouseY, float delta) {
        FWLTheme theme = fwl().currentTheme();
        if (message != null) {
            float textWidth = theme.textWidth(graphics, message, 1);
            float x = desc.x() + (desc.width() / 2) - (textWidth / 2);
            float y = desc.y() + (desc.height() / 2) - (theme.textHeight(graphics, message, 1, desc.width() - 2) / 2);
            int color = getTextColor(theme);
            FWLTheme.renderText(graphics, message, x, y, 0, 1, color, false);
        }
    }

    private int getTextColor(FWLTheme theme) {
        Integer color = getTextColorForType(theme, desc.type().getPath());
        return color != null ? color : getTextColorForType(theme, ButtonTypes.DEFAULT.getPath(), 0xFFFFFFFF);
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

    public Component message() {
        return message;
    }

    @Override
    public TextButton setCallback(OnClick callback) {
        super.setCallback(callback);
        return this;
    }

    public TextButton setMessage(Component message) {
        this.message = message;
        return this;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.FOCUSED;
    }

    @Override
    public void updateNarration(NarrationElementOutput builder) {
        if (message != null)
            builder.add(NarratedElementType.TITLE, message);
    }
}
