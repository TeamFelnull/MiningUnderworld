package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.MixinTemp;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldGenRegion.class)
public class SuppressErrorInTestEnsuring {

    @Inject(method = "ensureCanWrite", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;logAndPauseIfInIde(Ljava/lang/String;)V"), cancellable = true)
    public void suppress(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (MixinTemp.testEnsuring.get())
            cir.setReturnValue(false);
    }
}
