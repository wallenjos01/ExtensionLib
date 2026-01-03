package org.wallentines.extlib.impl;

import com.google.common.collect.Maps;

import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.semver4j.RangesList;
import org.semver4j.Semver;

import java.util.*;

@ApiStatus.Internal
public record ExtensionMap(Map<Identifier, Semver> extensions) {

    public static final ExtensionMap EMPTY = new ExtensionMap(Collections.emptyMap());

    public Map<Identifier, RangesList> test(Map<Identifier, RangesList> predicateMap) {

        Map<Identifier, RangesList> result = Maps.newHashMap();
        for(Map.Entry<Identifier, RangesList> ent : predicateMap.entrySet()) {
            RangesList predicate = ent.getValue();
            Semver ver = extensions.get(ent.getKey());
            if(ver == null || !ver.satisfies(predicate)) {
                result.put(ent.getKey(), predicate);
            }
        }
        return result;
    }

    public ExtensionMap intersection(Collection<Identifier> other) {

        HashMap<Identifier, Semver> intersection = Maps.newHashMap();
        for(Identifier loc : other) {
            if(extensions.containsKey(loc)) {
                intersection.put(loc, extensions.get(loc));
            }
        }

        return new ExtensionMap(intersection);
    }

}
