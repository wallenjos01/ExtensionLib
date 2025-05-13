package org.wallentines.extlib.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.wallentines.extlib.impl.ExtensionMap;
import org.wallentines.extlib.impl.ExtensionMapHolder;

@Mixin(ServerPlayer.class)
@Implements(@Interface(iface= ExtensionMapHolder.class, prefix = "extlib$"))
public class MixinServerPlayer {

    @Unique
    private ExtensionMap extlib$extensions;

    public ExtensionMap extlib$getExtensionMap() {
        return extlib$extensions;
    }

    public void extlib$setExtensionMap(ExtensionMap extlib$extensions) {
        this.extlib$extensions = extlib$extensions;
    }

    @Inject(method="restoreFrom", at=@At("TAIL"))
    private void onRestore(ServerPlayer serverPlayer, boolean bl, CallbackInfo ci) {
        extlib$extensions = ((ExtensionMapHolder) serverPlayer).getExtensionMap();
    }

}
