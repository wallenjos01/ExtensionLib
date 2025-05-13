package org.wallentines.extlib.impl;

import net.fabricmc.loader.api.Version;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

@ApiStatus.Internal
public class ExtensionRegistryImpl {

    public static final Map<ResourceLocation, Version> ALL_EXTENSIONS = new HashMap<>();

}
