package dev.felnull.miningunderworld.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.explatform.MUExpectPlatform;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;
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

    public static void forEachPixel(BufferedImage image, BiConsumer<Integer, Integer> func) {
        IntStream.range(0, image.getWidth()).forEach(x -> IntStream.range(0, image.getHeight()).forEach(y -> func.accept(x, y)));
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
        var baseCoefficient = (1 - beta) * alpha / blendedAlpha;
        var addingCoefficient = beta / blendedAlpha;

        return FastColor.ARGB32.color(
                (int) Mth.clamp(blendedAlpha * 0xFF, 0, 0xFF),
                (int) Mth.clamp(baseCoefficient * FastColor.ARGB32.red(baseARGB) + addingCoefficient * FastColor.ARGB32.red(addingARGB), 0, 0xFF),
                (int) Mth.clamp(baseCoefficient * FastColor.ARGB32.green(baseARGB) + addingCoefficient * FastColor.ARGB32.green(addingARGB), 0, 0xFF),
                (int) Mth.clamp(baseCoefficient * FastColor.ARGB32.blue(baseARGB) + addingCoefficient * FastColor.ARGB32.blue(addingARGB), 0, 0xFF));
    }

    public static BufferedImage getImage(Resource resource) {
        try {
            return getImage(resource.open().readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage getImage(byte[] bytes) {
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Gson GSON = new Gson();

    public static JsonObject getJson(Resource resource) {
        try (var reader = resource.openAsReader()) {
            return GSON.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getStr(Resource resource) {
        try {
            return new String(resource.open().readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Vec3 randomBaseVector(RandomSource random) {
        var theta = random.nextFloat() * Math.PI;
        var phi = random.nextFloat() * 2 * Math.PI;
        return new Vec3(Math.sin(theta) * Math.cos(phi), Math.sin(theta) * Math.sin(phi), Math.cos(theta));
    }

    public static Vec3i toI(Vec3 v) {
        return new Vec3i(v.x, v.y, v.z);
    }

    public static <T> T getRandomlyFrom(Collection<T> c, RandomSource rand) {
        var list = new ArrayList<>(c);
        return list.get((int) (rand.nextFloat() * list.size()));//0<nextFloat<1のため配列の範囲外エラーは起きない
    }

    public static ResourceLocation addPrefixAndSuffix(ResourceLocation loc, String prefix, String suffix) {
        return loc.withPath(prefix + loc.getPath() + suffix);
    }

    public static ResourceLocation subPrefixAndSuffix(ResourceLocation loc, String prefix, String suffix) {
        return loc.withPath(loc.getPath().replaceAll(prefix + "(.*)" + suffix, "$1"));
    }
}
