package dev.felnull.miningunderworld.explatform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.felnull.miningunderworld.item.accessory.EquipmentAccessoryItem;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.List;

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
    public static void initAccessoryItem(EquipmentAccessoryItem equipmentAccessoryItem) {
        throw new AssertionError();
    }
}
