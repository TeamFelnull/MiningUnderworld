package dev.felnull.miningunderworld;

import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.miningunderworld.item.MUItems;
import net.minecraft.resources.ResourceLocation;

public class MiningUnderworld {
    public static final String MODID = "miningunderworld";

    public static void init() {
        MUItems.init();
        MUBlocks.init();
    }

    public static ResourceLocation resourceLocation(String name){
        return new ResourceLocation(MiningUnderworld.MODID, name);
    }
}
