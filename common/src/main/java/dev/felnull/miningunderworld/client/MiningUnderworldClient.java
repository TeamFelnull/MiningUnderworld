package dev.felnull.miningunderworld.client;

import dev.felnull.miningunderworld.client.handler.ClientHandler;
import dev.felnull.miningunderworld.client.renderer.MURenderTypes;
import dev.felnull.miningunderworld.client.renderer.entity.MUEntityRenderers;
import dev.felnull.miningunderworld.network.MUPackets;
import dev.felnull.miningunderworld.server.data.OreGetter;

public class MiningUnderworldClient {
    public static void preInit() {
        MUEntityRenderers.init();

        OreGetter.getInstance().init();
    }

    public static void init() {
        MUItemProperties.init();
        MURenderTypes.init();
        MUPackets.initClient();

        ClientHandler.init();
    }
}
