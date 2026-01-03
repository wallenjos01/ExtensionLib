package org.wallentines.extlib.mixin;

import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import org.semver4j.RangesList;
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
    private Map<Identifier, RangesList> extlib$versionPredicates = Collections.emptyMap();

    public Map<Identifier, RangesList> extlib$getExtensionPredicates() {
        return extlib$versionPredicates;
    }

    public void extlib$setExtensionPredicates(Map<Identifier, RangesList> predicates) {
        extlib$versionPredicates = predicates;
    }



}
