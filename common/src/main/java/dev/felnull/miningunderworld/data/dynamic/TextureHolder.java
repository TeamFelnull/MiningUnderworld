package dev.felnull.miningunderworld.data.dynamic;

import com.google.gson.JsonObject;
import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class TextureHolder {
    public static Map<ResourceLocation, BufferedImage> oreToTexture = new HashMap<>();//自動生成した鉱石特有テクスチャ

    //テクスチャを読み込みテクスチャを生成する
    public static void load(ResourceManager resourceManager) {
        var baseCrystalResource = resourceManager.getResource(MUUtils.modLoc("textures/block/crystal_base.png")).get();

        //鉱石事に生成
        OreHolder.oreLocs.forEach(ore -> {
            var oreModelResource = resourceManager.getResource(MUUtils.addPrefixAndSuffix(ore, "models/block/", ".json"));
            var oreColor = new AtomicInteger();//鉱石特有色を計算するという意思表示

            //RGB計算
            if (oreModelResource.isPresent()) {
                var oreTextures = parse(MUUtils.getJson(oreModelResource.get()))//ブロックモデルの情報から
                        .stream().map(oreTexLoc -> resourceManager.getResource(MUUtils.addPrefixAndSuffix(oreTexLoc, "textures/", ".png")))
                        .filter(Optional::isPresent).map(Optional::get).map(MUUtils::getImage).toList();//テクスチャ取得

                var stonePixel = oreTextures.get(0).getRGB(0, 0);//石部分の色
                oreColor.set(stonePixel);//初期値
                oreTextures.forEach(oreTexture -> //石の色から一番違う色をoreColorに順次更新→鉱石部分の色が取得される！
                        MUUtils.forEachPixel(oreTexture, (x, y) -> oreColor.set(moreDifferentFrom(stonePixel, oreColor.get(), oreTexture.getRGB(x, y)))));
            } else //取得できなきゃ白
                oreColor.set(0xFFFFFF);

            //不透明度は約0.75で固定
            oreColor.set(0xBF000000 + (oreColor.get() & 0x00FFFFFF));

            var baseCrystalTexture = MUUtils.getImage(baseCrystalResource);//基テクスチャ
            MUUtils.forEachPixel(baseCrystalTexture, (x, y) ->
                    baseCrystalTexture.setRGB(x, y, MUUtils.blend(oreColor.get(), baseCrystalTexture.getRGB(x, y))));//下から鉱石特有色を重ねる
            oreToTexture.put(ore, baseCrystalTexture);//鉱石テクスチャ確定
        });
    }

    public static List<ResourceLocation> parse(JsonObject jo) {
        return jo.get("textures").getAsJsonObject().entrySet()
                .stream()
                .map(e -> e.getValue().getAsString())
                .map(ResourceLocation::new)
                .toList();
    }

    public static int moreDifferentFrom(int base, int a, int b) {
        var baseHSB = argbToHSB(base);
        var aHSB = argbToHSB(a);
        var bHSB = argbToHSB(b);
        var aDif = getHSBDif(aHSB, baseHSB);
        var bDif = getHSBDif(bHSB, baseHSB);
        var maxDif = 114514;//極端に違う色で困るようなことがあれば使う、でもそんなことない。rgbのみで判断してた時の名残
        return aDif < maxDif && bDif < maxDif
                ? aDif > bDif ? a : b
                : bDif >= maxDif ? a : b;
    }

    public static float getHSBDif(float[] a, float[] b) {
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]) + Math.abs(a[2] - b[2]);
    }

    public static float[] argbToHSB(int argb) {
        return Color.RGBtoHSB(argb & 0xFF0000, argb & 0xFF00, argb & 0xFF, null);
    }
}
