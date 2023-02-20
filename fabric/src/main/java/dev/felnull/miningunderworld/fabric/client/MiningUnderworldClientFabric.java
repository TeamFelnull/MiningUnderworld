package dev.felnull.miningunderworld.fabric.client;

import dev.felnull.miningunderworld.client.MiningUnderworldClient;
import net.fabricmc.api.ClientModInitializer;

public class MiningUnderworldClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MiningUnderworldClient.init();
    }
}
