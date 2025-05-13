package org.wallentines.extlib.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.execution.ExecutionContext;
import net.minecraft.commands.execution.Frame;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.wallentines.extlib.api.PlayerExtensions;

import java.util.Set;


public class Functions {

    public static void test(CommandSourceStack css,
                            CompoundTag tag,
                            ResourceLocation id,
                            CommandDispatcher<CommandSourceStack> dispatcher,
                            ExecutionContext<CommandSourceStack> ctx,
                            Frame frame,
                            Void data) throws CommandSyntaxException {


        ServerPlayer player = css.getPlayerOrException();
        Set<ResourceLocation> extensions = PlayerExtensions.getExtensions(player).keySet();

        MutableComponent cmp = player.getDisplayName().copy().append(" has " + extensions.size() + " extensions enabled:");

        for(ResourceLocation loc : extensions) {
            cmp.append("\n - ")
                    .append(Component.translatable(loc.getNamespace() + ".extension." + loc.getPath() + ".name"))
                    .append(" version ")
                    .append(PlayerExtensions.getExtensionVersion(css.getPlayerOrException(), loc).toString());
        }

        css.getPlayerOrException().sendSystemMessage(cmp);

    }

}
