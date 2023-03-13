package dev.felnull.miningunderworld.block;

import dev.felnull.miningunderworld.particles.MUParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class TarStainsBlock extends MultifaceBlock {
    private final MultifaceSpreader spreader = new MultifaceSpreader(this);

    public TarStainsBlock(Properties properties) {
        super(properties);
    }


    @Override
    public MultifaceSpreader getSpreader() {
        return this.spreader;
    }


    public BlockState getAllAttachedFace(BlockGetter blockGetter, BlockPos blockPos, Stream<Direction> directions) {
        var state = blockGetter.getBlockState(blockPos);
        BlockState[] ret = {defaultBlockState()};

        directions.filter(dir -> isValidStateForPlacement(blockGetter, state, blockPos, dir))
                .forEach(dir -> ret[0] = ret[0].setValue(MultifaceBlock.getFaceProperty(dir), Boolean.TRUE));

        return ret[0] != defaultBlockState() ? ret[0] : Blocks.AIR.defaultBlockState();
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        super.animateTick(blockState, level, blockPos, randomSource);
        if (this == MUBlocks.SMALL_TAR_STAINS.get()) return;


        for (Direction dir : Direction.values()) {
            animateFace(dir, blockState, level, blockPos, randomSource);
        }
    }

    private void animateFace(Direction direction, BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (direction == Direction.DOWN) return;
        if (!blockState.getValue(getFaceProperty(direction))) return;
        if (randomSource.nextInt(Direction.values().length) != 0) return;

        double x = blockPos.getX();
        double y = blockPos.getY();
        double z = blockPos.getZ();

        double r1 = randomSource.nextDouble() * 0.8;
        double r2 = randomSource.nextDouble() * 0.8;

        if (direction.getAxis() == Direction.Axis.X) {
            double step = direction.getStepX();

            x += Math.max(step, 0) - 0.05 * step;
            y += r1 + 0.1;
            z += r2 + 0.1;
        } else if (direction.getAxis() == Direction.Axis.Y) {
            double step = direction.getStepY();

            x += r1 + 0.1;
            y += Math.max(step, 0) - 0.05 * step;
            z += r2 + 0.1;
        } else if (direction.getAxis() == Direction.Axis.Z) {
            double step = direction.getStepZ();

            x += r1 + 0.1;
            y += r2 + 0.1;
            z += Math.max(step, 0) - 0.05 * step;
        }

        level.addParticle(MUParticleTypes.DRIPPING_TAR.get(), x, y, z, 0.0, 0.0, 0.0);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);

        if (this == MUBlocks.TAR_STAINS.get() && !level.isClientSide) {
            BlockState smallStains = MUBlocks.SMALL_TAR_STAINS.get().defaultBlockState();

            for (Direction direction : Direction.values()) {
                var pro = getFaceProperty(direction);
                smallStains = smallStains.setValue(pro, blockState.getValue(pro));
            }

            level.setBlock(blockPos, smallStains, 2);
            level.gameEvent(GameEvent.BLOCK_DESTROY, blockPos, GameEvent.Context.of(blockState));
            level.levelEvent(2001, blockPos, Block.getId(blockState));
        }
    }
}
