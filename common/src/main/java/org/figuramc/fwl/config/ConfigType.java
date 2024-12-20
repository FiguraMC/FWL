package org.figuramc.fwl.config;

import com.google.gson.JsonElement;
import org.figuramc.fwl.gui.screens.FWLScreen;
import org.figuramc.fwl.gui.widgets.FWLWidget;

public interface ConfigType {
    FWLWidget getWidget(JsonElement preset, FWLScreen parentScreen, float x, float y);
}
