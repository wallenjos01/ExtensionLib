package org.wallentines.extlib.test;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.wallentines.extlib.api.ExtensionRegistry;

public class Init implements ModInitializer {
    @Override
    public void onInitialize() {
        ExtensionRegistry.registerExtension(ResourceLocation.tryBuild("extlibtest", "test"), ExtensionRegistry.makeVersion(0,1,0));
    }
}
