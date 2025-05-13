package org.wallentines.extlib.client.impl;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.ApiStatus;
import org.wallentines.extlib.api.ExtensionRegistry;
import org.wallentines.extlib.impl.ClientboundEnabledExtensionsPacket;
import org.wallentines.extlib.impl.ExtensionMap;
import org.wallentines.extlib.impl.ExtensionMapHolder;
import org.wallentines.extlib.impl.ServerboundExtensionPacket;
import org.wallentines.extlib.mixin.AccessorClientHandshakePacketListener;

import java.util.concurrent.CompletableFuture;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public class ClientInit implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        ClientLoginNetworking.registerGlobalReceiver(ServerboundExtensionPacket.ID, (client, handler, buf, callbacksConsumer) -> {

            ExtensionMap map = new ExtensionMap(ExtensionRegistry.getAllExtensions());
            ServerData sd = ((AccessorClientHandshakePacketListener) handler).getServerData();
            if(sd != null) {
                map = map.intersection(((ExtensionListHolder) sd).getEnabledExtensions());
            }

            FriendlyByteBuf out = PacketByteBufs.create();
            new ServerboundExtensionPacket(map).write(out);

            return CompletableFuture.completedFuture(out);
        });

        ClientConfigurationNetworking.registerGlobalReceiver(ClientboundEnabledExtensionsPacket.TYPE, (payload, context) -> {

            ExtensionMapHolder holder = ((ExtensionMapHolder) context.client());
            holder.setExtensionMap(new ExtensionMap(ExtensionRegistry.getAllExtensions()).intersection(payload.ids()));
        });

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            ((ExtensionMapHolder) client).setExtensionMap(new ExtensionMap(ExtensionRegistry.getAllExtensions()));
        });

    }
}
