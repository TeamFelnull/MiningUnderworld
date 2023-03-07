package dev.felnull.miningunderworld.util;

import dev.felnull.miningunderworld.integration.EquipmentAccessoryIntegration;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * アクセサリ関係のユーティリティ
 */
public class MUAccessoryUtils {
    /**
     * アクセサリを装備してるかどうか
     *
     * @param livingEntity エンティティ
     * @param item         アイテム
     * @return 装備してるかどうか
     */
    public static boolean isEquipped(@NotNull LivingEntity livingEntity, @NotNull Item item) {
        return isEquipped(livingEntity, it -> it.is(item));
    }

    /**
     * アクセサリを装備してるかどうか
     *
     * @param livingEntity エンティティ
     * @param predicate    アイテムのフィルター
     * @return 装備してるかどうか
     */
    public static boolean isEquipped(@NotNull LivingEntity livingEntity, @NotNull Predicate<ItemStack> predicate) {
        if (StreamSupport.stream(livingEntity.getHandSlots().spliterator(), false).anyMatch(predicate))
            return true;

        if (EquipmentAccessoryIntegration.INSTANCE.isEnable() && EquipmentAccessoryIntegration.INSTANCE.isEquipped(livingEntity, predicate))
            return true;

        if (livingEntity instanceof Player player) {
            var inv = player.getInventory();
            return IntStream.range(0, 9).mapToObj(inv::getItem).anyMatch(predicate);
        }

        return false;
    }
}
