package org.wallentines.extlib.impl;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.semver4j.Semver;

import java.util.HashMap;
import java.util.Map;

@ApiStatus.Internal
public class ExtensionRegistryImpl {

    public static final Map<ResourceLocation, Semver> ALL_EXTENSIONS = new HashMap<>();

}
