package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.block.CollapseStarter;
import dev.felnull.miningunderworld.entity.PrevFallDistanceEntity;
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

    //崩落開始するべきならプレイヤーの動きをサーバーに送って崩落開始。送ってる間にprevFallDistanceが更新されてしまうのでそれも一緒に送る。
    @Inject(method = "tick", at = @At("HEAD"))
    public void startCollapse(CallbackInfo ci) {
        if (CollapseStarter.shouldStartCollapse(this))
            MUPackets.PLAYER_START_COLLAPSE.accept(getDeltaMovement().horizontalDistance(), ((PrevFallDistanceEntity) this).getPrevFallDistance());

        System.out.println("Client sent:" + ((PrevFallDistanceEntity) this).getPrevFallDistance());
    }
}
