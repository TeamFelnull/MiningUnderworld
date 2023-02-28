package dev.felnull.miningunderworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;

public class TarStainsBlock extends MultifaceBlock {
    private final MultifaceSpreader spreader = new MultifaceSpreader(this);

    public TarStainsBlock(Properties properties) {
        super(properties);
    }


    @Override
    public MultifaceSpreader getSpreader() {
        return this.spreader;
    }


    public BlockState getAllAttachedFace(BlockGetter blockGetter, BlockPos blockPos) {
        var state = blockGetter.getBlockState(blockPos);
        var ret = defaultBlockState();

        for (Direction direction : Direction.values()) {
            if (isValidStateForPlacement(blockGetter, state, blockPos, direction))
                ret = ret.setValue(MultifaceBlock.getFaceProperty(direction), Boolean.TRUE);
        }

        return ret != defaultBlockState() ? ret : Blocks.AIR.defaultBlockState();
    }

}
