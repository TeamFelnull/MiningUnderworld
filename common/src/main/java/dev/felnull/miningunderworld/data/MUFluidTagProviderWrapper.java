package dev.felnull.miningunderworld.data;

import dev.felnull.miningunderworld.fluid.MUFluidTags;
import dev.felnull.miningunderworld.fluid.MUFluids;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.FluidTagProviderWrapper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.material.Fluid;

import java.util.concurrent.CompletableFuture;

public class MUFluidTagProviderWrapper extends FluidTagProviderWrapper {
    public MUFluidTagProviderWrapper(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, lookup, crossDataGeneratorAccess);
    }

    @Override
    public void generateTag(IntrinsicTagProviderAccess<Fluid> providerAccess) {
        providerAccess.tag(MUFluidTags.TAR).add(MUFluids.TAR.get(), MUFluids.FLOWING_TAR.get());
    }
}
