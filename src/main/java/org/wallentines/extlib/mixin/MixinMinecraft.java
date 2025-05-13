package org.wallentines.extlib.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.wallentines.extlib.client.impl.ExtensionListHolder;
import org.wallentines.extlib.impl.ExtensionMap;
import org.wallentines.extlib.impl.ExtensionMapHolder;
import org.wallentines.extlib.impl.Util;
import org.wallentines.mdcfg.ConfigObject;
import org.wallentines.mdcfg.codec.DecodeException;
import org.wallentines.mdcfg.codec.JSONCodec;
import org.wallentines.mdcfg.serializer.ConfigContext;
import org.wallentines.mdcfg.serializer.SerializeException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Mixin(Minecraft.class)
@Implements(@Interface(iface= ExtensionMapHolder.class, prefix = "extlib$"))
public class MixinMinecraft {

    @Shadow @Final private static Logger LOGGER;
    @Unique
    private ExtensionMap extlib$extensions = ExtensionMap.EMPTY;

    public ExtensionMap extlib$getExtensionMap() {
        return extlib$extensions;
    }

    public void extlib$setExtensionMap(ExtensionMap extlib$extensions) {
        this.extlib$extensions = extlib$extensions;
    }

    @Inject(method="disconnect(Lnet/minecraft/client/gui/screens/Screen;Z)V", at=@At("HEAD"))
    private void onDisconnect(Screen screen, boolean bl, CallbackInfo ci) {
        extlib$setExtensionMap(ExtensionMap.EMPTY);
    }

    @ModifyArg(method="doWorldLoad", at=@At(value="INVOKE", target="Lnet/minecraft/client/multiplayer/ClientHandshakePacketListenerImpl;<init>(Lnet/minecraft/network/Connection;Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/multiplayer/ServerData;Lnet/minecraft/client/gui/screens/Screen;ZLjava/time/Duration;Ljava/util/function/Consumer;Lnet/minecraft/client/multiplayer/TransferState;)V"))
    private ServerData modifyLocalServerData(ServerData original, @Local(argsOnly=true) LevelStorageSource.LevelStorageAccess access) {

        ServerData modified = original;
        if(original == null) {
            modified = new ServerData("", "", ServerData.Type.LAN);
        }

        Path levelRoot = access.getLevelDirectory().path();
        Path extFile = levelRoot.resolve("extensions.json");

        if(Files.isRegularFile(extFile)) {
            try {
                ConfigObject obj = JSONCodec.fileCodec().loadFromFile(ConfigContext.INSTANCE, extFile, StandardCharsets.UTF_8);
                List<ResourceLocation> ids = Util.ID_SERIALIZER.listOf().mapToList().deserialize(ConfigContext.INSTANCE, obj).getOrThrow();
                ((ExtensionListHolder) modified).setEnabledExtensions(ids);
            } catch(DecodeException | SerializeException | IOException ex) {
                LOGGER.warn("Unable to load extensions.json", ex);
            }
        }

        return modified;
    }


}
