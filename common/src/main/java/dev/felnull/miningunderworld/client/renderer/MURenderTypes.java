package dev.felnull.miningunderworld.client.renderer;

import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.felnull.miningunderworld.block.MUBlocks;
import net.minecraft.client.renderer.RenderType;

public class MURenderTypes {
    public static void init() {
       /* RenderTypeRegistry.register(RenderType.translucent(),
                MUFluids.TAR.get(),
                MUFluids.FLOWING_TAR.get(),
                MUFluids.TEST_FLUID.get(),
                MUFluids.TEST_FLUID_FLOWING.get());

        RenderTypeRegistry.register(RenderType.translucent(),
                MUBlocks.TAR.get());*/

        RenderTypeRegistry.register(RenderType.cutout(),
                MUBlocks.TAR_STAINS.get(),
                MUBlocks.SMALL_TAR_STAINS.get());
    }
}
