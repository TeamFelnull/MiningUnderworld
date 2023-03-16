package dev.felnull.miningunderworld.block;

import dev.felnull.miningunderworld.data.dynamic.OreHolder;
import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;

public class CrystalSand extends SandBlock {

    public final float dropProb;

    public CrystalSand(float dropProb, int dustColor, MaterialColor color) {
        super(dustColor, BlockBehaviour.Properties.of(Material.SAND, color).strength(0.5F).sound(SoundType.SAND));
        this.dropProb = dropProb;
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder builder) {
        ItemStack stack = builder.getOptionalParameter(LootContextParams.TOOL);
        if (stack != null && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) != 0)
            return super.getDrops(blockState, builder);

        int fortune = stack != null ? EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, stack) : 0;
        if (builder.getLevel().random.nextFloat() < dropProb * (fortune + 1)) {
            var ore = getRandomOre(builder.getLevel().random);
            return ore.getDrops(ore.defaultBlockState(), builder);
        }

        return super.getDrops(blockState, builder);
    }

    public static Block getRandomOre(RandomSource rand) {
        var oreLoc = MUUtils.getRandomlyFrom(OreHolder.oreLocs, rand);
        return CrystalBlock.getOre(oreLoc);
    }
}
