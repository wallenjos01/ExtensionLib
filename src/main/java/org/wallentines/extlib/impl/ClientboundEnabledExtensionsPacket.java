package org.wallentines.extlib.impl;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashSet;
import java.util.Set;

@ApiStatus.Internal
public record ClientboundEnabledExtensionsPacket(Set<ResourceLocation> ids) implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.tryBuild("extensionlib", "enabled_extensions");
    public static final CustomPacketPayload.Type<ClientboundEnabledExtensionsPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, ClientboundEnabledExtensionsPacket> CODEC = new StreamCodec<>() {
        @Override
        public ClientboundEnabledExtensionsPacket decode(FriendlyByteBuf buf) {

            int count = buf.readVarInt();
            Set<ResourceLocation> ids = new HashSet<>();
            for (int i = 0; i < count; i++) {
                ids.add(buf.readResourceLocation());
            }

            return new ClientboundEnabledExtensionsPacket(ids);
        }

        @Override
        public void encode(FriendlyByteBuf buf, ClientboundEnabledExtensionsPacket packet) {

            buf.writeVarInt(packet.ids.size());
            for (ResourceLocation id : packet.ids) {
                buf.writeResourceLocation(id);
            }
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
