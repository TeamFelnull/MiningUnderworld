package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FloatGoal.class)
public class FloatGoalMixin {
    @Shadow
    @Final
    private Mob mob;

    @Inject(method = "canUse", at = @At("RETURN"), cancellable = true)
    private void canUse(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || MUUtils.isInTar(mob));
    }
}
