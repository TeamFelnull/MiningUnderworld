package dev.felnull.miningunderworld;

import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.miningunderworld.client.MiningUnderworldClient;
import dev.felnull.miningunderworld.dimension.MiningUnderworldDimension;
import dev.felnull.miningunderworld.entity.MUEntityTypes;
import dev.felnull.miningunderworld.fluid.MUFluids;
import dev.felnull.miningunderworld.handler.CommonHandler;
import dev.felnull.miningunderworld.item.MUItems;
import dev.felnull.miningunderworld.network.MUPackets;
import dev.felnull.miningunderworld.recipe.MURecipeSerializers;
import dev.felnull.miningunderworld.server.handler.ServerHandler;
import dev.felnull.otyacraftengine.util.OEDataGenUtils;

public class MiningUnderworld {
    public static final String MODID = "miningunderworld";

    public static void init() {
        MUPackets.initServer();
        MUFluids.init();
        MUItems.init();
        MUBlocks.init();
        MUEntityTypes.init();
        MURecipeSerializers.init();
        MiningUnderworldDimension.init();

        CommonHandler.init();
        ServerHandler.init();

        if (!OEDataGenUtils.isDataGenerating())
            EnvExecutor.runInEnv(Env.CLIENT, () -> MiningUnderworldClient::preInit);
    }
}
