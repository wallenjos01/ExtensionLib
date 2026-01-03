package org.wallentines.extlib.impl;

import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.semver4j.RangesList;

import java.util.Map;

@ApiStatus.Internal
public interface ExtensionPredicateHolder extends ExtensionMapHolder {

    Map<Identifier, RangesList> getExtensionPredicates();

    void setExtensionPredicates(Map<Identifier, RangesList> predicates);

}
