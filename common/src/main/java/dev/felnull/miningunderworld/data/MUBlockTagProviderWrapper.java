package dev.felnull.miningunderworld.data;

import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.BlockTagProviderWrapper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class MUBlockTagProviderWrapper extends BlockTagProviderWrapper {
    public MUBlockTagProviderWrapper(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, lookup, crossDataGeneratorAccess);
    }

    @Override
    public void generateTag(IntrinsicTagProviderAccess<Block> providerAccess) {
        providerAccess.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(MUBlocks.LOOT_POT.get(), MUBlocks.GOLDEN_LOOT_POT.get());
    }
}
