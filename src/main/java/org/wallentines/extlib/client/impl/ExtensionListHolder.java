package org.wallentines.extlib.client.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.wallentines.extlib.api.ExtensionRegistry;
import org.wallentines.extlib.impl.Util;
import org.wallentines.mdcfg.ConfigList;
import org.wallentines.mdcfg.ConfigObject;
import org.wallentines.mdcfg.codec.FileWrapper;
import org.wallentines.mdcfg.serializer.ConfigContext;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public interface ExtensionListHolder {

    List<ResourceLocation> getEnabledExtensions();

    void setEnabledExtensions(List<ResourceLocation> extensions);


    record FileExtensionListHolder(FileWrapper<ConfigObject> file) implements ExtensionListHolder {

        @Override
        public List<ResourceLocation> getEnabledExtensions() {
            file.load();
            if(file.getRoot() == null || !file.getRoot().isList()) {
                file.setRoot(new ConfigList());
                return new ArrayList<>(ExtensionRegistry.getAllExtensions().keySet());
            }

            ConfigList list = file.getRoot().asList();
            return Util.ID_SERIALIZER.listOf().mapToList().deserialize(ConfigContext.INSTANCE, list).getOrThrow();
        }

        @Override
        public void setEnabledExtensions(List<ResourceLocation> extensions) {
            file.setRoot(Util.ID_SERIALIZER.listOf().mapToList().serialize(ConfigContext.INSTANCE, extensions).getOrThrow());
        }
    }

}
