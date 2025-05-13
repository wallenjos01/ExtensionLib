package org.wallentines.extlib.impl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.semver4j.RangesList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wallentines.extlib.api.ExtensionRegistry;
import org.wallentines.mdcfg.ConfigObject;
import org.wallentines.mdcfg.ConfigSection;
import org.wallentines.mdcfg.codec.FileWrapper;
import org.wallentines.mdcfg.mc.api.ServerConfigFolders;
import org.wallentines.mdcfg.serializer.ConfigContext;

import java.nio.file.Path;
import java.util.Map;

@ApiStatus.Internal
public class Init implements ModInitializer {

    private static final ConfigSection DEFAULT_CONFIG = new ConfigSection()
            .with("required_extensions", new ConfigSection());


    private static final Logger log = LoggerFactory.getLogger(Init.class);

    @Override
    public void onInitialize() {

        ServerLoginNetworking.registerGlobalReceiver(ServerboundExtensionPacket.ID, (minecraftServer, serverLoginPacketListener, understood, data, loginSynchronizer, packetSender) -> {

            if(!understood) {
                log.info("{}'s client does not support extensions", serverLoginPacketListener.getUserName());
                return;
            }

            ServerboundExtensionPacket pck = ServerboundExtensionPacket.read(data);

            ExtensionMap out = pck.extensions().intersection(ExtensionRegistry.getAllExtensions().keySet());

            ((ExtensionMapHolder) serverLoginPacketListener).setExtensionMap(out);
            log.info("{} logged in with {} enabled extension(s)", serverLoginPacketListener.getUserName(), out.extensions().size());
        });

        ServerLoginConnectionEvents.QUERY_START.register((listener, minecraftServer, loginPacketSender, loginSynchronizer) -> {
            FriendlyByteBuf buf = PacketByteBufs.create();
            loginPacketSender.sendPacket(ServerboundExtensionPacket.ID, buf);
        });

        PayloadTypeRegistry.configurationS2C().register(ClientboundEnabledExtensionsPacket.TYPE, ClientboundEnabledExtensionsPacket.CODEC);

        ServerConfigurationConnectionEvents.CONFIGURE.register((handler, server) -> {
            ExtensionMap extensions = ((ExtensionMapHolder) handler).getExtensionMap();
            if(extensions == null) extensions = ExtensionMap.EMPTY;

            Map<ResourceLocation, RangesList> predicates = ((ExtensionPredicateHolder) server).getExtensionPredicates();
            Map<ResourceLocation, RangesList> missing = extensions.test(predicates);
            if(!missing.isEmpty()) {

                MutableComponent with = Component.literal("\n").withStyle(ChatFormatting.GRAY);
                for(Map.Entry<ResourceLocation, RangesList> entry : missing.entrySet()) {

                    String extensionName = entry.getKey().getNamespace() + ".extension." + entry.getKey().getPath() + ".name";

                    with.append("\n")
                            .append(Component.translatableWithFallback(extensionName, entry.getKey().toString()).withStyle(ChatFormatting.YELLOW))
                            .append(" ")
                            .append(Component.translatable(entry.getValue().toString()).withStyle(ChatFormatting.WHITE));
                }

                handler.disconnect(Component.translatableWithFallback("disconnect.missing_extensions", "This server requires the ExtensionLib and the following extensions: %s", with));
                return;
            }

            handler.send(new ClientboundCustomPayloadPacket(new ClientboundEnabledExtensionsPacket(extensions.extensions().keySet())));
        });

        ServerLifecycleEvents.SERVER_STARTING.register((server) -> {

            Path folder = ServerConfigFolders.createConfigFolder(server, "ExtensionLib").orElseThrow();
            FileWrapper<ConfigObject> wrapper = ServerConfigFolders.FILE_CODEC_REGISTRY.findOrCreate(ConfigContext.INSTANCE, "config", folder, DEFAULT_CONFIG);

            wrapper.load();
            ((ExtensionPredicateHolder) server).setExtensionPredicates(wrapper.getRoot().asSection().get("required_extensions", Util.VERSION_PREDICATE_SERIALIZER.mapOf(Util.ID_SERIALIZER)));
        });

    }
}
