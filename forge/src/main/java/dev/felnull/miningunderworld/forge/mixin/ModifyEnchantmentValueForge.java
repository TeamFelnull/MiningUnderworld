package dev.felnull.miningunderworld.forge.mixin;

import dev.felnull.miningunderworld.Temp;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EnchantmentHelper.class, remap = false)
public class ModifyEnchantmentValueForge {

    @Redirect(method = "selectEnchantment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getEnchantmentValue()I"))
    private static int modifyEnchantmentValue(ItemStack itemStack) {
        return Temp.modifyEnchantmentValue(itemStack.getEnchantmentValue());
    }
}
