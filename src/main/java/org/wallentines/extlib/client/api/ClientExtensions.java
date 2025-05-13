package org.wallentines.extlib.client.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.Version;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.wallentines.extlib.impl.ExtensionMap;
import org.wallentines.extlib.impl.ExtensionMapHolder;

import java.util.Collections;
import java.util.Map;

/**
 * An interface for querying extensions on the client
 */
@Environment(EnvType.CLIENT)
public interface ClientExtensions {

    /**
     * Gets a map of extensions the client currently has enabled to their version
     * @return A map of extensions IDs to versions
     */
    static Map<ResourceLocation, Version> getExtensions() {
        ExtensionMap map = ((ExtensionMapHolder) Minecraft.getInstance()).getExtensionMap();
        if(map == null) return Collections.emptyMap();

        return map.extensions();
    }

    /**
     * Determines if the client currently has the given extension enabled
     * @param extension The extension to lookup
     * @return Whether the client currently has the extension enabled
     */
    static boolean hasExtension(ResourceLocation extension) {

        ExtensionMap map = ((ExtensionMapHolder) Minecraft.getInstance()).getExtensionMap();
        if(map == null || map.extensions().isEmpty()) return false;

        return map.extensions().containsKey(extension);
    }

    /**
     * Gets the version of the given extension the client currently has enabled, or null
     * @param extension The extension ID to lookup
     * @return The extension version, or null if the client doesn't have it enabled
     */
    @Nullable
    static Version getExtensionVersion(ResourceLocation extension) {

        ExtensionMap map = ((ExtensionMapHolder) Minecraft.getInstance()).getExtensionMap();
        if(map == null || map.extensions().isEmpty()) return null;

        return map.extensions().get(extension);
    }


}
