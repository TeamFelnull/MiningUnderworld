package dev.felnull.miningunderworld.forge.handler;

import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.data.MiningUnderworldDataGenerator;
import dev.felnull.otyacraftengine.forge.data.CrossDataGeneratorAccesses;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MiningUnderworld.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenHandler {

    @SubscribeEvent
    public static void onDataGen(GatherDataEvent event) {
        MiningUnderworldDataGenerator.init(CrossDataGeneratorAccesses.create(event));
    }
}
