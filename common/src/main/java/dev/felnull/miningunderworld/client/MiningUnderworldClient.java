package dev.felnull.miningunderworld.client;

import dev.felnull.miningunderworld.client.handler.ClientHandler;
import dev.felnull.miningunderworld.client.model.MUModelLayers;
import dev.felnull.miningunderworld.client.particle.MUClientParticleProviders;
import dev.felnull.miningunderworld.client.renderer.MURenderTypes;
import dev.felnull.miningunderworld.client.renderer.entity.MUEntityRenderers;
import dev.felnull.miningunderworld.network.MUPackets;

public class MiningUnderworldClient {
    public static void preInit() {
        MUEntityRenderers.init();
        MUModelLayers.init();
        MUClientParticleProviders.init();
    }

    public static void init() {
        MUItemProperties.init();
        MURenderTypes.init();
        MUPackets.initClient();

        ClientHandler.init();
    }
}
