package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.block.MiningTntBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntBlock.class)
public class TntBlockMixin {
    @Inject(method = "explode(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At("HEAD"), cancellable = true)
    private static void explode(Level level, BlockPos blockPos, LivingEntity livingEntity, CallbackInfo ci) {
        if (level.getBlockState(blockPos).getBlock() instanceof MiningTntBlock) {
            MiningTntBlock.miningExplode(level, blockPos, livingEntity);
            ci.cancel();
        }
    }
}
