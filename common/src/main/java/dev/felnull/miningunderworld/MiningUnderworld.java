package dev.felnull.miningunderworld;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import dev.felnull.miningunderworld.block.CrystalBlock;
import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.miningunderworld.client.MiningUnderworldClient;
import dev.felnull.miningunderworld.data.dynamic.OreHolder;
import dev.felnull.miningunderworld.dimension.MiningUnderworldDimension;
import dev.felnull.miningunderworld.entity.MUEntityTypes;
import dev.felnull.miningunderworld.fluid.MUFluids;
import dev.felnull.miningunderworld.handler.CommonHandler;
import dev.felnull.miningunderworld.item.MUItems;
import dev.felnull.miningunderworld.network.MUPackets;
import dev.felnull.miningunderworld.particles.MUParticleTypes;
import dev.felnull.miningunderworld.recipe.MURecipeSerializers;
import dev.felnull.miningunderworld.server.handler.ServerHandler;
import dev.felnull.otyacraftengine.util.OEDataGenUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MiningUnderworld {
    public static final String MODID = "miningunderworld";
    public static final CreativeTabRegistry.TabSupplier MOD_TAB = CreativeTabRegistry.create(
            new ResourceLocation(MiningUnderworld.MODID, MiningUnderworld.MODID),
            builder -> builder
                    .icon(() -> new ItemStack(Items.APPLE))
                    .displayItems((flag , output, op) -> {
                        MUItems.ITEMS.forEach(i -> output.accept(i.get()));
                        MUBlocks.BLOCK_ITEMS.forEach(b -> {
                            if(!(b.get() instanceof CrystalBlock.Item crystal && crystal.getCrystalBlock().ORE_ID >= OreHolder.idToOre.size()))
                                output.accept(b.get());
                        });
                    }));

    public static void init() {
        OreHolder.getInstance().initServer();
        MUPackets.initServer();
        MUFluids.init();
        MUItems.init();
        MUBlocks.init();
        MUEntityTypes.init();
        MURecipeSerializers.init();
        MiningUnderworldDimension.init();
        MUParticleTypes.init();

        CommonHandler.init();
        ServerHandler.init();


        if (!OEDataGenUtils.isDataGenerating())
            EnvExecutor.runInEnv(Env.CLIENT, () -> MiningUnderworldClient::preInit);
    }
}
