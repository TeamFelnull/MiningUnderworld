package dev.felnull.miningunderworld.block;

import dev.felnull.miningunderworld.item.MUItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;

public class CrystalClayBlock extends Block {
    public CrystalClayBlock() {
        super(BlockBehaviour.Properties.of(Material.CLAY).strength(0.6F).sound(SoundType.GRAVEL));
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder builder) {
        ItemStack stack = builder.getOptionalParameter(LootContextParams.TOOL);
        if (stack != null && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) != 0)
            return super.getDrops(blockState, builder);

        return List.of(new ItemStack(MUItems.BLUE_CLAY_BALL.get(), 4));
    }
}
