package dev.felnull.miningunderworld.integration;

import dev.felnull.miningunderworld.explatform.MUEquipmentAccessoryExpectPlatform;
import dev.felnull.miningunderworld.item.accessory.EquipmentAccessoryItem;
import dev.felnull.otyacraftengine.integration.BaseIntegration;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.List;

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

    public void initAccessoryItem(EquipmentAccessoryItem equipmentAccessoryItem) {
        MUEquipmentAccessoryExpectPlatform.initAccessoryItem(equipmentAccessoryItem);
    }
}
