package dev.felnull.miningunderworld.block;

import dev.felnull.miningunderworld.dimension.generation.CrystalFeature;
import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;

//鉱石ごとに対応した色とドロップを持つクリスタル
public class CrystalBlock extends HalfTransparentBlock {

    public final ResourceLocation ORE_LOC;

    public CrystalBlock(ResourceLocation loc) {
        super(BlockBehaviour.Properties.of(Material.AMETHYST, MaterialColor.NONE)
                .strength(0.5F, 0F)
                .sound(SoundType.AMETHYST)
                .noOcclusion()
                .friction(1.145141919810F)//少し動いただけで加速しだす
                .isViewBlocking((a, b, c) -> false)
                .isValidSpawn((a, b, c, d) -> false)
                .lightLevel(a -> 15)
                .requiresCorrectToolForDrops()
                .randomTicks());
        ORE_LOC = loc;
    }

    @Override
    public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
        return 1.0F;//確かガラスにあった処理
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) {
        return true;//之もガラスにあった処理
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder builder) {

        ItemStack stack = builder.getOptionalParameter(LootContextParams.TOOL);
        if (stack != null && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) != 0)
            return super.getDrops(blockState, builder);//シルクタッチなら回収可

        var ore = getOre();//鉱石取得、なかったら空気になるみたい
        return ore.getDrops(ore.defaultBlockState(), builder);//対応する鉱石をこのコンテキストで壊したものを返す
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel level, BlockPos pos, RandomSource random) {
        var biome = level.getBiome(pos).value();
        if (random.nextFloat() < 0.01F * biome.getBaseTemperature() * biome.getDownfall())
            if (Direction.stream().allMatch(d -> level.getBlockState(pos.relative(d)).getBlock() == this)) {
                var origin = pos.offset(MUUtils.toI(MUUtils.randomBaseVector(random)));
                if (Direction.stream().allMatch(d -> isAirOrMe(level.getBlockState(origin.relative(d)).getBlock())))
                    CrystalFeature.addCrystal(level, origin, this.defaultBlockState(), false);
            }
    }

    private boolean isAirOrMe(Block block) {
        return block == this || block == Blocks.AIR;
    }

    public Block getOre() {
        return getOre(ORE_LOC);
    }

    public static Block getOre(ResourceLocation loc) {
        return BuiltInRegistries.BLOCK.get(loc);
    }

    @Override
    public String getDescriptionId() {
        return "block.miningunderworld.crystal";
    }

    @Override
    public MutableComponent getName() {
        return Component.translatable(getDescriptionId(), getOre().getName());
    }

    public class Item extends BlockItem {

        public Item() {
            super(CrystalBlock.this, new Item.Properties());
        }

        @Override
        public Component getName(ItemStack itemStack) {
            return CrystalBlock.this.getName();
        }
    }
}
