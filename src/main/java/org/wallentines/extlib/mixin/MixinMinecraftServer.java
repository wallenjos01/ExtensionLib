package org.wallentines.extlib.mixin;

import net.fabricmc.loader.api.metadata.version.VersionPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.wallentines.extlib.impl.ExtensionPredicateHolder;

import java.util.Collections;
import java.util.Map;

@Mixin(MinecraftServer.class)
@Implements(@Interface(iface= ExtensionPredicateHolder.class, prefix = "extlib$"))
public class MixinMinecraftServer {

    @Unique
    private Map<ResourceLocation, VersionPredicate> extlib$versionPredicates = Collections.emptyMap();

    public Map<ResourceLocation, VersionPredicate> extlib$getExtensionPredicates() {
        return extlib$versionPredicates;
    }

    public void extlib$setExtensionPredicates(Map<ResourceLocation, VersionPredicate> predicates) {
        extlib$versionPredicates = predicates;
    }



}
