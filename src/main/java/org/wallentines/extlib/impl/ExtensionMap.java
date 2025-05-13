package org.wallentines.extlib.impl;

import com.google.common.collect.Maps;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.semver4j.RangesList;
import org.semver4j.Semver;

import java.util.*;

@ApiStatus.Internal
public record ExtensionMap(Map<ResourceLocation, Semver> extensions) {

    public static final ExtensionMap EMPTY = new ExtensionMap(Collections.emptyMap());

    public Map<ResourceLocation, RangesList> test(Map<ResourceLocation, RangesList> predicateMap) {

        Map<ResourceLocation, RangesList> result = Maps.newHashMap();
        for(Map.Entry<ResourceLocation, RangesList> ent : predicateMap.entrySet()) {
            RangesList predicate = ent.getValue();
            Semver ver = extensions.get(ent.getKey());
            if(ver == null || !ver.satisfies(predicate)) {
                result.put(ent.getKey(), predicate);
            }
        }
        return result;
    }

    public ExtensionMap intersection(Collection<ResourceLocation> other) {

        HashMap<ResourceLocation, Semver> intersection = Maps.newHashMap();
        for(ResourceLocation loc : other) {
            if(extensions.containsKey(loc)) {
                intersection.put(loc, extensions.get(loc));
            }
        }

        return new ExtensionMap(intersection);
    }

}
