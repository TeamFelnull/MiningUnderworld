package dev.felnull.miningunderworld.explatform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.felnull.miningunderworld.item.accessory.EquipmentAccessoryItem;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

public class MUEquipmentAccessoryExpectPlatform {
    @ExpectPlatform
    public static String getModId() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static List<TagKey<Item>> getRingItemTag() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static TagKey<Item> getBeltItemTag() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void initAccessoryItem(EquipmentAccessoryItem equipmentAccessoryItem) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isEquipped(LivingEntity livingEntity, Predicate<ItemStack> predicate) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean equip(Player player, ItemStack stack) {
        throw new AssertionError();
    }
}
