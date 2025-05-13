package org.wallentines.extlib.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.protocol.login.ServerboundLoginAcknowledgedPacket;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.wallentines.extlib.impl.ExtensionMap;
import org.wallentines.extlib.impl.ExtensionMapHolder;

@Mixin(ServerLoginPacketListenerImpl.class)
@Implements(@Interface(iface= ExtensionMapHolder.class, prefix = "extlib$"))
public class MixinLoginPacketListener {

    @Unique
    private ExtensionMap extlib$extensions;

    public ExtensionMap extlib$getExtensionMap() {
        return extlib$extensions;
    }

    public void extlib$setExtensionMap(ExtensionMap extlib$extensions) {
        this.extlib$extensions = extlib$extensions;
    }

    @Inject(method="handleLoginAcknowledgement", at=@At(value="INVOKE", target="Lnet/minecraft/server/network/ServerConfigurationPacketListenerImpl;startConfiguration()V"))
    private void onConfiguration(ServerboundLoginAcknowledgedPacket serverboundLoginAcknowledgedPacket, CallbackInfo ci, @Local ServerConfigurationPacketListenerImpl configListener) {

        ((ExtensionMapHolder) configListener).setExtensionMap(extlib$extensions);

    }


}
