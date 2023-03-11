package dev.felnull.miningunderworld.block;

import dev.architectury.core.block.ArchitecturyLiquidBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.fluid.MUFluids;
import dev.felnull.miningunderworld.item.MUCreativeModeTab;
import dev.felnull.miningunderworld.particles.MUParticleTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class MUBlocks {
    //固体ブロック
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MiningUnderworld.MODID, Registries.BLOCK);
    private static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(MiningUnderworld.MODID, Registries.ITEM);
    public static final RegistrySupplier<Block> TEST_BLOCK = register("test_block", () -> new Block(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT).strength(0.5F).sound(SoundType.GRAVEL)));
    public static final RegistrySupplier<Block> LOOT_POT = register("loot_pot", () -> new LootPotBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLUE).strength(0.5F).sound(SoundType.GLASS), false));
    public static final RegistrySupplier<Block> GOLDEN_LOOT_POT = register("golden_loot_pot", () -> new LootPotBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.GOLD).strength(0.5F).sound(SoundType.METAL), true), it -> new BlockItem(it, new Item.Properties().rarity(Rarity.RARE).arch$tab(MUCreativeModeTab.MOD_TAB)));
    public static final RegistrySupplier<Block> TAR_STAINS = register("tar_stains", () -> new TarStainsBlock(BlockBehaviour.Properties.of(Material.WATER, MaterialColor.COLOR_BLACK).friction(0.98F).noCollission().strength(0.5F).sound(SoundType.HONEY_BLOCK)));
    public static final RegistrySupplier<Block> SMALL_TAR_STAINS = register("small_tar_stains", () -> new TarStainsBlock(BlockBehaviour.Properties.of(Material.WATER, MaterialColor.COLOR_BLACK).friction(0.98F).noCollission().strength(0.5F).sound(SoundType.HONEY_BLOCK)));
    public static final RegistrySupplier<Block> SOAKED_TAR_STONE = register("soaked_tar_stone", () -> new LiquidSoakedBlock(MUFluids.TAR, MUParticleTypes.DRIPPING_TAR::get, BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistrySupplier<Block> SOAKED_TAR_DEEPSLATE = register("soaked_tar_deepslate", () -> new LiquidSoakedBlock(MUFluids.TAR, MUParticleTypes.DRIPPING_TAR::get, BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));
    public static final RegistrySupplier<Block> SOAKED_LAVA_STONE = register("soaked_lava_stone", () -> new LiquidSoakedBlock(() -> Fluids.LAVA, () -> ParticleTypes.DRIPPING_LAVA, BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistrySupplier<Block> SOAKED_LAVA_DEEPSLATE = register("soaked_lava_deepslate", () -> new LiquidSoakedBlock(() -> Fluids.LAVA, () -> ParticleTypes.DRIPPING_LAVA, BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));
    public static final RegistrySupplier<Block> LIKELY_COLLAPSING_BLOCK = register("likely_collapsing_block", () -> new CollapsingBlock(1 / 2F, 1, BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT).strength(0.5F).sound(SoundType.GRAVEL)));
    public static final RegistrySupplier<Block> MOST_LIKELY_COLLAPSING_BLOCK = register("most_likely_collapsing_block", () -> new CollapsingBlock(1F, 2, BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT).strength(0.5F).sound(SoundType.GRAVEL)));
    public static final RegistrySupplier<Block> MINING_TNT = register("mining_tnt", () -> new MiningTntBlock(BlockBehaviour.Properties.of(Material.EXPLOSIVE, MaterialColor.STONE).strength(0.1F).sound(SoundType.STONE)));
    //独自アイテムを持つブロック
    public static final RegistrySupplier<Block> CRYSTAL = registerCrystal("crystal", () -> new CrystalBlock(BlockBehaviour.Properties.of(Material.AMETHYST, MaterialColor.NONE).strength(1.5F).sound(SoundType.AMETHYST).noOcclusion().requiresCorrectToolForDrops()));
    //液体ブロック
    public static final RegistrySupplier<LiquidBlock> TEST_FLUID = registerBlockOnly("test_fluid", () -> new ArchitecturyLiquidBlock(MUFluids.TEST_FLUID, BlockBehaviour.Properties.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable()));
    public static final RegistrySupplier<LiquidBlock> TAR = registerBlockOnly("tar", () -> new TarLiquidBlock(MUFluids.TAR, BlockBehaviour.Properties.copy(Blocks.WATER).randomTicks().noCollission().strength(100.0F).noLootTable()));

    private static <B extends Block> RegistrySupplier<B> registerBlockOnly(String name, Supplier<B> block) {
        return BLOCKS.register(name, block);
    }

    private static RegistrySupplier<Block> register(String name, Supplier<Block> block) {
        return register(name, block, it -> new BlockItem(it, new Item.Properties().arch$tab(MUCreativeModeTab.MOD_TAB)));
    }

    private static RegistrySupplier<Block> registerCrystal(String name, Supplier<Block> block) {
        return register(name, block, it -> new CrystalItem(it, new Item.Properties().arch$tab(MUCreativeModeTab.MOD_TAB)));
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
