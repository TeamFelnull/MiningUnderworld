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

    @Override
    default void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        var ths = (EquipmentAccessoryItem) this;
        ths.onAccessoryEquip(stack, entity);
    }

    @Override
    default void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        var ths = (EquipmentAccessoryItem) this;
        ths.onAccessoryUnequip(stack, entity);
    }


    @Override
    default boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        var ths = (EquipmentAccessoryItem) this;
        return Trinket.super.canEquip(stack, slot, entity) && ths.canAccessoryEquip(stack, entity);
    }

    @Override
    default boolean canUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        var ths = (EquipmentAccessoryItem) this;
        return Trinket.super.canUnequip(stack, slot, entity) && ths.canAccessoryEquip(stack, entity);
    }
}
