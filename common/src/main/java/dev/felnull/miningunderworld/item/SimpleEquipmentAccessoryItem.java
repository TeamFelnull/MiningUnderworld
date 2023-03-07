package dev.felnull.miningunderworld.item;

import dev.felnull.miningunderworld.item.accessory.AccessoryType;
import org.jetbrains.annotations.NotNull;

/**
 * 何も効果がない装備可能なアクセサリ
 */
public class SimpleEquipmentAccessoryItem extends BaseEquipmentAccessoryItem {
    private final AccessoryType accessoryType;

    public SimpleEquipmentAccessoryItem(AccessoryType accessoryType, Properties properties) {
        super(properties);
        this.accessoryType = accessoryType;
    }

    @Override
    public @NotNull AccessoryType getAccessoryType() {
        return accessoryType;
    }
}
