package org.figuramc.fwl.fabric;

import net.fabricmc.api.ClientModInitializer;
import org.figuramc.fwl.FWL;

public class FWLFabric extends FWL implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        init();
    }
}
