package org.wallentines.extlib.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.wallentines.extlib.api.ExtensionRegistry;
import org.wallentines.extlib.client.impl.ExtensionListHolder;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerData.class)
@Implements(@Interface(iface= ExtensionListHolder.class, prefix = "extlib$"))
public class MixinServerData {

    @Unique
    private List<ResourceLocation> extlib$enabledExtensions = new ArrayList<>();

    public List<ResourceLocation> extlib$getEnabledExtensions() {
        return extlib$enabledExtensions;
    }

    public void extlib$setEnabledExtensions(List<ResourceLocation> enabledExtensions) {
        this.extlib$enabledExtensions = enabledExtensions;
    }

    @Inject(method="<init>", at=@At("TAIL"))
    private void onInit(String string, String string2, ServerData.Type type, CallbackInfo ci) {
        this.extlib$enabledExtensions = List.copyOf(ExtensionRegistry.getAllExtensions().keySet());
    }

    @Inject(method="write", at=@At(value="RETURN"))
    private void writeServerData(CallbackInfoReturnable<CompoundTag> cir, @Local CompoundTag tag) {
        ListTag listTag = new ListTag();
        for(ResourceLocation ext : extlib$enabledExtensions) {
            listTag.add(StringTag.valueOf(ext.toString()));
        }
        tag.put("enabled_extensions", listTag);
    }

    @Inject(method="read", at=@At(value="RETURN"))
    private static void onRead(CompoundTag compoundTag, CallbackInfoReturnable<ServerData> cir, @Local ServerData out) {

        List<ResourceLocation> enabledExtensions = new ArrayList<>();
        for(Tag t : compoundTag.getListOrEmpty("enabled_extensions")) {
            t.asString().map(ResourceLocation::tryParse).ifPresent(enabledExtensions::add);
        }

        ((ExtensionListHolder) out).setEnabledExtensions(enabledExtensions);
    }

    @Inject(method="copyFrom", at=@At("RETURN"))
    private void onCopy(ServerData serverData, CallbackInfo ci) {
        extlib$enabledExtensions = List.copyOf(((ExtensionListHolder) serverData).getEnabledExtensions());
    }


}
