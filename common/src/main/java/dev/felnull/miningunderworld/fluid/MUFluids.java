package dev.felnull.miningunderworld.fluid;

import dev.architectury.core.fluid.ArchitecturyFlowingFluid;
import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.core.fluid.SimpleArchitecturyFluidAttributes;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.miningunderworld.item.MUItems;
import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class MUFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(MiningUnderworld.MODID, Registries.FLUID);

    //https://github.com/architectury/architectury-api/blob/1.19.3/testmod-common/src/main/java/dev/architectury/test/registry/TestRegistries.java
    public static final ArchitecturyFluidAttributes TEST_FLUID_ATTRIBUTES = SimpleArchitecturyFluidAttributes.ofSupplier(() -> MUFluids.TEST_FLUID_FLOWING, () -> MUFluids.TEST_FLUID)
            .convertToSource(true)
            .flowingTexture(new ResourceLocation("block/water_flow"))
            .sourceTexture(new ResourceLocation("block/water_still"))
            .blockSupplier(() -> MUBlocks.TEST_FLUID)
            .bucketItemSupplier(() -> MUItems.TEST_FLUID_BUCKET)
            .overlayTexture(new ResourceLocation("block/lava_still"))
            .color(0xFF0000);

    public static final ArchitecturyFluidAttributes TAR_ATTRIBUTES = SimpleArchitecturyFluidAttributes.ofSupplier(() -> MUFluids.FLOWING_TAR, () -> MUFluids.TAR)
            .convertToSource(true)
            .flowingTexture(MUUtils.modLoc("block/tar_flow"))
            .sourceTexture(MUUtils.modLoc("block/tar_still"))
            .blockSupplier(() -> MUBlocks.TAR)
            .bucketItemSupplier(() -> MUItems.TAR_BUCKET)
            .overlayTexture(MUUtils.modLoc("block/tar_still"))
            .temperature(302);

    public static final RegistrySupplier<FlowingFluid> TEST_FLUID = register("test_fluid", () -> new ArchitecturyFlowingFluid.Source(TEST_FLUID_ATTRIBUTES));
    public static final RegistrySupplier<FlowingFluid> TEST_FLUID_FLOWING = register("test_fluid_flowing", () -> new ArchitecturyFlowingFluid.Flowing(TEST_FLUID_ATTRIBUTES));

    public static final RegistrySupplier<FlowingFluid> TAR = register("tar", () -> new ArchitecturyFlowingFluid.Source(TAR_ATTRIBUTES));
    public static final RegistrySupplier<FlowingFluid> FLOWING_TAR = register("flowing_tar", () -> new ArchitecturyFlowingFluid.Flowing(TAR_ATTRIBUTES));


    private static RegistrySupplier<FlowingFluid> register(String name, Supplier<FlowingFluid> fluidSupplier) {
        return FLUIDS.register(name, fluidSupplier);
    }

    public static void init() {
        FLUIDS.register();
    }
}
