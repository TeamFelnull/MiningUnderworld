package dev.felnull.miningunderworld.dimension;

import dev.felnull.miningunderworld.dimension.generation.MUCarvers;
import dev.felnull.miningunderworld.dimension.generation.MUPlacedFeatures;
import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

//Biomeの要素をBiomeGenerationSettingsから細かく指定する
//BiomeSpecialEffects：バイオームにいるときの感覚的効果。水や草や葉っぱの色、霧やパーティクル、音。
//MobSpawnSettings:モブスポーン設定
//BiomeGenerationSettings:バイオーム特有の地形生成。穴開けたり(Carver)生成物(Feature)追加
public class MUBiomes {
    public static final Map<ResourceKey<Biome>, Function<BiomeGenerationSettings.Builder, Biome>> MU_BIOMES = new HashMap<>();
    public static final ResourceKey<Biome> DIRTY_CAVE = register("dirty_cave", MUBiomes::dirtyCave);
    public static final ResourceKey<Biome> COLLAPSING_CAVE = register("collapsing_cave", MUBiomes::collapsingCave);
    public static final ResourceKey<Biome> CRYSTAL_CAVE = register("crystal_cave", MUBiomes::crystalCave);
    public static final ResourceKey<Biome> TAR_TAINTED_CAVE = register("tar_tainted_cave", MUBiomes::tarTaintedCave);

    public static List<ResourceLocation> biomeLocs() {
        return MU_BIOMES.keySet().stream().map(ResourceKey::location).toList();
    }

    public static ResourceKey<Biome> register(String name, Function<BiomeGenerationSettings.Builder, Biome> f) {
        var key = ResourceKey.create(Registries.BIOME, MUUtils.modLoc(name));
        MU_BIOMES.put(key, f);
        return key;
    }

    public static RegistrySetBuilder addToBuilder(RegistrySetBuilder builder) {//データパック自動生成に登録
        return builder.add(Registries.BIOME, c -> {
            var placedFeatures = c.lookup(Registries.PLACED_FEATURE);
            var configuredCarvers = c.lookup(Registries.CONFIGURED_CARVER);
            MU_BIOMES.forEach((k, f) -> c.register(k, f.apply(new BiomeGenerationSettings.Builder(placedFeatures, configuredCarvers))));
        });
    }

    public static Biome dirtyCave(BiomeGenerationSettings.Builder gen) {
        var amb = defaultAmbient();
        //ハエエフェクト

        var spawn = defaultSpawn();
        //糞コウモリ

        //BiomeDefaultFeaturesにBiomeGenerationSettings.Builderの使い道いろいろある
        //きたない液体池
        //汚い液体シート
        //固めた糞尿鉱石

        return defaultBiome(gen)
                .temperature(0.5F)
                .build();
    }

    public static Biome collapsingCave(BiomeGenerationSettings.Builder gen) {
        //天上は砂利多め→SurfaceRule
        gen.addCarver(GenerationStep.Carving.AIR, MUCarvers.COLLAPSING_CANYON);//崖多め
        gen.addFeature(GenerationStep.Decoration.RAW_GENERATION, MUPlacedFeatures.COLLAPSING_CAVE_FLOOR);//複数種類のモロ感足場

        //破壊すると確率で周りのブロック崩落、確率で連鎖→ServerHandler

        return defaultBiome(gen)
                .temperature(-0.5F)
                .build();
    }

    public static Biome crystalCave(BiomeGenerationSettings.Builder gen) {
        var amb = defaultAmbient()
                .skyColor(0x6f6fe3)
                .fogColor(0x98a4ff)
                .waterColor(0x00ddff)
                .waterFogColor(0xa6e2ff);

        MUPlacedFeatures.MANY_CRYSTALS.forEach(crystal ->
                gen.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, crystal));//全宝石のクリスタル柱フィーチャー
        gen.addFeature(GenerationStep.Decoration.LAKES, MiscOverworldPlacements.SPRING_WATER);
        //砂漠感

        return defaultBiome(amb, gen)
                .temperature(1)
                .downfall(1)
                .build();
    }

    public static Biome tarTaintedCave(BiomeGenerationSettings.Builder gen) {
        //TODO タールの汚れの表面を追加
        //  gen.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MUPlacedFeatures.TAR_STAINS);

        //DECORATIONはただのタグ？
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.LUSH_CAVES_CEILING_VEGETATION);
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.CAVE_VINES);
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.LUSH_CAVES_CLAY);
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.LUSH_CAVES_VEGETATION);
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.ROOTED_AZALEA_TREE);
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.SPORE_BLOSSOM);
        gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.CLASSIC_VINES);

        return defaultBiome(gen)
                .temperature(-1F)//今のとこ温度だけでバイオーム分布決めてるから温度被ると生成されなくなる可能性が出てくる、biomeごとにambient決めるならそれにも依存できて温度被っても問題なくなる
                .build();
    }

    public static Biome.BiomeBuilder defaultBiome(BiomeGenerationSettings.Builder gen) {
        return defaultBiome(defaultAmbient(), defaultSpawn(), defaultGeneration(gen));
    }

    public static Biome.BiomeBuilder defaultBiome(BiomeSpecialEffects.Builder ambient, BiomeGenerationSettings.Builder gen) {
        return defaultBiome(ambient, defaultSpawn(), defaultGeneration(gen));
    }

    public static Biome.BiomeBuilder defaultBiome(MobSpawnSettings.Builder spawn, BiomeGenerationSettings.Builder gen) {
        return defaultBiome(defaultAmbient(), spawn, defaultGeneration(gen));
    }

    public static Biome.BiomeBuilder defaultBiome(BiomeSpecialEffects.Builder ambient, MobSpawnSettings.Builder spawn, BiomeGenerationSettings.Builder gen) {
        return new Biome.BiomeBuilder()
                .specialEffects(ambient.build())
                .mobSpawnSettings(spawn.build())
                .generationSettings(gen.build())
                .precipitation(Biome.Precipitation.NONE)//何も降らない
                .downfall(0)//降水確率0
                .temperatureAdjustment(Biome.TemperatureModifier.NONE)//ナニコレ
                .temperature(0.5F);
    }

    //黄昏のまま
    public static BiomeSpecialEffects.Builder defaultAmbient() {
        return new BiomeSpecialEffects.Builder()
                .fogColor(0xC0FFD8)
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533)
                .skyColor(0x20224A)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                /*.backgroundMusic(TFConfiguredFeatures.TFMUSICTYPE)*/;
    }

    public static MobSpawnSettings.Builder defaultSpawn() {
        MobSpawnSettings.Builder spawn = new MobSpawnSettings.Builder();

        //ウーパールーパーがデフォルト湧き
        spawn.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.AXOLOTL, 12, 4, 4));
        //ゾンビとかスケルトンとか一般モブ追加
        BiomeDefaultFeatures.commonSpawns(spawn);

        return spawn;
    }

    public static BiomeGenerationSettings.Builder defaultGeneration(BiomeGenerationSettings.Builder gen) {
        return gen
                .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MUPlacedFeatures.TEST_FEATURE)//独自生成物追加
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MUPlacedFeatures.DUMMY)//後で全modの鉱石に差し替えるためのダミー
                .addCarver(GenerationStep.Carving.AIR, Carvers.CAVE)//洞窟
                .addCarver(GenerationStep.Carving.AIR, Carvers.CAVE_EXTRA_UNDERGROUND);//洞窟２;;
    }
}
