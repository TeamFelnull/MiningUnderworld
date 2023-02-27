package dev.felnull.miningunderworld.fabric.mixin;

import dev.felnull.miningunderworld.fabric.FluidDeceiveLivingEntity;
import dev.felnull.miningunderworld.fluid.MUFluidTags;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract boolean updateFluidHeightAndDoFluidPushing(TagKey<Fluid> tagKey, double d);

    @Shadow
    protected Object2DoubleMap<TagKey<Fluid>> fluidHeight;

    @Inject(method = "updateInWaterStateAndDoFluidPushing", at = @At("RETURN"), cancellable = true)
    private void updateInWaterStateAndDoFluidPushing(CallbackInfoReturnable<Boolean> cir) {
        boolean bl = this.updateFluidHeightAndDoFluidPushing(MUFluidTags.TAR, 0.0023333333333333335f);
        boolean r = cir.getReturnValue();
        cir.setReturnValue(r || bl);
    }

    @Inject(method = "isInWater", at = @At("HEAD"), cancellable = true)
    private void isInWater(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof FluidDeceiveLivingEntity deceiveFluidLivingEntity && (deceiveFluidLivingEntity.isFluidDeceiveTravel()||deceiveFluidLivingEntity.isFluidDeceiveTravel()))
            cir.setReturnValue(true);
    }

    @Inject(method = "getFluidHeight", at = @At("HEAD"), cancellable = true)
    private void getFluidHeight(TagKey<Fluid> tagKey, CallbackInfoReturnable<Double> cir) {
        if ((Object) this instanceof FluidDeceiveLivingEntity deceiveFluidLivingEntity && deceiveFluidLivingEntity.isFluidDeceiveAiStepJumpingFluidHeight())
            cir.setReturnValue(this.fluidHeight.getDouble(MUFluidTags.TAR));
    }

    @Inject(method = "isInLava", at = @At("HEAD"), cancellable = true)
    private void isInLava(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof FluidDeceiveLivingEntity deceiveFluidLivingEntity && deceiveFluidLivingEntity.isFluidDeceiveAiStepJumpingInLava())
            cir.setReturnValue(true);
    }
}
