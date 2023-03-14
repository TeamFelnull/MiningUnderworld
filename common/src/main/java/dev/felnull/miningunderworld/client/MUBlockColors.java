package dev.felnull.miningunderworld.client;

import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.felnull.miningunderworld.block.CrystalBlock;
import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.miningunderworld.data.dynamic.OreHolder;
import dev.felnull.miningunderworld.data.dynamic.TextureHolder;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MUBlockColors {
    public static void init() {
        /*MUBlocks.CRYSTALS.forEach(rs ->
                ColorHandlerRegistry.registerBlockColors(new BlockColor() {
                    @Override
                    public int getColor(BlockState blockState, @Nullable BlockAndTintGetter blockAndTintGetter, @Nullable BlockPos blockPos, int tinindex) {
                        if (tinindex == 0)
                            if (rs.get() instanceof CrystalBlock crystal && crystal.ORE_ID < OreHolder.idToOre.size())
                                return TextureHolder.idToColor.get(crystal.ORE_ID);
                        return -1;
                    }
                }, rs.get()));*/
    }
}
