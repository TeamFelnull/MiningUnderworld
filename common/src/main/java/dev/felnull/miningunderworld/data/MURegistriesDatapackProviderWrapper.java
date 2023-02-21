package dev.felnull.miningunderworld.data;

import dev.felnull.miningunderworld.dimension.MUBiomes;
import dev.felnull.miningunderworld.dimension.MiningUnderworldDimension;
import dev.felnull.miningunderworld.feature.MUPlacedFeatures;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.RegistriesDatapackProviderWrapper;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class MURegistriesDatapackProviderWrapper extends RegistriesDatapackProviderWrapper {
    private static final RegistrySetBuilder BUILDER =
            MiningUnderworldDimension.addToBuilder(
                    MUBiomes.addToBuilder(
                            MUPlacedFeatures.addToBuilder(
                                    new RegistrySetBuilder())));

    public MURegistriesDatapackProviderWrapper(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, lookup, BUILDER, crossDataGeneratorAccess);
    }
}
