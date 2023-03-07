package dev.felnull.miningunderworld.explatform.fabric;

import dev.felnull.miningunderworld.fluid.MUFluidTags;
import dev.felnull.miningunderworld.mixin.EntityAccessor;
import net.minecraft.world.entity.Entity;

public class MUExpectPlatformImpl {
    public static boolean isInTar(Entity entity) {
        boolean firstTick = ((EntityAccessor) entity).getFirstTick();
        return !firstTick && entity.getFluidHeight(MUFluidTags.TAR) > 0;
    }

    public static boolean isEyeInTar(Entity entity) {
        return entity.isEyeInFluid(MUFluidTags.TAR);
    }
}
