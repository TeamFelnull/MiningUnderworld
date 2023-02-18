package dev.felnull.miningunderworld.fabric;

import dev.felnull.miningunderworld.MiningUnderworld;
import net.fabricmc.api.ModInitializer;

public class MiningUnderworldFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MiningUnderworld.init();
    }
}
