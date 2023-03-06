package dev.felnull.miningunderworld.item.accessory;

import dev.felnull.miningunderworld.integration.EquipmentAccessoryIntegration;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * 装備可能なアクセサリアイテムにこのインターフェースを実装してください
 */
public interface EquipmentAccessoryItem {
    /**
     * コンストラクタで呼び出し必須
     */
    default void initAccessoryItem() {
        if (EquipmentAccessoryIntegration.INSTANCE.isEnable())
            EquipmentAccessoryIntegration.INSTANCE.initAccessoryItem(this);
    }

    /**
     * アクセサリの装備箇所を指定、
     * ここを変更した場合はデータジェネレータを動かしてください
     *
     * @return タイプ
     */
    @NotNull
    AccessoryType getAccessoryType();

    /**
     * アクセサリを装備時に毎チック呼び出し
     *
     * @param stack        アイテム
     * @param livingEntity エンティティ
     */
    void accessoryTick(ItemStack stack, LivingEntity livingEntity);
}
