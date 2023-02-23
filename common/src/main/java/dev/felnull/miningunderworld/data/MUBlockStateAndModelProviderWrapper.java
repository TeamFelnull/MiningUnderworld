package dev.felnull.miningunderworld.data;

import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.BlockStateAndModelProviderWrapper;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class MUBlockStateAndModelProviderWrapper extends BlockStateAndModelProviderWrapper {
    public MUBlockStateAndModelProviderWrapper(PackOutput packOutput, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, crossDataGeneratorAccess);
    }

    @Override
    public void generateStatesAndModels(BlockStateAndModelProviderAccess providerAccess) {
        providerAccess.genSimpleCubeBlockStateModelAndItemModel(MUBlocks.TEST_BLOCK.get());

        fluidModelAndState(providerAccess, MUBlocks.TEST_FLUID.get(), modLoc("block/test_block"));
        fluidModelAndState(providerAccess, MUBlocks.TAR.get(), modLoc("block/tar_still"));
    }

    private void fluidModelAndState(BlockStateAndModelProviderAccess providerAccess, Block block, ResourceLocation particle) {
        providerAccess.genSimpleBlockState(block, providerAccess.genParticleOnlyModel(block, particle));
    }
}
