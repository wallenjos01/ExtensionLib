package org.wallentines.extlib.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import org.semver4j.Semver;
import org.wallentines.extlib.impl.ExtensionMap;
import org.wallentines.extlib.impl.ExtensionMapHolder;

import java.util.Collections;
import java.util.Map;

/**
 * An interface for querying extension support for players connected to the server
 */
public interface PlayerExtensions {

    /**
     * Gets a list map of extensions IDs the given player has enabled to their versions
     * @param player The player to lookup
     * @return A map of extensions for a player
     */
    static Map<ResourceLocation, Semver> getExtensions(ServerPlayer player) {
        ExtensionMap map = ((ExtensionMapHolder) player).getExtensionMap();
        if(map == null) return Collections.emptyMap();

        return map.extensions();
    }

    /**
     * Determines if the given player has the given extension enabled
     * @param player The player to lookup
     * @param extensionId The extension ID to lookup
     * @return Whether the player has the extension enabled
     */
    static boolean hasExtension(ServerPlayer player, ResourceLocation extensionId) {

        ExtensionMap map = ((ExtensionMapHolder) player).getExtensionMap();
        if(map == null || map.extensions().isEmpty()) return false;

        return map.extensions().containsKey(extensionId);
    }

    /**
     * Gets the version of the given extension the given player has, or null
     * @param player The player to look up
     * @param extensionId The extension ID to lookup
     * @return The player's extension version, or null if they do not have it enabled
     */
    @Nullable
    static Semver getExtensionVersion(ServerPlayer player, ResourceLocation extensionId) {

        ExtensionMap map = ((ExtensionMapHolder) player).getExtensionMap();
        if(map == null || map.extensions().isEmpty()) return null;

        return map.extensions().get(extensionId);
    }

}
