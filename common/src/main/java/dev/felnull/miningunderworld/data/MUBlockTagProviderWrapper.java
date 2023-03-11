package dev.felnull.miningunderworld.data;

import dev.architectury.registry.registries.RegistrySupplier;
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
        providerAccess.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                MUBlocks.LOOT_POT.get(),
                MUBlocks.GOLDEN_LOOT_POT.get(),
                MUBlocks.SOAKED_TAR_STONE.get(),
                MUBlocks.SOAKED_TAR_DEEPSLATE.get(),
                MUBlocks.SOAKED_LAVA_STONE.get(),
                MUBlocks.SOAKED_LAVA_DEEPSLATE.get(),
                MUBlocks.MINING_TNT.get());

        providerAccess.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                MUBlocks.CRYSTALS.stream().map(RegistrySupplier::get).toArray(Block[]::new));

        providerAccess.tag(BlockTags.STONE_ORE_REPLACEABLES).add(
                MUBlocks.SOAKED_TAR_STONE.get(),
                MUBlocks.SOAKED_LAVA_STONE.get());

        providerAccess.tag(BlockTags.DEEPSLATE_ORE_REPLACEABLES).add(
                MUBlocks.SOAKED_TAR_DEEPSLATE.get(),
                MUBlocks.SOAKED_LAVA_DEEPSLATE.get());

        providerAccess.tag(BlockTags.OVERWORLD_CARVER_REPLACEABLES).add(
                MUBlocks.SOAKED_TAR_STONE.get(),
                MUBlocks.SOAKED_TAR_DEEPSLATE.get(),
                MUBlocks.SOAKED_LAVA_STONE.get(),
                MUBlocks.SOAKED_LAVA_DEEPSLATE.get());
    }
}
