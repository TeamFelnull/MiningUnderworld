package dev.felnull.miningunderworld.data;

import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.miningunderworld.item.MUItems;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.BlockLootTableProviderWrapper;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class MUBlockLootTableProviderWrapper extends BlockLootTableProviderWrapper {
    public MUBlockLootTableProviderWrapper(PackOutput packOutput, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, crossDataGeneratorAccess);
    }

    @Override
    public void generateBlockLootTables(BlockLootSubProvider blockLoot, BlockLootTableProviderAccess providerAccess) {
        providerAccess.dropSelf(MUBlocks.TEST_BLOCK.get());
        providerAccess.dropSelf(MUBlocks.UNSTABLE_STANCE.get());
        providerAccess.dropSelf(MUBlocks.COLLAPSING_STANCE.get());
        providerAccess.dropSelf(MUBlocks.SOLID_TAR.get());
        providerAccess.dropOther(MUBlocks.SEMISOLID_TAR.get(), MUItems.TAR_CHUNK.get());
        providerAccess.dropSelf(MUBlocks.CONDENSED_TAR.get());

        providerAccess.dropWhenSilkTouch(MUBlocks.LOOT_POT.get());
        providerAccess.dropWhenSilkTouch(MUBlocks.GOLDEN_LOOT_POT.get());
        MUBlocks.CRYSTALS.stream().map(RegistrySupplier::get).forEach(providerAccess::dropWhenSilkTouch);//forgeのrunData時にエラーされないためだけに残す
        providerAccess.dropSelf(MUBlocks.BLUE_SAND.get());
        providerAccess.dropSelf(MUBlocks.WHITE_SAND.get());
        providerAccess.dropWhenSilkTouch(MUBlocks.BLUE_CLAY.get());

        providerAccess.add(MUBlocks.TAR_STAINS.get(), BlockLootSubProvider.noDrop());
        providerAccess.add(MUBlocks.SMALL_TAR_STAINS.get(), BlockLootSubProvider.noDrop());

        providerAccess.dropOther(MUBlocks.SOAKED_TAR_STONE.get(), Blocks.STONE);
        providerAccess.dropOther(MUBlocks.SOAKED_TAR_DEEPSLATE.get(), Blocks.DEEPSLATE);
        providerAccess.dropOther(MUBlocks.SOAKED_LAVA_STONE.get(), Blocks.STONE);
        providerAccess.dropOther(MUBlocks.SOAKED_LAVA_DEEPSLATE.get(), Blocks.DEEPSLATE);
        providerAccess.dropSelf(MUBlocks.MINING_TNT.get());

        //providerAccess.dropSelf(MUBlocks.NAZO.get());
    }

    @Override
    public Iterable<Block> getKnownBlocks() {
        return extract(MUBlocks.BLOCKS);
    }
}
