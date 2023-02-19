package dev.felnull.miningunderworld.dimension;

import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.block.MUBlocks;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;

import java.util.List;
import java.util.OptionalLong;

public class MiningUnderworldDimension {
    public static final ResourceKey<DimensionType> MU_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, MiningUnderworld.resourceLocation("mu_type"));
    public static final ResourceKey<NoiseGeneratorSettings> MU_GENERATOR = ResourceKey.create(Registries.NOISE_SETTINGS, MiningUnderworld.resourceLocation("mu_generator"));
    public static final ResourceKey<LevelStem> MINING_UNDERWORLD = ResourceKey.create(Registries.LEVEL_STEM, MiningUnderworld.resourceLocation("mining_underworld"));

    public static RegistrySetBuilder addToBuilder(RegistrySetBuilder builder){//データパック自動生成に登録
        return builder
                .add(Registries.DIMENSION_TYPE, c -> c.register(MU_TYPE, muType()))//基本情報
                .add(Registries.NOISE_SETTINGS, c -> c.register(MU_GENERATOR, muGenerator(c)))//地形生成
                .add(Registries.LEVEL_STEM, c -> c.register(MINING_UNDERWORLD, miningUnderworld(c)));//ディメンション本体
    }

    private static DimensionType muType() {
        //各引数の意味は以下参照
        //https://minecraftjapan.miraheze.org/wiki/%E3%83%87%E3%83%BC%E3%82%BF%E3%83%91%E3%83%83%E3%82%AF/%E3%83%AF%E3%83%BC%E3%83%AB%E3%83%89%E3%81%AE%E3%82%AB%E3%82%B9%E3%82%BF%E3%83%9E%E3%82%A4%E3%82%BA
        return new DimensionType(OptionalLong.empty(),
                true,
                false,
                false,
                true,
                3f,
                false,
                false,
                -64,
                320,
                320,
                BlockTags.INFINIBURN_OVERWORLD,
                BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                1.0F,
                new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 0));
    }

    private static NoiseGeneratorSettings muGenerator(BootstapContext<NoiseGeneratorSettings> context) {
        return new NoiseGeneratorSettings(
                NoiseSettings.create(0, 128, 1, 2),
                MUBlocks.TEST_BLOCK.get().defaultBlockState(),
                Blocks.WATER.defaultBlockState(),
                MUNoiseRouter.create(context),
                MUSurfaceRule.create(),
                List.of(),
                19,
                false,
                false,
                true,
                false);
    }

    private static LevelStem miningUnderworld(BootstapContext<LevelStem> context) {
        var dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        var biomes = context.lookup(Registries.BIOME);
        var noiseSettings = context.lookup(Registries.NOISE_SETTINGS);
        return new LevelStem(dimTypes.getOrThrow(MU_TYPE),
                new NoiseBasedChunkGenerator(
                        MultiNoiseBiomeSource.Preset.OVERWORLD.biomeSource(biomes),
                        noiseSettings.getOrThrow(MU_GENERATOR)));
    }
}
