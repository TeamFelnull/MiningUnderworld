package dev.felnull.miningunderworld.data;

import dev.felnull.miningunderworld.entity.MUDamageSources;
import dev.felnull.miningunderworld.world.dimension.MiningUnderworldDimension;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.RegistriesDatapackProviderWrapper;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class MURegistriesDatapackProviderWrapper extends RegistriesDatapackProviderWrapper {
    private static final RegistrySetBuilder BUILDER =
            MUDamageSources.addToBuilder(
                    MiningUnderworldDimension.addToBuilder(new RegistrySetBuilder()));

    public MURegistriesDatapackProviderWrapper(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, lookup, BUILDER, crossDataGeneratorAccess);
    }

    //https://github1s.com/TeamTwilight/twilightforest/blob/HEAD/src/main/java/twilightforest/data/RegistryDataGenerator.java <-参考
    protected static CompletableFuture<HolderLookup.Provider> unitedLookup(CompletableFuture<HolderLookup.Provider> lookup) {
        return lookup.thenApplyAsync(it -> BUILDER.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), it), Util.backgroundExecutor());
    }
}
