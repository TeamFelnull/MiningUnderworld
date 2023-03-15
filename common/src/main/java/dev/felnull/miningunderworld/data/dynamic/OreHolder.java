package dev.felnull.miningunderworld.data.dynamic;

import com.google.gson.JsonObject;
import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OreHolder {

    public static Set<ResourceLocation> oreLocs = new HashSet<>();

    public static void load(@NotNull ResourceManager resourceManager) {
        resourceManager.listResources("worldgen/configured_feature", loc -> loc.getPath().contains("ore_"))
                .forEach((location, resource) -> {
                    var ore = parse(MUUtils.getJson(resource));//jsonから鉱石取得
                    if (ore != null)//あれば
                        oreLocs.addAll(ore);
                });
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
