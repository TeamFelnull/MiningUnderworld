package dev.felnull.miningunderworld.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.felnull.miningunderworld.MiningUnderworld;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MiningUnderworld.MODID)
public class MiningUnderworldForge {
    public MiningUnderworldForge() {
        EventBuses.registerModEventBus(MiningUnderworld.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        MiningUnderworld.init();
    }
}
