package dev.felnull.miningunderworld.world.dimension.generation;

import com.mojang.datafixers.util.Pair;
import dev.felnull.miningunderworld.util.MUUtils;
import dev.felnull.otyacraftengine.util.OEDataGenUtils;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

//Feature:生成物の型
//ConfiguredFeature:生成物
//PlacedFeature:配置情報も含めた生成物、実際に使う形
public interface MUPlacedFeatures {
    Map<Pair<ResourceKey<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>>, Pair<ResourceKey<PlacedFeature>, List<PlacementModifier>>> DATAPACK_CACHE = new HashMap<>();

    ResourceKey<PlacedFeature> TEST_FEATURE = register("test",
            MUFeatures.TEST_FEATURE,//生成物の型
            new TestFeature.Config(19),//型に入れる情報
            commonOrePlacement(19, PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT));//生成物の配置方
    List<ResourceKey<PlacedFeature>> MANY_CRYSTALS = IntStream.range(0, 19).mapToObj(i -> register("crystal_" + i,
            MUFeatures.CRYSTAL,
            FeatureConfiguration.NONE,//１チャンク当たり（おそらく）の設置数上限256個だけどそれ以上設置したいから複数作る
            commonOrePlacement(256, PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT))).toList();
    ResourceKey<PlacedFeature> DUMMY = register("dummy",
            MUFeatures.TEST_FEATURE,
            new TestFeature.Config(19),
            commonOrePlacement(19, PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT));
    ResourceKey<PlacedFeature> COLLAPSING_CAVE_FLOOR = register("collapsing_cave_floor",
            MUFeatures.COLLAPSING_CAVE_FLOOR,
            FeatureConfiguration.NONE,
            commonOrePlacement(1, PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT));

    static <FC extends FeatureConfiguration, F extends Feature<FC>> ResourceKey<PlacedFeature> register(String name, F feature, FC config, List<PlacementModifier> placement) {
        var placedFeature = ResourceKey.create(Registries.PLACED_FEATURE, MUUtils.modLoc(name));
        if (OEDataGenUtils.isDataGenerating()) {
            var configuredFeature = ResourceKey.create(Registries.CONFIGURED_FEATURE, MUUtils.modLoc(name));
            DATAPACK_CACHE.put(Pair.of(configuredFeature, new ConfiguredFeature<>(feature, config)), Pair.of(placedFeature, placement));//データパック生成で使う形にしてキャッシュ
        }
        return placedFeature;
    }

    static RegistrySetBuilder addToBuilder(RegistrySetBuilder builder) {//データパック自動生成に登録
        return builder
                .add(Registries.CONFIGURED_FEATURE, context ->
                        DATAPACK_CACHE.forEach((configuredFeature, placedFeature) ->
                                context.register(configuredFeature.getFirst(), configuredFeature.getSecond())))
                .add(Registries.PLACED_FEATURE, context -> {
                    var features = context.lookup(Registries.CONFIGURED_FEATURE);
                    DATAPACK_CACHE.forEach((configuredFeature, placedFeature) ->
                            context.register(placedFeature.getFirst(), new PlacedFeature(features.getOrThrow(configuredFeature.getFirst()), placedFeature.getSecond())));
                });//キャッシュした値を使って生成
    }

    //以下privateアクセスにされてたバニラのPlacementModifierたち
    private static List<PlacementModifier> orePlacement(PlacementModifier placementModifier, PlacementModifier placementModifier2) {
        return List.of(placementModifier, InSquarePlacement.spread(), placementModifier2, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int i, PlacementModifier placementModifier) {
        return orePlacement(CountPlacement.of(i), placementModifier);
    }

    private static List<PlacementModifier> rareOrePlacement(int i, PlacementModifier placementModifier) {
        return orePlacement(RarityFilter.onAverageOnceEvery(i), placementModifier);
    }

}
