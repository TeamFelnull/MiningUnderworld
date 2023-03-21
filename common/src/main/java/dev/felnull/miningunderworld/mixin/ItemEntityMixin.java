package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.entity.MUDamageSources;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void hurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        if (damageSource.is(MUDamageSources.MINING_EXPLOSION) || damageSource.is(MUDamageSources.MINING_EXPLOSION_PLAYER))
            cir.setReturnValue(false);
    }
}
