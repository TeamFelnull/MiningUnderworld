package dev.felnull.miningunderworld.fabric.mixin;

import dev.felnull.miningunderworld.fabric.FluidDeceiveLivingEntity;
import dev.felnull.miningunderworld.fluid.MUFluidTags;
import dev.felnull.miningunderworld.util.MUUtils;
import dev.felnull.miningunderworld.util.MitigatedThreadLocal;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements FluidDeceiveLivingEntity {
    private final MitigatedThreadLocal<Boolean> miningunderworld_fluidDeceiveTravel = MitigatedThreadLocal.newMitigatedThreadLocal(() -> false);
    private final MitigatedThreadLocal<Boolean> miningunderworld_fluidDeceiveAiStepJumpingFluidHeight = MitigatedThreadLocal.newMitigatedThreadLocal(() -> false);
    private final MitigatedThreadLocal<Boolean> miningunderworld_fluidDeceiveAiStepJumpingInLava = MitigatedThreadLocal.newMitigatedThreadLocal(() -> false);
  //  private final MitigatedThreadLocal<Boolean> miningunderworld_fluidDeceiveAiStepJumpingJumpInLiquid = MitigatedThreadLocal.newMitigatedThreadLocal(() -> false);

    @Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void travel(Vec3 vec3, CallbackInfo ci, double d, boolean bl, FluidState fluidState) {
        if (fluidState.is(MUFluidTags.TAR))
            miningunderworld_fluidDeceiveTravel.set(true);
    }

    @Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z", ordinal = 0, shift = At.Shift.AFTER))
    private void travelAfter(Vec3 vec3, CallbackInfo ci) {
        miningunderworld_fluidDeceiveTravel.set(false);
    }

    @Override
    public boolean isFluidDeceiveTravel() {
        return miningunderworld_fluidDeceiveTravel.get();
    }


    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getFluidHeight(Lnet/minecraft/tags/TagKey;)D", ordinal = 1))
    private void aiStep(CallbackInfo ci) {
        if (MUUtils.isInTar((Entity) (Object) this))
            miningunderworld_fluidDeceiveAiStepJumpingFluidHeight.set(true);
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getFluidHeight(Lnet/minecraft/tags/TagKey;)D", ordinal = 1, shift = At.Shift.AFTER))
    private void aiStepAfter(CallbackInfo ci) {
        miningunderworld_fluidDeceiveAiStepJumpingFluidHeight.set(false);
    }


    @Override
    public boolean isFluidDeceiveAiStepJumpingFluidHeight() {
        return miningunderworld_fluidDeceiveAiStepJumpingFluidHeight.get();
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInLava()Z", ordinal = 1))
    private void aiStep2(CallbackInfo ci) {
        if (MUUtils.isInTar((Entity) (Object) this))
            miningunderworld_fluidDeceiveAiStepJumpingInLava.set(true);
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInLava()Z", ordinal = 1, shift = At.Shift.AFTER))
    private void aiStep2After(CallbackInfo ci) {
        miningunderworld_fluidDeceiveAiStepJumpingInLava.set(false);
    }

    @Override
    public boolean isFluidDeceiveAiStepJumpingInLava() {
        return miningunderworld_fluidDeceiveAiStepJumpingInLava.get();
    }/*

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;jumpInLiquid(Lnet/minecraft/tags/TagKey;)V", ordinal = 1))
    private void aiStep3(CallbackInfo ci) {
        if (MUUtils.isInTar((Entity) (Object) this))
            miningunderworld_fluidDeceiveAiStepJumpingJumpInLiquid.set(true);
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;jumpInLiquid(Lnet/minecraft/tags/TagKey;)V", ordinal = 1, shift = At.Shift.AFTER))
    private void aiStep3After(CallbackInfo ci) {
        miningunderworld_fluidDeceiveAiStepJumpingJumpInLiquid.set(false);
    }*/
}
