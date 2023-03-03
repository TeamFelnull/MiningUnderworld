package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.Temp;
import dev.felnull.miningunderworld.item.MUArmorMaterials;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnchantmentHelper.class)
public class ModifyEnchantmentValue {

    @Redirect(method = "selectEnchantment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;getEnchantmentValue()I"))
    private static int modifyEnchantmentValue(Item instance) {
        var enchantmentValue = instance.getEnchantmentValue();
        if(Temp.armorSlotsReferringNow.get() == null)//null→アーマーが取得されなかった→エンチャ台以外からselectEnchantmentが呼ばれた
            return enchantmentValue;
        var isFullArmor = Temp.armorSlotsReferringNow.get().stream().allMatch(item ->
                item.getItem() instanceof ArmorItem armor && armor.getMaterial() == MUArmorMaterials.LAPIS_LAZULI);
        Temp.armorSlotsReferringNow.set(null);//エンチャ台で呼んだら毎回nullに戻して、アーマーが取得されたかどうか<=>nullじゃないかどうか、とする
        return isFullArmor ? enchantmentValue + 20 : enchantmentValue;
    }
}
