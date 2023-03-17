package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.world.dimension.MUBiomeSource;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.RepeatingPlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

@Mixin(RepeatingPlacement.class)
public class DenseOres {

    private boolean shouldDense;

    @Inject(method = "getPositions", at = @At("HEAD"))
    public void shouldDense(PlacementContext placementContext, RandomSource randomSource, BlockPos blockPos, CallbackInfoReturnable<Stream<BlockPos>> cir) {
        shouldDense = placementContext.generator().getBiomeSource() instanceof MUBiomeSource
                && placementContext.topFeature().get().feature().value().feature() == Feature.ORE;
    }

    @ModifyArg(method = "getPositions", at = @At(value = "INVOKE", target = "Ljava/util/stream/IntStream;range(II)Ljava/util/stream/IntStream;"), index = 1)
    public int dense(int count) {
        return shouldDense ? count * 16 : count;
    }
}
