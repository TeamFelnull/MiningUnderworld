package dev.felnull.miningunderworld.dimension.generation;

import dev.architectury.registry.registries.DeferredRegister;
import dev.felnull.miningunderworld.MiningUnderworld;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class MUFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(MiningUnderworld.MODID, Registries.FEATURE);
    public static final Feature<TestFeature.Config> TEST_FEATURE = register("test", new TestFeature());
    public static final Feature<CrystalFeature.Config> CRYSTAL_FEATURE = register("crystal", new CrystalFeature());

    public static <T extends FeatureConfiguration> Feature<T> register(String name, Feature<T> feature) {
        FEATURES.register(name, () -> feature);
        return feature;
    }

    public static void init() {
        FEATURES.register();
    }
}
