package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.fluid.MUFluids;
import dev.felnull.miningunderworld.particles.MUParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Fluid.class)
public class FluidMixin {
    @Inject(method = "getDripParticle", at = @At("HEAD"), cancellable = true)
    private void getDripParticle(CallbackInfoReturnable<ParticleOptions> cir) {
        if (((Object) this) == MUFluids.TAR.get() || ((Object) this) == MUFluids.FLOWING_TAR.get())
            cir.setReturnValue(MUParticleTypes.DRIPPING_TAR.get());
    }
}
