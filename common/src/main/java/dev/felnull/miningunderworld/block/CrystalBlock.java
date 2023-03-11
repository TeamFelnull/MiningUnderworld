package dev.felnull.miningunderworld.block;

import dev.felnull.miningunderworld.data.dynamic.OreHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;

//鉱石ごとに対応した色とドロップを持つクリスタル
public class CrystalBlock extends Block {

    public final int ORE_ID;//鉱石番号。ブロック生成時に鉱石取得するのが面倒だったので、先に十分量のブロックを生成して、中身は後で考える。
    public static final int MAX_ID = 255;//最大鉱石番号。256個の鉱石までなら正常に処理できることを意味する。

    public CrystalBlock(int i, Properties properties) {
        super(properties);
        ORE_ID = i;
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

    public Block getOre() {
        return getOre(ORE_ID);
    }

    public static Block getOre(int i) {
        return BuiltInRegistries.BLOCK.get(OreHolder.idToOre.get(i));
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

        public Item(Block block, Properties properties) {
            super(block, properties);
        }

        @Override
        public Component getName(ItemStack itemStack) {
            return CrystalBlock.this.getName();
        }

        public CrystalBlock getCrystalBlock(){
            return CrystalBlock.this;
        }
    }
}
