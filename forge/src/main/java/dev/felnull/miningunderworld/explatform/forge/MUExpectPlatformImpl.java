package dev.felnull.miningunderworld.explatform.forge;

import dev.felnull.miningunderworld.fluid.MUFluids;
import dev.felnull.miningunderworld.mixin.EntityAccessor;
import net.minecraft.world.entity.Entity;

public class MUExpectPlatformImpl {
    public static boolean isInTar(Entity entity) {
        boolean firstTick = ((EntityAccessor) entity).getFirstTick();
        return !firstTick && (entity.isInFluidType(MUFluids.TAR.get().getFluidType()) || entity.isInFluidType(MUFluids.FLOWING_TAR.get().getFluidType()));
    }

    public static boolean isEyeInTar(Entity entity) {
        return entity.isEyeInFluidType(MUFluids.TAR.get().getFluidType()) || entity.isEyeInFluidType(MUFluids.FLOWING_TAR.get().getFluidType());
    }
}
