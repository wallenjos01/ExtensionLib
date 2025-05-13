package org.wallentines.extlib.impl;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.semver4j.RangesList;

import java.util.Map;

@ApiStatus.Internal
public interface ExtensionPredicateHolder extends ExtensionMapHolder {

    Map<ResourceLocation, RangesList> getExtensionPredicates();

    void setExtensionPredicates(Map<ResourceLocation, RangesList> predicates);

}
