package dev.felnull.miningunderworld.util;

import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.explatform.MUExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
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

/**
 * ユーティリティ
 */
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
     * 目の高さまでタールに浸ってるかどうか
     *
     * @param entity エンティティ
     * @return 結果
     */
    public static boolean isEyeInTar(@NotNull Entity entity) {
        return MUExpectPlatform.isEyeInTar(entity);
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

    /**
     * 二種のARGBをアルファブレンド
     *
     * @param baseARGB   基となるARGB
     * @param addingARGB 追加するARGB
     * @return アルファブレンドされたARGB
     */
    public static int blend(int baseARGB, int addingARGB) {
        var alpha = (float) FastColor.ARGB32.alpha(baseARGB) / 0xFF;//追加される方の不透明度
        var beta = (float) FastColor.ARGB32.alpha(addingARGB) / 0xFF;//追加する方の不透明度
        var blendedAlpha = 1 - (1 - beta) * (1 - alpha);
        var baseCoefficient = (1-beta) * alpha / blendedAlpha;
        var addingCoefficient = beta / blendedAlpha;

        return FastColor.ARGB32.color(
                (int) Mth.clamp(blendedAlpha * 0xFF, 0, 0xFF),
                (int) Mth.clamp(baseCoefficient * FastColor.ARGB32.red(baseARGB) +addingCoefficient * FastColor.ARGB32.red(addingARGB), 0, 0xFF),
                (int) Mth.clamp(baseCoefficient * FastColor.ARGB32.green(baseARGB) +addingCoefficient * FastColor.ARGB32.green(addingARGB), 0, 0xFF),
                (int) Mth.clamp(baseCoefficient * FastColor.ARGB32.blue(baseARGB) +addingCoefficient * FastColor.ARGB32.blue(addingARGB), 0, 0xFF));
    }
}
