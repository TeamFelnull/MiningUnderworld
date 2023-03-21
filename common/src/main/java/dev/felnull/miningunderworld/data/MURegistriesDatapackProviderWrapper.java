package dev.felnull.miningunderworld.data;

import dev.felnull.miningunderworld.entity.MUDamageSources;
import dev.felnull.miningunderworld.world.dimension.MiningUnderworldDimension;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.RegistriesDatapackProviderWrapper;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class MURegistriesDatapackProviderWrapper extends RegistriesDatapackProviderWrapper {
    private static final RegistrySetBuilder BUILDER =
            MUDamageSources.addToBuilder(
                    MiningUnderworldDimension.addToBuilder(new RegistrySetBuilder()));

    public MURegistriesDatapackProviderWrapper(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, lookup, BUILDER, crossDataGeneratorAccess);
    }
}
