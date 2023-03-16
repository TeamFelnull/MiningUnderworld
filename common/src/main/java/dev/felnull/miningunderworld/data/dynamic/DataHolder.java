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
    public static Set<ResourceLocation> oreConfiguredFeatures = new HashSet<>();
    public static Set<ResourceLocation> orePlacedFeatures = new HashSet<>();
    public static Map<ResourceLocation, String> muBiomes = new HashMap<>();

    public static Set<String> blacklists = new HashSet<>(List.of("debris", "nether", "deltas"));
    public static boolean collectedBiomes;
    public static String joinedOres = "";

    public static void load(@NotNull ResourceManager resourceManager) {
        resourceManager.listResources("worldgen/configured_feature", loc -> loc.getPath().contains("ore_"))
                .forEach((location, resource) -> {
                    var ore = parse(MUUtils.getJson(resource));//jsonから鉱石取得
                    if (ore != null && !ore.isEmpty()) {//あれば
                        oreLocs.addAll(ore);//鉱石追加
                        oreConfiguredFeatures.add(MUUtils.subPrefixAndSuffix(location, "worldgen/configured_feature/", ".json"));//ConfiguredFeature追加
                    }
                });
        resourceManager.listResources("worldgen/placed_feature", loc -> loc.getPath().contains("ore_"))
                .forEach((location, resource) -> {
                    if (oreConfiguredFeatures.contains(new ResourceLocation(MUUtils.getJson(resource).get("feature").getAsString()))
                            && blacklists.stream().noneMatch(it -> location.getPath().contains(it)))
                        orePlacedFeatures.add(MUUtils.subPrefixAndSuffix(location, "worldgen/placed_feature/", ".json"));
                });
        orePlacedFeatures.forEach(System.out::println);

        //ここでは加工する前のbiomeを取得したく、生成時には加工後の者を取得したい
        //この処理の前後でバイオーム取得先を変えるため、collectingBiomesでMUPackResourceのバイオーム取得部分を制御
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
                    .filter(it -> !it.contains("deepslate") && (it.contains("_ore") || jo.get("type").getAsString().equals("minecraft:scattered_ore")))
                    .map(ResourceLocation::new)
                    .toList();
        return null;//なければnull
    }
}
