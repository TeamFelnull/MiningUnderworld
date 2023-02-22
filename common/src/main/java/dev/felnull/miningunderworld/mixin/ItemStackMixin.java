package dev.felnull.miningunderworld.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract void setDamageValue(int i);

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V", ordinal = 0, shift = At.Shift.AFTER), cancellable = true)
    private void hurt(int i, RandomSource randomSource, ServerPlayer serverPlayer, CallbackInfoReturnable<Boolean> cir) {
       /* var ths = (ItemStack) (Object) this;

        WeatheringItem.WeatheringState state;
        if (getItem() instanceof WeatheringItem && (state = WeatheringItem.getWeatheringState(ths)) != WeatheringItem.WeatheringState.NONE) {
            this.setDamageValue(j);
        }*/
        //TODO 錆びてる場合の耐久地関係
    }
}
