package dev.felnull.miningunderworld.item.accessory;

import dev.felnull.miningunderworld.integration.EquipmentAccessoryIntegration;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public enum AccessoryType {
    NONE(() -> null),
    RING(EquipmentAccessoryIntegration.INSTANCE::getRingItemTag);

    private final Supplier<List<TagKey<Item>>> tagKeys;

    AccessoryType(Supplier<List<TagKey<Item>>> tagKeys) {
        this.tagKeys = tagKeys;
    }

    @Nullable
    public List<TagKey<Item>> getTagKeys() {
        if (!EquipmentAccessoryIntegration.INSTANCE.isEnableElement())
            return null;
        return tagKeys.get();
    }
}
