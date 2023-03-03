package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.Temp;
import dev.felnull.miningunderworld.item.MUArmorMaterials;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EnchantmentHelper.class)
public class ModifyEnchantmentValue {

    @ModifyVariable(method = "selectEnchantment", at = @At("STORE"), ordinal = 0)
    private static int modifyEnchantmentValue(int enchantmentValue) {
        var isFullArmor = Temp.armorSlotsReferringNow.stream().allMatch(item ->
                item.getItem() instanceof ArmorItem armor && armor.getMaterial() == MUArmorMaterials.LAPIS_LAZULI);
        return isFullArmor ? enchantmentValue + 5 : enchantmentValue;
    }
}
