package dev.felnull.miningunderworld.block;

import dev.felnull.miningunderworld.data.dynamic.OreHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;

//鉱石ごとに対応した色とドロップを持つクリスタル
public class CrystalBlock extends Block {

    //鉱石番号。ただのBlockはblockstatesでしか見た目を変えられず、それはコードでしか指定できない。だから既存鉱石数を十分にカバーできる数のblockstatesを持つ必要。
    public static final int MAX_ID = 255;//最大鉱石番号。256個の鉱石までなら正常に処理できることを意味する。
    public static final IntegerProperty ORE_ID = IntegerProperty.create("ore_id", 0, MAX_ID);//鉱石番号、0～255まで、256種のblockstatesを追加。

    public CrystalBlock(Properties properties) {
        super(properties);
        //registerDefaultState(stateDefinition.any().setValue(ORE_ID, 0));//デフォ値なくても大丈夫ぽい？
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ORE_ID);//ここで実際にblockstates追加
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
            return List.of(new ItemStack(CrystalItem.withId(blockState)));//シルクタッチなら回収可

        var ore = getOre(blockState);//oreなかったら空気になるみたい
        return ore.getDrops(ore.defaultBlockState(), builder);//そのブロックをこのコンテキストで壊したものを返す
    }

    public static Block getOre(BlockState blockState) {
        return BuiltInRegistries.BLOCK.get(OreHolder.idToOre.get(blockState.getValue(ORE_ID)));
    }

    public static Block getOre(int i) {
        return BuiltInRegistries.BLOCK.get(OreHolder.idToOre.get(i));
    }

    @Override
    public MutableComponent getName() {
        return Component.translatable(this.getDescriptionId(), getOre(defaultBlockState()).getName());//TODO blockstate毎の名前
    }
}
