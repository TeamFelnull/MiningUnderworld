package dev.felnull.miningunderworld.block;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CrystalItem extends BlockItem {
    public int id;//TODO: NBTに保存

    public CrystalItem(Block block, Properties properties) {
        super(block, properties);
    }

    public CrystalItem setId(int id){
        this.id = id;
        return this;
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext blockPlaceContext) {
        return MUBlocks.CRYSTAL.get().defaultBlockState().setValue(CrystalBlock.ORE_ID, id);
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return Component.translatable(this.getDescriptionId(itemStack), CrystalBlock.getOre(id).getName());
    }

    public static Item withId(BlockState blockState){
        return withId(blockState.getValue(CrystalBlock.ORE_ID));
    }

    public static Item withId(int oreId){
        return ((CrystalItem) MUBlocks.CRYSTAL.get().asItem()).setId(oreId);
    }
}
