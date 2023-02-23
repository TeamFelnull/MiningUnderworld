package dev.felnull.miningunderworld.dimension;

import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.feature.MUPlacedFeatures;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class MUBiomes {


    public static final Set<ResourceKey<Biome>> MU_BIOMES = new HashSet<>();
    public static final ResourceKey<Biome> DIRTY_CAVE = biomeKey("dirty_cave");
    public static final ResourceKey<Biome> COLLAPSING_CAVE = biomeKey("collapsing_cave");
    public static final ResourceKey<Biome> CRYSTAL_CAVE = biomeKey("crystal_cave");

    public static ResourceKey<Biome> biomeKey(String name){
        var key = ResourceKey.create(Registries.BIOME, MiningUnderworld.modLoc(name));
        MU_BIOMES.add(key);
        return key;
    }

    public static RegistrySetBuilder addToBuilder(RegistrySetBuilder builder) {//データパック自動生成に登録
        return builder.add(Registries.BIOME, c -> {
            var placedFeatures = c.lookup(Registries.PLACED_FEATURE);
            var configuredCarvers = c.lookup(Registries.CONFIGURED_CARVER);
            Supplier<BiomeGenerationSettings.Builder> genSup = () -> new BiomeGenerationSettings.Builder(placedFeatures, configuredCarvers);

            c.register(DIRTY_CAVE, dirtyCave(genSup));
            c.register(COLLAPSING_CAVE, collapsingCave(genSup));
            c.register(CRYSTAL_CAVE, crystalCave(genSup));
        });
    }

    public static Biome dirtyCave(Supplier<BiomeGenerationSettings.Builder> genSup) {
        var gen = defaultGeneration(genSup);
        //BiomeDefaultFeaturesにBiomeGenerationSettings.Builderの使い道いろいろある
        BiomeDefaultFeatures.addSwampVegetation(gen);//沼地の追加

        return defaultBiome(genSup)
                .generationSettings(gen.build())
                .temperature(0.114514F)
                .build();
    }

    public static Biome collapsingCave(Supplier<BiomeGenerationSettings.Builder> genSup) {
        return defaultBiome(genSup)
                .temperature(0.1919F)
                .build();
    }

    public static Biome crystalCave(Supplier<BiomeGenerationSettings.Builder> genSup) {
        return defaultBiome(genSup)
                .temperature(0.810F)
                .build();
    }

    public static Biome.BiomeBuilder defaultBiome(Supplier<BiomeGenerationSettings.Builder> genSup) {
        return defaultBiome(defaultAmbient(), defaultMobSpawning(), defaultGeneration(genSup), genSup);
    }

    public static Biome.BiomeBuilder defaultBiome(BiomeSpecialEffects.Builder ambient, MobSpawnSettings.Builder spawn, BiomeGenerationSettings.Builder gen, Supplier<BiomeGenerationSettings.Builder> genSup) {
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

    public static MobSpawnSettings.Builder defaultMobSpawning() {
        MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();

        //ウーパールーパーがデフォルト湧き
        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.AXOLOTL, 12, 4, 4));
        //ゾンビとかスケルトンとか一般モブ追加
        BiomeDefaultFeatures.commonSpawns(spawnInfo);

        return spawnInfo;
    }

    public static BiomeGenerationSettings.Builder defaultGeneration(Supplier<BiomeGenerationSettings.Builder> genSup){
        var gen = genSup.get();
        BiomeDefaultFeatures.addDefaultOres(gen);//鉱石追加
        BiomeDefaultFeatures.addExtraEmeralds(gen);//エメラルドも追加
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MUPlacedFeatures.TEST_FEATURE);//独自生成物追加
        return gen;
    }
}
