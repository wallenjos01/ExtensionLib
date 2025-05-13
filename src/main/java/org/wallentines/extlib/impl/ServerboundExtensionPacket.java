package org.wallentines.extlib.impl;

import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ApiStatus.Internal
public record ServerboundExtensionPacket(ExtensionMap extensions) {

    public static final ResourceLocation ID = Objects.requireNonNull(ResourceLocation.tryBuild("extensionlib", "extensions"));

    public static ServerboundExtensionPacket read(FriendlyByteBuf buf) {

        int count = buf.readVarInt();
        Map<ResourceLocation, Version> extensions = new HashMap<>(count);

        for(int i = 0; i < count; i++) {
            try {
                extensions.put(buf.readResourceLocation(), Version.parse(buf.readUtf()));
            } catch (VersionParsingException ex) {
                throw new RuntimeException(ex);
            }
        }

        return new ServerboundExtensionPacket(new ExtensionMap(extensions));
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(extensions.extensions().size());
        for(Map.Entry<ResourceLocation, Version> extension : extensions.extensions().entrySet()) {
            buf.writeResourceLocation(extension.getKey());
            buf.writeUtf(extension.getValue().toString());
        }
    }

}
