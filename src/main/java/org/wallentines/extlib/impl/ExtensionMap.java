package org.wallentines.extlib.impl;

import com.google.common.collect.Maps;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

@ApiStatus.Internal
public record ExtensionMap(Map<ResourceLocation, Version> extensions) {

    public static final ExtensionMap EMPTY = new ExtensionMap(Collections.emptyMap());

    public Map<ResourceLocation, VersionPredicate> test(Map<ResourceLocation, VersionPredicate> predicateMap) {

        Map<ResourceLocation, VersionPredicate> result = Maps.newHashMap();
        for(Map.Entry<ResourceLocation, VersionPredicate> ent : predicateMap.entrySet()) {
            VersionPredicate predicate = ent.getValue();
            Version ver = extensions.get(ent.getKey());
            if(ver == null || !predicate.test(ver)) {
                result.put(ent.getKey(), predicate);
            }
        }
        return result;
    }

    public ExtensionMap intersection(Collection<ResourceLocation> other) {

        HashMap<ResourceLocation, Version> intersection = Maps.newHashMap();
        for(ResourceLocation loc : other) {
            if(extensions.containsKey(loc)) {
                intersection.put(loc, extensions.get(loc));
            }
        }

        return new ExtensionMap(intersection);
    }

}
