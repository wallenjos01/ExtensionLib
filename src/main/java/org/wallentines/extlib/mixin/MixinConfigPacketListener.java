package org.wallentines.extlib.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.wallentines.extlib.impl.ExtensionMap;
import org.wallentines.extlib.impl.ExtensionMapHolder;

@Mixin(ServerConfigurationPacketListenerImpl.class)
@Implements(@Interface(iface= ExtensionMapHolder.class, prefix = "extlib$"))
public class MixinConfigPacketListener {

    @Unique
    private ExtensionMap extlib$extensions;

    public ExtensionMap extlib$getExtensionMap() {
        return extlib$extensions;
    }

    public void extlib$setExtensionMap(ExtensionMap extlib$extensions) {
        this.extlib$extensions = extlib$extensions;
    }

    @Inject(method="handleConfigurationFinished", at=@At(value="INVOKE", target="Lnet/minecraft/server/players/PlayerList;placeNewPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/network/CommonListenerCookie;)V"))
    private void onConfigurationFinished(CallbackInfo ci, @Local ServerPlayer player) {
        ((ExtensionMapHolder) player).setExtensionMap(extlib$extensions);
    }


}
