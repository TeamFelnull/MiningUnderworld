package dev.felnull.miningunderworld.client;

import dev.felnull.miningunderworld.client.handler.ClientHandler;
import dev.felnull.miningunderworld.client.renderer.MURenderTypes;

public class MiningUnderworldClient {
    public static void init() {
        MUItemProperties.init();
        MURenderTypes.init();

        ClientHandler.init();
    }
}
