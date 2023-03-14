package dev.felnull.miningunderworld.block;

import dev.architectury.core.block.ArchitecturyLiquidBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.fluid.MUFluids;
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

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public interface MUBlocks {

    DeferredRegister<Block> BLOCKS = DeferredRegister.create(MiningUnderworld.MODID, Registries.BLOCK);
    DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(MiningUnderworld.MODID, Registries.ITEM);

    //タール
    RegistrySupplier<Block> TAR_STAINS = register("tar_stains", () -> new TarStainsBlock(BlockBehaviour.Properties.of(Material.WATER, MaterialColor.COLOR_BLACK).noCollission().strength(0.5F).sound(SoundType.GRAVEL)));
    RegistrySupplier<Block> SMALL_TAR_STAINS = register("small_tar_stains", () -> new TarStainsBlock(BlockBehaviour.Properties.of(Material.WATER, MaterialColor.COLOR_BLACK).noCollission().strength(0.5F).sound(SoundType.GRAVEL)));
    RegistrySupplier<Block> SOLID_TAR = register("solid_tar", () -> new SolidTarBlock(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT).strength(0.5F).sound(SoundType.GRAVEL)));
    RegistrySupplier<Block> SEMISOLID_TAR = register("semisolid_tar", () -> new SemisolidTarBlock(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT).strength(0.5F).sound(SoundType.GRAVEL)));
    RegistrySupplier<Block> CONDENSED_TAR = register("condensed_tar", () -> new Block(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT).strength(0.5F).sound(SoundType.GRAVEL)));

    //染み込んだ石
    RegistrySupplier<Block> SOAKED_TAR_STONE = register("soaked_tar_stone", () -> new LiquidSoakedBlock(MUFluids.TAR, MUParticleTypes.DRIPPING_TAR::get, BlockBehaviour.Properties.copy(Blocks.STONE)));
    RegistrySupplier<Block> SOAKED_TAR_DEEPSLATE = register("soaked_tar_deepslate", () -> new LiquidSoakedBlock(MUFluids.TAR, MUParticleTypes.DRIPPING_TAR::get, BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));
    RegistrySupplier<Block> SOAKED_LAVA_STONE = register("soaked_lava_stone", () -> new LiquidSoakedBlock(() -> Fluids.LAVA, () -> ParticleTypes.DRIPPING_LAVA, BlockBehaviour.Properties.copy(Blocks.STONE)));
    RegistrySupplier<Block> SOAKED_LAVA_DEEPSLATE = register("soaked_lava_deepslate", () -> new LiquidSoakedBlock(() -> Fluids.LAVA, () -> ParticleTypes.DRIPPING_LAVA, BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));

    //クリスタル
    List<RegistrySupplier<Block>> CRYSTALS = IntStream.rangeClosed(0, CrystalBlock.MAX_ID)
            .mapToObj(i -> register("crystal_" + i, () -> new CrystalBlock(i), crystal -> crystal.new Item(crystal)))
            .toList();//Streamのままでは再利用できない。Listにしておいて、使うたび再度streamを開く必要
    RegistrySupplier<Block> BLUE_SAND = register("blue_sand", () -> new CrystalSand(0.1F, 0xffa0a0ff, MaterialColor.COLOR_LIGHT_BLUE));
    RegistrySupplier<Block> WHITE_SAND = register("white_sand", () -> new CrystalSand(1F, 0xffe7d5ff, MaterialColor.COLOR_MAGENTA));

    //液体
    RegistrySupplier<LiquidBlock> TEST_FLUID = registerBlockOnly("test_fluid", () -> new ArchitecturyLiquidBlock(MUFluids.TEST_FLUID, BlockBehaviour.Properties.copy(Blocks.WATER).noCollission().strength(100.0F).noLootTable()));
    RegistrySupplier<LiquidBlock> TAR = registerBlockOnly("tar", () -> new TarLiquidBlock(MUFluids.TAR, BlockBehaviour.Properties.copy(Blocks.WATER).randomTicks().noCollission().strength(100.0F).noLootTable()));

    //その他
    RegistrySupplier<Block> TEST_BLOCK = register("test_block", () -> new Block(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT).strength(0.5F).sound(SoundType.GRAVEL)));
    RegistrySupplier<Block> LOOT_POT = register("loot_pot", () -> new LootPotBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLUE).strength(0.5F).sound(SoundType.GLASS), false));
    RegistrySupplier<Block> GOLDEN_LOOT_POT = register("golden_loot_pot", () -> new LootPotBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.GOLD).strength(0.5F).sound(SoundType.METAL), true), it -> new BlockItem(it, new Item.Properties().rarity(Rarity.RARE)));
    RegistrySupplier<Block> LIKELY_COLLAPSING_BLOCK = register("likely_collapsing_block", () -> new CollapsingBlock(1 / 2F, 1, BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT).strength(0.5F).sound(SoundType.GRAVEL)));
    RegistrySupplier<Block> MOST_LIKELY_COLLAPSING_BLOCK = register("most_likely_collapsing_block", () -> new CollapsingBlock(1F, 2, BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT).strength(0.5F).sound(SoundType.GRAVEL)));
    RegistrySupplier<Block> MINING_TNT = register("mining_tnt", () -> new MiningTntBlock(BlockBehaviour.Properties.of(Material.EXPLOSIVE, MaterialColor.STONE).strength(0.1F).sound(SoundType.STONE)));
    RegistrySupplier<Block> NAZO = register("nazo", () -> new HABlock(BlockBehaviour.Properties.of(Material.EXPLOSIVE, MaterialColor.STONE).strength(0.1F).sound(SoundType.STONE)));

    private static <B extends Block> RegistrySupplier<B> registerBlockOnly(String name, Supplier<B> block) {
        return BLOCKS.register(name, block);
    }

    private static RegistrySupplier<Block> register(String name, Supplier<Block> block) {
        return register(name, block, it -> new BlockItem(it, new Item.Properties()));
    }

    private static <B extends Block> RegistrySupplier<Block> register(String name, Supplier<B> block, Function<B, Item> blockItem) {
        var blockr = BLOCKS.register(name, block);
        BLOCK_ITEMS.register(name, () -> blockItem.apply(blockr.get()));
        return (RegistrySupplier<Block>) blockr;
    }

    static void init() {
        BLOCKS.register();
        BLOCK_ITEMS.register();
    }
}
