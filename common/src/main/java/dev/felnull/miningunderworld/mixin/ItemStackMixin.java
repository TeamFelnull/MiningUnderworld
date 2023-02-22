package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.item.WeatheringItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract void setDamageValue(int i);

    @Shadow
    public abstract int getDamageValue();

    @Shadow
    public abstract int getMaxDamage();

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V", ordinal = 0, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void hurt(int i, RandomSource randomSource, ServerPlayer serverPlayer, CallbackInfoReturnable<Boolean> cir, int j) {
        var ths = (ItemStack) (Object) this;

        //耐久地が減ったときに確率で錆びる+錆びていると耐久地が減りやすくなる
        if (getItem() instanceof WeatheringItem weatheringItem) {
            weatheringItem.weatheringHurt(ths, randomSource);

            WeatheringItem.WeatheringState state;
            if ((state = WeatheringItem.getWeatheringState(ths)) != WeatheringItem.WeatheringState.NONE) {
                int dmg = j - this.getDamageValue();
                int rdmg = this.getDamageValue() + (dmg * state.ordinal());
                this.setDamageValue(rdmg);
                cir.setReturnValue(j >= this.getMaxDamage());
            }
        }
    }
}
