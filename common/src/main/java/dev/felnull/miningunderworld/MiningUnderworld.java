package dev.felnull.miningunderworld;

import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.miningunderworld.dimension.MiningUnderworldDimension;
import dev.felnull.miningunderworld.fluid.MUFluids;
import dev.felnull.miningunderworld.handler.CommonHandler;
import dev.felnull.miningunderworld.item.MUItems;
import dev.felnull.miningunderworld.recipe.MURecipeSerializers;
import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.resources.ResourceLocation;

public class MiningUnderworld {
    public static final String MODID = "miningunderworld";

    public static void init() {
        MUFluids.init();
        MUItems.init();
        MUBlocks.init();
        MiningUnderworldDimension.init();
        CommonHandler.init();
        MURecipeSerializers.init();
    }

    public static ResourceLocation modLoc(String name) {
        return MUUtils.modLoc(name);
    }
}
