package dev.felnull.miningunderworld.block;

import dev.architectury.core.block.ArchitecturyLiquidBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.fluid.MUFluids;
import dev.felnull.miningunderworld.item.MUCreativeModeTab;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.Function;
import java.util.function.Supplier;

public class MUBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MiningUnderworld.MODID, Registries.BLOCK);
    private static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(MiningUnderworld.MODID, Registries.ITEM);
    public static final RegistrySupplier<Block> TEST_BLOCK = register("test_block", () -> new Block(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT).strength(0.5F).sound(SoundType.GRAVEL)));

    public static final RegistrySupplier<LiquidBlock> TEST_FLUID = registerBlockOnly("test_fluid", () -> new ArchitecturyLiquidBlock(MUFluids.TEST_FLUID, BlockBehaviour.Properties.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable()));
    public static final RegistrySupplier<LiquidBlock> TAR = registerBlockOnly("tar", () -> new ArchitecturyLiquidBlock(MUFluids.TAR, BlockBehaviour.Properties.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable()));

    private static <B extends Block> RegistrySupplier<B> registerBlockOnly(String name, Supplier<B> block) {
        return BLOCKS.register(name, block);
    }

    private static RegistrySupplier<Block> register(String name, Supplier<Block> block) {
        return register(name, block, n -> new BlockItem(n, new Item.Properties().arch$tab(MUCreativeModeTab.MOD_TAB)));
    }

    private static RegistrySupplier<Block> register(String name, Supplier<Block> block, Function<Block, Item> blockItem) {
        var blockr = BLOCKS.register(name, block);
        BLOCK_ITEMS.register(name, () -> blockItem.apply(blockr.get()));
        return blockr;
    }

    public static void init() {
        BLOCKS.register();
        BLOCK_ITEMS.register();
    }
}
