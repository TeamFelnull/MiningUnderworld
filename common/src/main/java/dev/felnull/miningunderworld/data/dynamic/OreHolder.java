package dev.felnull.miningunderworld.data.dynamic;

import com.google.gson.JsonObject;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.felnull.miningunderworld.util.MUUtils;
import dev.felnull.otyacraftengine.resources.PlatformResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class OreHolder extends PlatformResourceReloadListener<OreHolder.Loader> {

    private static final OreHolder INSTANCE = new OreHolder();
    private static final ResourceLocation ORE = MUUtils.modLoc("ore");

    public static Map<Integer, ResourceLocation> idToOre = new HashMap<>();//(key = 3, value = minecraft:iron)的な

    public static OreHolder getInstance() {
        return INSTANCE;
    }

    public void initServer() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, this);
    }

    @Override
    protected Loader prepare(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        return new Loader(resourceManager, profilerFiller);
    }

    @Override
    protected void apply(@NotNull Loader loader, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        //諸事情（apply以外でも読み込みたい）によりnewした段階でロード終わるためここには何もなし
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return ORE;
    }

    public static class Loader {

        public Loader(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
            this(resourceManager);//profilerなんて必要ねえんだよ
        }

        public Loader(@NotNull ResourceManager resourceManager) {
            var ores = new TreeSet<ResourceLocation>();//取得した鉱石保存場所、重複したものはまとめて
            resourceManager
                    .listResources("worldgen/configured_feature", loc -> loc.getPath().contains("ore_"))
                    .forEach(((location, resource) -> {
                        var ore = parse(MUUtils.getJson(resource));//jsonから鉱石取得
                        if (ore != null)//あれば
                            ores.addAll(ore);
                    }));

            int i = 0;
            for (ResourceLocation ore : ores)
                idToOre.put(i++, ore);//クリスタルの番号（辞書順）と鉱石を対応させる
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
}
