package dev.felnull.miningunderworld.util;

import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.explatform.MUExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class MUUtils {
    /**
     * MiningUnderworldのリソースロケーション
     *
     * @param path パス
     * @return リソースロケーション
     */
    public static ResourceLocation modLoc(@NotNull String path) {
        return new ResourceLocation(MiningUnderworld.MODID, path);
    }

    /**
     * タールに触れているかどうか
     *
     * @param entity エンティティ
     * @return 結果
     */
    public static boolean isInTar(@NotNull Entity entity) {
        return MUExpectPlatform.isInTar(entity);
    }

    /**
     * エンティティが持つ全てのアイテム
     *
     * @param entity エンティティ
     * @return アイテムスタック
     */
    @NotNull
    @Unmodifiable
    public static Stream<ItemStack> getAllHaveItem(@NotNull Entity entity) {
        if (entity instanceof ItemEntity itemEntity)
            return Stream.of(itemEntity.getItem());

        if (entity instanceof ItemFrame itemFrame)
            return Stream.of(itemFrame.getItem());

        if (entity instanceof Player player)
            return player.inventoryMenu.getItems().stream();

        if (entity instanceof LivingEntity livingEntity)
            return StreamSupport.stream(livingEntity.getAllSlots().spliterator(), false);

        return Stream.of();
    }

    /**
     * フル装備かどうか
     *
     * @param livingEntity  エンティティ
     * @param armorMaterial 指定のアーマーマテリアル
     * @return 結果
     */
    public static boolean isFullArmor(LivingEntity livingEntity, ArmorMaterial armorMaterial) {
        var armors = livingEntity.getArmorSlots();

        for (ItemStack stack : armors) {
            if (!(stack.getItem() instanceof ArmorItem armorItem) || !armorMaterial.equals(armorItem.getMaterial()))
                return false;
        }

        return true;
    }
}
