package dev.felnull.miningunderworld.data.dynamic;

import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;

public class TextureHolder {
    public static byte[] crystalTexture;//加工元のクリスタルテクスチャ

    public static void load(ResourceManager resourceManager) {
        try {
            crystalTexture = resourceManager.getResource(MUUtils.modLoc("textures/block/crystal_base.png")).get().open().readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
