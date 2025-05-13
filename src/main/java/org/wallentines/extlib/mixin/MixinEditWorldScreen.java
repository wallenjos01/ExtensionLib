package org.wallentines.extlib.mixin;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.EditWorldScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.wallentines.extlib.client.impl.ExtensionListHolder;
import org.wallentines.extlib.client.impl.ExtensionsScreen;
import org.wallentines.mdcfg.ConfigObject;
import org.wallentines.mdcfg.codec.FileWrapper;
import org.wallentines.mdcfg.codec.JSONCodec;
import org.wallentines.mdcfg.serializer.ConfigContext;

import java.nio.file.Path;

@Mixin(EditWorldScreen.class)
public class MixinEditWorldScreen extends Screen {

    @Shadow @Final private LinearLayout layout;

    @Shadow @Final private LevelStorageSource.LevelStorageAccess levelAccess;

    @Unique
    private FileWrapper<ConfigObject> extensions;

    protected MixinEditWorldScreen(Component component) {
        super(component);
    }

    @Inject(method="<init>", at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/layouts/SpacerElement;<init>(II)V", ordinal = 1))
    private void addExtensionButton(CallbackInfo ci) {


        Path filePath = levelAccess.getLevelDirectory().path().resolve("extensions.json");
        extensions = new FileWrapper<>(ConfigContext.INSTANCE, JSONCodec.fileCodec(), filePath);

        layout.addChild(Button.builder(ExtensionsScreen.BUTTON, (buttonx) -> {
            ExtensionListHolder holder = new ExtensionListHolder.FileExtensionListHolder(extensions);
            ExtensionsScreen extensionsScreen = new ExtensionsScreen(this, holder);

            minecraft.setScreen(extensionsScreen);

        }).width(200).build());
    }

    @Inject(method="onRename", at=@At(value="INVOKE", target="Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;renameLevel(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void saveExtensions(CallbackInfo ci) {
        extensions.save();
    }

}
