package org.wallentines.extlib.test;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.wallentines.extlib.api.ExtensionRegistry;

public class Init implements ModInitializer {
    @Override
    public void onInitialize() {
        ExtensionRegistry.registerExtension(Identifier.tryBuild("extlibtest", "test"), ExtensionRegistry.makeVersion(0,1,0));
    }
}
