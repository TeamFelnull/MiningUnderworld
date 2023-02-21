package dev.felnull.miningunderworld.data;

import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.BlockStateAndModelProviderWrapper;
import dev.felnull.otyacraftengine.data.provider.BlockTagProviderWrapper;
import net.minecraft.data.PackOutput;

public class MUBlockStateAndModelProviderWrapper extends BlockStateAndModelProviderWrapper {
    public MUBlockStateAndModelProviderWrapper(PackOutput packOutput, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, crossDataGeneratorAccess);
    }

    @Override
    public void generateStatesAndModels(BlockStateAndModelProviderAccess providerAccess) {
        providerAccess.genSimpleCubeBlockStateModelAndItemModel(MUBlocks.TEST_BLOCK.get());

    }
}
