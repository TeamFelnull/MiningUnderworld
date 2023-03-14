package dev.felnull.miningunderworld.client.renderer;

import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.miningunderworld.fluid.MUFluids;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class MURenderTypes {
    public static void init() {
        RenderTypeRegistry.register(RenderType.translucent(),
                MUFluids.TAR.get(),
                MUFluids.FLOWING_TAR.get(),
                MUFluids.TEST_FLUID.get(),
                MUFluids.TEST_FLUID_FLOWING.get());

        RenderTypeRegistry.register(RenderType.translucent(),
                MUBlocks.CRYSTALS.stream()
                        .map(RegistrySupplier::get)
                        .toArray(Block[]::new));

        RenderTypeRegistry.register(RenderType.cutout(),
                MUBlocks.TAR_STAINS.get(),
                MUBlocks.SMALL_TAR_STAINS.get(),
                MUBlocks.SOAKED_TAR_STONE.get(),
                MUBlocks.SOAKED_TAR_DEEPSLATE.get(),
                MUBlocks.SOAKED_LAVA_STONE.get(),
                MUBlocks.SOAKED_LAVA_DEEPSLATE.get());
    }
}
