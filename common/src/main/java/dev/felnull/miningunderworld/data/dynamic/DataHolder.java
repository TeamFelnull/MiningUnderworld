package dev.felnull.miningunderworld.data.dynamic;

import com.google.gson.JsonObject;
import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DataHolder {

    public static Set<ResourceLocation> oreLocs = new HashSet<>();
    public static Set<ResourceLocation> orePlacedFeatures = new HashSet<>();
    public static Map<ResourceLocation, String> muBiomes = new HashMap<>();

    public static boolean collectedBiomes;
    public static String joinedOres;

    public static void load(@NotNull ResourceManager resourceManager) {
        var overworldOres = new HashSet<>();
        resourceManager.listResources("worldgen/configured_feature", loc -> loc.getPath().contains("ore_"))
                .forEach((location, resource) -> {
                    var ore = parse(MUUtils.getJson(resource));//jsonから鉱石取得
                    if (!ore.isEmpty()) {//あれば
                        oreLocs.addAll(ore.stream().filter(it -> !it.getPath().contains("deepslate")).toList());//深層以外の鉱石追加
                        if (ore.stream().anyMatch(it -> it.getPath().contains("deepslate")))//深層バージョンを持つ＝オーバーワールドに生成される鉱石なら
                            overworldOres.add(MUUtils.subPrefixAndSuffix(location, "worldgen/configured_feature/", ".json"));//保持
                    }
                });
        resourceManager.listResources("worldgen/placed_feature", loc -> loc.getPath().contains("ore_"))
                .forEach((location, resource) -> {
                    if (overworldOres.contains(new ResourceLocation(MUUtils.getJson(resource).get("feature").getAsString())))
                        orePlacedFeatures.add(MUUtils.subPrefixAndSuffix(location, "worldgen/placed_feature/", ".json"));//オーバーワールドに生成される鉱石追加
                });

        //ここでは加工する前のbiomeを取得したく、生成時には加工後のものを取得したい
        //この処理の前後でバイオーム取得先を変えるため、collectedBiomesでMUPackResourceのバイオーム取得部分を制御
        resourceManager.listResources("worldgen/biome", loc -> loc.getNamespace().equals(MiningUnderworld.MODID))
                .forEach((location, resource) -> muBiomes.put(location, MUUtils.getStr(resource)));
        joinedOres = String.join(",", DataHolder.orePlacedFeatures.stream().map(it -> "\"" + it + "\"").toArray(String[]::new));
        collectedBiomes = true;
    }

    public static List<ResourceLocation> parse(JsonObject jo) {
        if (jo.get("type").getAsString().equals("minecraft:ore") || jo.get("type").getAsString().equals("minecraft:scattered_ore"))//FeatureをOreかScatteredOreに限定
            return jo.get("config").getAsJsonObject()//Featureが限定さればFeatureConfigurationも限定されるため、ヌルポ気にせず目的のものを取得できる
                    .get("targets").getAsJsonArray()
                    .asList().stream()
                    .map(it -> it.getAsJsonObject()
                            .get("state").getAsJsonObject()
                            .get("Name").getAsString())
                    .filter(it -> it.contains("_ore") || jo.get("type").getAsString().equals("minecraft:scattered_ore"))
                    .map(ResourceLocation::new)
                    .toList();
        return List.of();
    }
}
