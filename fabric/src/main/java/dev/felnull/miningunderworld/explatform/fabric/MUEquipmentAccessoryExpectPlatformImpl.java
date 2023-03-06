package dev.felnull.miningunderworld.explatform.fabric;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import dev.felnull.miningunderworld.item.accessory.EquipmentAccessoryItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Supplier;

public class MUEquipmentAccessoryExpectPlatformImpl {
    private static final Supplier<TagKey<Item>> HAND_RING_TAG = Suppliers.memoize(() -> regTag(new ResourceLocation(getModId(), "hand/ring")));
    private static final Supplier<TagKey<Item>> OFFHAND_RING_TAG = Suppliers.memoize(() -> regTag(new ResourceLocation(getModId(), "offhand/ring")));

    public static String getModId() {
        return "trinkets";
    }

    public static List<TagKey<Item>> getRingItemTag() {
        return ImmutableList.of(HAND_RING_TAG.get(), OFFHAND_RING_TAG.get());
    }

    private static TagKey<Item> regTag(ResourceLocation location) {
        return TagKey.create(Registries.ITEM, location);
    }

    public static void initAccessoryItem(EquipmentAccessoryItem equipmentAccessoryItem) {
        if (equipmentAccessoryItem instanceof Item item) {
            TrinketsApi.registerTrinket(item, (Trinket) equipmentAccessoryItem);
        } else {
            throw new IllegalStateException("Not item");
        }
    }
}
