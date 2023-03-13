package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.miningunderworld.block.SemisolidTarBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public class FallingBlockEntityMixin {

    @Shadow
    private BlockState blockState;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 0), cancellable = true)
    public void tick(CallbackInfo ci) {
        FallingBlockEntity thiz = (FallingBlockEntity) (Object) this;
        BlockPos blockPos = thiz.blockPosition();
        BlockState onBlockState = thiz.level.getBlockState(blockPos);

        if (this.blockState.is(MUBlocks.SEMISOLID_TAR.get()) && onBlockState.is(MUBlocks.SEMISOLID_TAR.get())) {
            if (SemisolidTarBlock.onFallBlockEntity(thiz, thiz.level, blockPos, this.blockState, onBlockState)) {
                ci.cancel();
                ((ServerLevel) thiz.level).getChunkSource().chunkMap.broadcast(thiz, new ClientboundBlockUpdatePacket(blockPos, thiz.level.getBlockState(blockPos)));
                thiz.discard();
            }
        }
    }
}
