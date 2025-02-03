package org.figuramc.fwl.fabric.entries.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.figuramc.fwl.FWL;
import org.figuramc.fwl.utils.Pair;

import java.util.HashSet;
import java.util.Set;

public class FabricEntryPointManager {
    public static <T> Set<Pair<String, T>> load(String name, Class<T> clazz) {
        Set<Pair<String, T>> ret = new HashSet<>();
        for (EntrypointContainer<T> entrypoint : FabricLoader.getInstance().getEntrypointContainers(name, clazz)) {
            ModMetadata metadata = entrypoint.getProvider().getMetadata();
            String modId = metadata.getId();
            try {
                ret.add(Pair.of(modId, entrypoint.getEntrypoint()));
            } catch (Exception e) {
                FWL.fwl().LOGGER.error("Failed to load {} entrypoint of mod {}", name, modId, e);
            }
        }

        return ret;
    }
}
