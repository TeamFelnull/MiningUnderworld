package dev.felnull.miningunderworld.forge.mixin;

import dev.felnull.miningunderworld.fluid.MUFluids;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FluidType.class, remap = false)
public abstract class FluidTypeMixin {

    @Inject(method = "canSwim", at = @At("HEAD"), cancellable = true)
    private void canSwim(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (MUFluids.TAR.get().getFluidType() == (Object) this || MUFluids.FLOWING_TAR.get().getFluidType() == (Object) this)
            cir.setReturnValue(false);
    }
}
