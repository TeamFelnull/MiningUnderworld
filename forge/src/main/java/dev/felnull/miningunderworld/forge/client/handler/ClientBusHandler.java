package dev.felnull.miningunderworld.forge.client.handler;

import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.client.MiningUnderworldClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MiningUnderworld.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientBusHandler {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MiningUnderworldClient.init();
    }
}