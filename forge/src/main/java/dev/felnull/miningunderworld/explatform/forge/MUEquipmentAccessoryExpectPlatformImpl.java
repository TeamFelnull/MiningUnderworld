package dev.felnull.miningunderworld.explatform.forge;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import dev.felnull.miningunderworld.item.accessory.EquipmentAccessoryItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Supplier;

public class MUEquipmentAccessoryExpectPlatformImpl {
    private static final Supplier<TagKey<Item>> RING_TAG = Suppliers.memoize(() -> ItemTags.create(new ResourceLocation(getModId(), "ring")));

    public static String getModId() {
        return "curios";
    }

    public static List<TagKey<Item>> getRingItemTag() {
        return ImmutableList.of(RING_TAG.get());
    }

    public static void initAccessoryItem(EquipmentAccessoryItem equipmentAccessoryItem) {
    }
}
