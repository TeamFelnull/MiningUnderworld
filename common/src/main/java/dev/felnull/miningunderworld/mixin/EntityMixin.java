package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.block.CollapsingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    protected boolean onGround;

    @Shadow
    public Level level;

    @Shadow
    private BlockPos blockPosition;

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V"))
    public void startCollapsing(CallbackInfo ci) {
        if (!level.isClientSide() && onGround && level.getBlockState(blockPosition.below()).getBlock() instanceof CollapsingBlock)
            CollapsingBlock.collapsing((Entity) (Object) this, blockPosition.below());
    }
}
