package dev.felnull.miningunderworld;

import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.miningunderworld.item.MUItems;

public class MiningUnderworld {
    public static final String MODID = "miningunderworld";

    public static void init() {
        MUItems.init();
        MUBlocks.init();
    }
}
