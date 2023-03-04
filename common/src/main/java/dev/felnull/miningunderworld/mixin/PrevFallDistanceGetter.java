package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.entity.PrevFallDistanceEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class PrevFallDistanceGetter implements PrevFallDistanceEntity {

    @Shadow public float fallDistance;

    public float prevFallDistance;
    private boolean postponeStoring;

    @Inject(method = "tick", at = @At("TAIL"))
    public void storeFallDistance(CallbackInfo ci){
        if(!postponeStoring)
            this.prevFallDistance = fallDistance;
        else
            postponeStoring = false;
    }

    @Override
    public float getPrevFallDistance() {
        return prevFallDistance;
    }

    @Override
    public void postponeStoring() {
        this.postponeStoring = true;
    }
}
