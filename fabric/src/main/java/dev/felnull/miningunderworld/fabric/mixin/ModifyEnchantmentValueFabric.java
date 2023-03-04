package dev.felnull.miningunderworld.fabric.mixin;

import dev.felnull.miningunderworld.Temp;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnchantmentHelper.class)
public class ModifyEnchantmentValueFabric {

    @Redirect(method = "selectEnchantment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;getEnchantmentValue()I"))
    private static int modifyEnchantmentValue(Item item) {
        return Temp.modifyEnchantmentValue(item.getEnchantmentValue());
    }
}
