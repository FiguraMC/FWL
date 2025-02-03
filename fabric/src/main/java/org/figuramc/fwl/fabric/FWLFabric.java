package org.figuramc.fwl.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import org.figuramc.fwl.FWL;
import org.figuramc.fwl.entries.ThemeRegistrar;
import org.figuramc.fwl.fabric.entries.fabric.FabricEntryPointManager;
import org.figuramc.fwl.utils.Pair;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FWLFabric extends FWL implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        init();
    }

    @Override
    protected Set<Pair<String, ThemeRegistrar>> getThemeRegistrars() {
        return FabricEntryPointManager.load("fwl_registrars", ThemeRegistrar.class);
    }
}
