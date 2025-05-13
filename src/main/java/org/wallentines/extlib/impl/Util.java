package org.wallentines.extlib.impl;

import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;
import net.fabricmc.loader.impl.util.version.VersionPredicateParser;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.wallentines.mdcfg.serializer.InlineSerializer;
import org.wallentines.mdcfg.serializer.SerializeResult;
import org.wallentines.mdcfg.serializer.Serializer;

@ApiStatus.Internal
public class Util {

    public static final InlineSerializer<ResourceLocation> ID_SERIALIZER = InlineSerializer.of(ResourceLocation::toString, ResourceLocation::tryParse);

    public static final Serializer<VersionPredicate> VERSION_PREDICATE_SERIALIZER = Serializer.STRING.map(vp -> SerializeResult.success(vp.toString()), str -> {
        try {
            return SerializeResult.success(VersionPredicateParser.parse(str));
        } catch (VersionParsingException ex) {
            return SerializeResult.failure("Unable to parse version predicate: " + str, ex);
        }
    });

}
