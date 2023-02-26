package dev.felnull.miningunderworld.fluid;

import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class MUFluidTags {
    public static final TagKey<Fluid> TAR = bind("tar");

    private static TagKey<Fluid> bind(String id) {
        return TagKey.create(Registries.FLUID, MUUtils.modLoc(id));
    }
}
