package dev.felnull.miningunderworld.forge.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class)
public class LivingEntityMixin {
    /*@Unique
    private final ThreadLocal<Boolean> disableFluidMovement = ThreadLocal.withInitial(() -> false);

    @Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isAffectedByFluids()Z", ordinal = 0))
    private void travel(Vec3 vec3, CallbackInfo ci) {
        disableFluidMovement.set(true);
    }

    @Inject(method = "travel", at = @At("RETURN"))
    private void travel2(Vec3 vec3, CallbackInfo ci) {
        disableFluidMovement.set(false);
    }

    @Inject(method = "isAffectedByFluids", at = @At("RETURN"), cancellable = true)
    private void isAffectedByFluids(CallbackInfoReturnable<Boolean> cir) {
        if (disableFluidMovement.get()) {
            disableFluidMovement.set(false);

        }
        System.out.println("ｳｧｧ!!ｵﾚﾓｲｯﾁｬｳｩｩｩ!!!ｳｳｳｳｳｳｳｳｳｩｩｩｩｩｩｩｩｳｳｳｳｳｳｳｳ!ｲｨｨｲｨｨｨｲｲｲｨｲｲｲｲ");
        cir.setReturnValue(false);
    }*/
}
