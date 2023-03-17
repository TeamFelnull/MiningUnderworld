package dev.felnull.miningunderworld.world.dimension.generation;

import dev.architectury.registry.registries.DeferredRegister;
import dev.felnull.miningunderworld.MiningUnderworld;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MUFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(MiningUnderworld.MODID, Registries.FEATURE);
    public static final Feature<TestFeature.Config> TEST_FEATURE = register("test", new TestFeature());
    public static final Feature<NoneFeatureConfiguration> CRYSTAL = register("crystal", new CrystalFeature());
    public static final Feature<NoneFeatureConfiguration> COLLAPSING_CAVE_FLOOR = register("collapsing_cave_floor", new CollapsingCaveFloorFeature());

    public static <T extends FeatureConfiguration> Feature<T> register(String name, Feature<T> feature) {
        FEATURES.register(name, () -> feature);
        return feature;
    }

    public static void init() {
        FEATURES.register();
    }
}
