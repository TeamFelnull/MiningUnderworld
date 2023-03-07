package dev.felnull.miningunderworld.forge.mixin.integration.curios;

import dev.felnull.miningunderworld.item.accessory.EquipmentAccessoryItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

@Mixin(value = EquipmentAccessoryItem.class, remap = false)
public interface EquipmentAccessoryItemExtender extends ICurioItem {
    @Override
    default void curioTick(SlotContext slotContext, ItemStack stack) {
        var ths = (EquipmentAccessoryItem) this;
        ths.accessoryTick(stack, slotContext.entity());
    }

    @Override
    default void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        var ths = (EquipmentAccessoryItem) this;
        ths.onAccessoryEquip(stack, slotContext.entity());
    }

    @Override
    default void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        var ths = (EquipmentAccessoryItem) this;
        ths.onAccessoryUnequip(stack, slotContext.entity());
    }

    @Override
    default boolean canEquip(SlotContext slotContext, ItemStack stack) {
        var ths = (EquipmentAccessoryItem) this;
        return ICurioItem.super.canEquip(slotContext, stack) && ths.canAccessoryEquip(stack, slotContext.entity());
    }

    @Override
    default boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        var ths = (EquipmentAccessoryItem) this;
        return ICurioItem.super.canUnequip(slotContext, stack) && ths.canAccessoryUnequip(stack, slotContext.entity());
    }
}
