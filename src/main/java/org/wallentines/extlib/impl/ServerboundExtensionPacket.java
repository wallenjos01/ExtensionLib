package org.wallentines.extlib.impl;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.semver4j.Semver;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ApiStatus.Internal
public record ServerboundExtensionPacket(ExtensionMap extensions) {

    public static final ResourceLocation ID = Objects.requireNonNull(ResourceLocation.tryBuild("extensionlib", "extensions"));

    public static ServerboundExtensionPacket read(FriendlyByteBuf buf) {

        int count = buf.readVarInt();
        Map<ResourceLocation, Semver> extensions = new HashMap<>(count);

        for(int i = 0; i < count; i++) {
            ResourceLocation id = buf.readResourceLocation();
            Semver ver = Semver.parse(buf.readUtf());
            if(ver == null) {
                throw new IllegalArgumentException("Invalid Version: " + buf.readUtf());
            }
            extensions.put(id, ver);

        }

        return new ServerboundExtensionPacket(new ExtensionMap(extensions));
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(extensions.extensions().size());
        for(Map.Entry<ResourceLocation, Semver> extension : extensions.extensions().entrySet()) {
            buf.writeResourceLocation(extension.getKey());
            buf.writeUtf(extension.getValue().toString());
        }
    }

}
