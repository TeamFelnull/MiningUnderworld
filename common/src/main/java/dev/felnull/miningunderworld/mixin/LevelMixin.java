package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.world.DynamicSignal;
import dev.felnull.miningunderworld.world.DynamicSignalLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class LevelMixin implements DynamicSignalLevel {
    @Unique
    private final DynamicSignal dynamicSignal = new DynamicSignal((Level) (Object) this);

    @Inject(method = "getSignal", at = @At("HEAD"), cancellable = true)
    private void getSignal(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Integer> cir) {
        if (dynamicSignal.isSignal(blockPos))
            cir.setReturnValue(15);
    }

    @Override
    public DynamicSignal getDynamicSignal() {
        return dynamicSignal;
    }
}
