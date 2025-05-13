package org.wallentines.extlib.api;

import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.resources.ResourceLocation;
import org.wallentines.extlib.impl.ExtensionRegistryImpl;

import java.util.Map;

/**
 * An interface for managing registered extensions for this
 */
public interface ExtensionRegistry {

    /**
     * Gets a map of all registered extensions to their versions
     * @return A map of all registered extensions
     */
    static Map<ResourceLocation, Version> getAllExtensions() {
        return ExtensionRegistryImpl.ALL_EXTENSIONS;
    }

    /**
     * Registers a new extension with the given ID and version
     * @param id The extension ID
     * @param version The extension version
     */
    static void registerExtension(ResourceLocation id, Version version) {
        ExtensionRegistryImpl.ALL_EXTENSIONS.putIfAbsent(id, version);
    }

    /**
     * A utility for creating a version object in the format "major.minor.patch"
     * @param major The major version number
     * @param minor The minor version number
     * @param patch The patch number
     * @return A new Version
     */
    static Version makeVersion(int major, int minor, int patch) {
        try {
            return Version.parse("%d.%d.%d".formatted(major, minor, patch));
        } catch (VersionParsingException ex) {
            throw new RuntimeException(ex);
        }
    }

}
