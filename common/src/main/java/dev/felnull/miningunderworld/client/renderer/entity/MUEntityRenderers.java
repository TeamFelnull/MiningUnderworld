package dev.felnull.miningunderworld.client.renderer.entity;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.felnull.miningunderworld.entity.MUEntityTypes;

public class MUEntityRenderers {
    public static void init() {
        EntityRendererRegistry.register(MUEntityTypes.MINING_TNT, MiningTntRenderer::new);
        EntityRendererRegistry.register(MUEntityTypes.CAVE_BAT, CaveBatRenderer::new);
        EntityRendererRegistry.register(MUEntityTypes.EXCRETA, ExcretaRenderer::new);
        EntityRendererRegistry.register(MUEntityTypes.STRICT_FALLING_BLOCK, StrictFallingBlockRenderer::new);
    }
}
