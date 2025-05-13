package org.wallentines.extlib.impl;

import net.fabricmc.loader.api.metadata.version.VersionPredicate;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

@ApiStatus.Internal
public interface ExtensionPredicateHolder extends ExtensionMapHolder {

    Map<ResourceLocation, VersionPredicate> getExtensionPredicates();

    void setExtensionPredicates(Map<ResourceLocation, VersionPredicate> predicates);

}
