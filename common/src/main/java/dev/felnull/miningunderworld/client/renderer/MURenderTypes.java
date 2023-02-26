package dev.felnull.miningunderworld.client.renderer;

import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.felnull.miningunderworld.block.MUBlocks;
import net.minecraft.client.renderer.RenderType;

public class MURenderTypes {
    public static void init() {
        RenderTypeRegistry.register(RenderType.translucent(), MUBlocks.TEST_FLUID.get(), MUBlocks.TAR.get());
    }
}
