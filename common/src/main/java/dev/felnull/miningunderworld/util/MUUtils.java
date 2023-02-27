package dev.felnull.miningunderworld.util;

import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.explatform.MUExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public final class MUUtils {
    /**
     * MiningUnderworldのリソースロケーション
     *
     * @param path パス
     * @return リソースロケーション
     */
    public static ResourceLocation modLoc(String path) {
        return new ResourceLocation(MiningUnderworld.MODID, path);
    }

    /**
     * タールに触れているかどうか
     *
     * @param entity エンティティ
     * @return 結果
     */
    public static boolean isInTar(Entity entity) {
        return MUExpectPlatform.isInTar(entity);
    }
}
