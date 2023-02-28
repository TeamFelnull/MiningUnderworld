package dev.felnull.miningunderworld.util;

import com.google.common.collect.ImmutableList;
import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.explatform.MUExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

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
    public static List<ItemStack> getAllHaveItem(@NotNull Entity entity) {
        if (entity instanceof ItemEntity itemEntity)
            return ImmutableList.of(itemEntity.getItem());

        if (entity instanceof ItemFrame itemFrame)
            return ImmutableList.of(itemFrame.getItem());

        if (entity instanceof Player player)
            return player.inventoryMenu.getItems();

        if (entity instanceof LivingEntity livingEntity)
            return ImmutableList.copyOf(livingEntity.getAllSlots());

        return ImmutableList.of();
    }
}
