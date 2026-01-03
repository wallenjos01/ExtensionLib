package org.wallentines.extlib.mixin;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.ManageServerScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.wallentines.extlib.client.impl.ExtensionListHolder;
import org.wallentines.extlib.client.impl.ExtensionsScreen;

@Mixin(ManageServerScreen.class)
public class MixinEditServerScreen extends Screen {

    @Final
    @Shadow
    private ServerData serverData;

    protected MixinEditServerScreen(Component component) {
        super(component);
    }

    @Inject(method="init", at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/screens/ManageServerScreen;updateAddButtonStatus()V"))
    private void onInit(CallbackInfo ci) {

        this.addRenderableWidget(Button.builder(ExtensionsScreen.BUTTON, button -> minecraft.setScreen(new ExtensionsScreen(this, (ExtensionListHolder) serverData)))
                .size(200, 20)
                .pos(width / 2 - 100, height / 4 + 48)
                .build());
    }

}
