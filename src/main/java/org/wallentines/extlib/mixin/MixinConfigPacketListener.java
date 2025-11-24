package org.wallentines.extlib.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.minecraft.server.network.config.PrepareSpawnTask;

import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
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

    @WrapOperation(method="handleConfigurationFinished", at=@At(value="INVOKE", target="Lnet/minecraft/server/network/config/PrepareSpawnTask;spawnPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/network/CommonListenerCookie;)Lnet/minecraft/server/level/ServerPlayer;"))
    private ServerPlayer onConfigurationFinished(PrepareSpawnTask task, Connection connection, CommonListenerCookie cookie, Operation<ServerPlayer> original) {
        ServerPlayer spl = original.call(task, connection, cookie);
        ((ExtensionMapHolder) spl).setExtensionMap(extlib$extensions);
        return spl;
    }


}
