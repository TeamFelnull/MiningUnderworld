package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.block.CollapsingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public Level level;

    @Shadow
    private BlockPos blockPosition;

    @Inject(method = "tick", at = @At("HEAD"))
    public void startCollapsing(CallbackInfo ci) {
        var entity = (Entity) (Object) this;
        if (!level.isClientSide()//ブロックを落下ブロックにする処理はサーバー専用
                && !(entity instanceof Player)//サーバー側からじゃプレイヤーの動きが分からないので無視、LocalPlayerMixinで処理
                && CollapsingBlock.canStartCollapse(entity))//崩壊させ得る状況
            CollapsingBlock.collapsing(entity, blockPosition.below());
    }
}
