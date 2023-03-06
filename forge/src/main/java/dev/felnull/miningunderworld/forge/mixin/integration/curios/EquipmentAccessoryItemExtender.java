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
}
