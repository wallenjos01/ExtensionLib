package org.wallentines.extlib.api;

import net.minecraft.resources.Identifier;
import org.semver4j.Semver;
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
    static Map<Identifier, Semver> getAllExtensions() {
        return ExtensionRegistryImpl.ALL_EXTENSIONS;
    }

    /**
     * Registers a new extension with the given ID and version
     * @param id The extension ID
     * @param version The extension version
     */
    static void registerExtension(Identifier id, Semver version) {
        ExtensionRegistryImpl.ALL_EXTENSIONS.putIfAbsent(id, version);
    }

    /**
     * A utility for creating a version object in the format "major.minor.patch"
     * @param major The major version number
     * @param minor The minor version number
     * @param patch The patch number
     * @return A new Version
     */
    static Semver makeVersion(int major, int minor, int patch) {
        return Semver.of(major, minor, patch);
    }

}
