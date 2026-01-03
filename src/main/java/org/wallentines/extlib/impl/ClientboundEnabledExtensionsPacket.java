package org.wallentines.extlib.impl;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashSet;
import java.util.Set;

@ApiStatus.Internal
public record ClientboundEnabledExtensionsPacket(Set<Identifier> ids) implements CustomPacketPayload {

    public static final Identifier ID = Identifier.tryBuild("extensionlib", "enabled_extensions");
    public static final CustomPacketPayload.Type<ClientboundEnabledExtensionsPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, ClientboundEnabledExtensionsPacket> CODEC = new StreamCodec<>() {
        @Override
        public ClientboundEnabledExtensionsPacket decode(FriendlyByteBuf buf) {

            int count = buf.readVarInt();
            Set<Identifier> ids = new HashSet<>();
            for (int i = 0; i < count; i++) {
                ids.add(buf.readIdentifier());
            }

            return new ClientboundEnabledExtensionsPacket(ids);
        }

        @Override
        public void encode(FriendlyByteBuf buf, ClientboundEnabledExtensionsPacket packet) {

            buf.writeVarInt(packet.ids.size());
            for (Identifier id : packet.ids) {
                buf.writeIdentifier(id);
            }
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
