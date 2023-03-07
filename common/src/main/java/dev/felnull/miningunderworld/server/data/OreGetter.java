package dev.felnull.miningunderworld.server.data;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.felnull.miningunderworld.util.MUUtils;
import dev.felnull.otyacraftengine.resources.PlatformResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

public class OreGetter extends PlatformResourceReloadListener<OreGetter.Loader> {

    private static final OreGetter INSTANCE = new OreGetter();
    private static final ResourceLocation ORE = MUUtils.modLoc("ore");
    public Set<ResourceLocation> ores = new HashSet<>();

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
        ores = loader.ores;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return ORE;
    }

    public static class Loader {
        public static final Logger LOGGER = LogManager.getLogger(Loader.class);
        private static final Gson GSON = new Gson();
        public Set<ResourceLocation> ores;

        protected Loader(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
            profilerFiller.startTick();

            var builder = new ImmutableSet.Builder<ResourceLocation>();

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

            resourceManager.
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
                        .filter(it -> !it.contains("deepslate") && ( it.contains("_ore") || jo.get("type").getAsString().equals("minecraft:scattered_ore")))
                        .map(ResourceLocation::new)
                        .toArray(ResourceLocation[]::new);
            return null;ResourceManager
        }
    }
}
