package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.miningunderworld.block.TarStainsBlock;
import dev.felnull.miningunderworld.fluid.MUFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlowingFluid.class)
public abstract class FlowingFluidMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(Level level, BlockPos blockPos, FluidState fluidState, CallbackInfo ci) {
        if (!((Object) this == MUFluids.TAR.get()) && !((Object) this == MUFluids.FLOWING_TAR.get()))
            return;

        if (level.getBlockState(blockPos).isAir()) {
            BlockState state = ((TarStainsBlock) MUBlocks.TAR_STAINS.get()).getAllAttachedFace(level, blockPos, Direction.stream());
            if (!state.isAir())
                level.setBlockAndUpdate(blockPos, state);
        }
    }
}
