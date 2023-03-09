package dev.felnull.miningunderworld.data.builtin;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.felnull.miningunderworld.util.MUUtils;
import dev.felnull.otyacraftengine.resources.PlatformResourceReloadListener;
import net.minecraft.client.searchtree.SearchTree;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.*;
import java.util.stream.IntStream;

public class OreGetter extends PlatformResourceReloadListener<OreGetter.Loader> {

    private static final OreGetter INSTANCE = new OreGetter();
    private static final ResourceLocation ORE = MUUtils.modLoc("ore");
    public Map<ResourceLocation, ResourceLocation> ores = new HashMap<>();//(key = crystal_3, value = iron)的な

    public static OreGetter getInstance() {
        return INSTANCE;
    }

    public void init() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, this);
    }

    @Override
    protected Loader prepare(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        return new Loader(resourceManager, profilerFiller);
    }

    @Override
    protected void apply(@NotNull Loader loader, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        IntStream.range(0, loader.ores.size()).forEach(i -> ores.put(MUUtils.modLoc("crystal_" + i), loader.ores.get(i)));
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return ORE;
    }

    public static class Loader {
        public static final Logger LOGGER = LogManager.getLogger(Loader.class);
        private static final Gson GSON = new Gson();
        public List<ResourceLocation> ores;
        public byte[] crystalTexture;

        protected Loader(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
            profilerFiller.startTick();
            var builder = new ImmutableList.Builder<ResourceLocation>();

            /*var a = """
                            {
                              "type": "minecraft:crafting_shaped",
                              "category": "equipment",
                              "key": {
                                "A": {
                                  "item": "miningunderworld:amethyst_ingot"
                                },
                                "S": {
                                  "item": "minecraft:stick"
                                }
                              },
                              "pattern": [
                                "AA",
                                "AS",
                                "AS"
                              ],
                              "result": {
                                "item": "miningunderworld:amethyst_axe"
                              }
                            }
                    """.getBytes();

            resourceManager.listPacks().filter(it -> it instanceof VanillaPackResources).forEach(pack ->
                    new Resource(pack, () -> new ByteArrayInputStream(a)));*///動的にレシピ追加しようとしたけどResource追加先がなかった

            resourceManager.listResources("worldgen/configured_feature", loc -> loc.getPath().contains("ore_")).forEach(((location, resource) -> {
                profilerFiller.push(location.toString());
                try (Reader reader = resource.openAsReader()) {
                    var ore = parse(GSON.fromJson(reader, JsonObject.class));
                    if (ore != null)
                        builder.add(ore);
                } catch (Exception e) {
                    LOGGER.error("Error occurred while loading configured feature resource json " + location, e);
                }
                profilerFiller.pop();
            }));

            profilerFiller.endTick();

            ores = builder.build();
        }

        public static ResourceLocation[] parse(JsonObject jo) {
            if (jo.get("type").getAsString().equals("minecraft:ore") || jo.get("type").getAsString().equals("minecraft:scattered_ore"))
                return jo.get("config").getAsJsonObject()
                        .get("targets").getAsJsonArray()
                        .asList().stream()
                        .map(it -> it.getAsJsonObject()
                                .get("state").getAsJsonObject()
                                .get("Name").getAsString())
                        .filter(it -> !it.contains("deepslate") && (it.contains("_ore") || jo.get("type").getAsString().equals("minecraft:scattered_ore")))
                        .map(ResourceLocation::new)
                        .toArray(ResourceLocation[]::new);
            return null;
        }
    }
}
