package dev.felnull.miningunderworld.dimension;

import com.mojang.serialization.Codec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.block.MUBlocks;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;

import java.util.List;
import java.util.OptionalLong;

//参考：https://github1s.com/TeamTwilight/twilightforest/blob/1.19.x/src/main/java/twilightforest/init/TFDimensionSettings.java
public class MiningUnderworldDimension {
    public static final ResourceKey<DimensionType> MU_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, MiningUnderworld.modLoc("mu_type"));
    public static final ResourceKey<NoiseGeneratorSettings> MU_GENERATOR = ResourceKey.create(Registries.NOISE_SETTINGS, MiningUnderworld.modLoc("mu_generator"));
    public static final ResourceKey<LevelStem> MINING_UNDERWORLD = ResourceKey.create(Registries.LEVEL_STEM, MiningUnderworld.modLoc("mining_underworld"));

    public static RegistrySetBuilder addToBuilder(RegistrySetBuilder builder){//データパック自動生成に登録
        return builder
                .add(Registries.DIMENSION_TYPE, c -> c.register(MU_TYPE, muType()))//基本情報
                .add(Registries.NOISE_SETTINGS, c -> c.register(MU_GENERATOR, muGenerator(c)))//地形生成
                .add(Registries.LEVEL_STEM, c -> c.register(MINING_UNDERWORLD, miningUnderworld(c)));//ディメンション本体
    }

    public static final DeferredRegister<Codec<? extends BiomeSource>> BIOME_SOURCES = DeferredRegister.create(MiningUnderworld.MODID, Registries.BIOME_SOURCE);
    public static final RegistrySupplier<Codec<? extends BiomeSource>> MU_BIOME = BIOME_SOURCES.register("mu_biome", () -> MUBiomeSource.MU_CODEC);

    public static void init() {
        BIOME_SOURCES.register();//バイオーム生成方法
    }

    public static final int minY = -64;
    public static final int maxY = 320;
    public static final int height = maxY - minY;

    private static DimensionType muType() {
        return new DimensionType(OptionalLong.empty(),
                true,
                false,
                false,
                true,
                3f,
                false,
                false,
                minY,
                height,
                height,
                BlockTags.INFINIBURN_OVERWORLD,
                BuiltinDimensionTypes.OVERWORLD_EFFECTS,//ディメンション固有のレンダー（？）
                1.0F,
                new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 0));
    }

    private static NoiseGeneratorSettings muGenerator(BootstapContext<NoiseGeneratorSettings> context) {
        return new NoiseGeneratorSettings(
                NoiseSettings.create(minY, height, 1, 1),
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
                        MUBiomeSource.create(biomes),
                        noiseSettings.getOrThrow(MU_GENERATOR)));
    }
}
