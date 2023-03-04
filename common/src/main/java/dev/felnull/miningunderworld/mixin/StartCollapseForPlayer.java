package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.block.CollapsingBlock;
import dev.felnull.miningunderworld.network.MUPackets;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class StartCollapseForPlayer extends LivingEntity {


    protected StartCollapseForPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void startCollapsing(CallbackInfo ci) {
        if (CollapsingBlock.canStartCollapse(this))//崩落させ得る状況ならば
            MUPackets.PLAYER_MOVING_ON_COLLAPSING.accept(getDeltaMovement());//プレイヤーの動きをサーバーに送る→PLAYER_MOVEMENT
    }
}
