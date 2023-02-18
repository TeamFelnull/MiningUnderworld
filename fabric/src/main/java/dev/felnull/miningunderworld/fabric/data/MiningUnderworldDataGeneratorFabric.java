package dev.felnull.miningunderworld.fabric.data;

import dev.felnull.miningunderworld.data.MiningUnderworldDataGenerator;
import dev.felnull.otyacraftengine.fabric.data.CrossDataGeneratorAccesses;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MiningUnderworldDataGeneratorFabric implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        MiningUnderworldDataGenerator.init(CrossDataGeneratorAccesses.create(fabricDataGenerator));
    }
}
