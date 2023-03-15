package dev.felnull.miningunderworld.client;

import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.felnull.miningunderworld.block.MUBlocks;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MUBlockColors {
    public static void init() {
       /* ColorHandlerRegistry.registerBlockColors(new BlockColor() {
            @Override
            public int getColor(BlockState blockState, @Nullable BlockAndTintGetter blockAndTintGetter, @Nullable BlockPos blockPos, int tinindex) {
                if (tinindex == 0)
                    return 0x6995f4;
                return -1;
            }
        }, MUBlocks.NAZO);*/
    }
}
