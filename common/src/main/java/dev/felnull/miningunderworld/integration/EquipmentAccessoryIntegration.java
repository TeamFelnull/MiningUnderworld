package dev.felnull.miningunderworld.integration;

import dev.felnull.miningunderworld.explatform.MUEquipmentAccessoryExpectPlatform;
import dev.felnull.miningunderworld.item.accessory.EquipmentAccessoryItem;
import dev.felnull.otyacraftengine.integration.BaseIntegration;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

/**
 * 装備可能なアクセサリ系MODとの連携
 */
public class EquipmentAccessoryIntegration extends BaseIntegration {
    public static final EquipmentAccessoryIntegration INSTANCE = new EquipmentAccessoryIntegration();

    @Override
    public String getModId() {
        return MUEquipmentAccessoryExpectPlatform.getModId();
    }

    @Override
    public boolean isConfigEnabled() {
        return true;
    }

    public List<TagKey<Item>> getRingItemTag() {
        return MUEquipmentAccessoryExpectPlatform.getRingItemTag();
    }

    public TagKey<Item> getBeltItemTag() {
        return MUEquipmentAccessoryExpectPlatform.getBeltItemTag();
    }

    public void initAccessoryItem(EquipmentAccessoryItem equipmentAccessoryItem) {
        MUEquipmentAccessoryExpectPlatform.initAccessoryItem(equipmentAccessoryItem);
    }

    public boolean isEquipped(LivingEntity livingEntity, Predicate<ItemStack> predicate) {
        return MUEquipmentAccessoryExpectPlatform.isEquipped(livingEntity, predicate);
    }

    public boolean equip(Player player, ItemStack stack) {
        return MUEquipmentAccessoryExpectPlatform.equip(player,stack);
    }
}
