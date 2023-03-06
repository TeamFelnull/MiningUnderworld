package dev.felnull.miningunderworld.fabric.mixin.integration.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.felnull.miningunderworld.item.accessory.EquipmentAccessoryItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = EquipmentAccessoryItem.class, remap = false)
public interface EquipmentAccessoryItemExtender extends Trinket {
    @Override
    default void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        var ths = (EquipmentAccessoryItem) this;
        ths.accessoryTick(stack, entity);
    }
}
