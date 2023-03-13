package dev.felnull.miningunderworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class LiquidSoakedBlock extends Block {
    private final Supplier<FlowingFluid> soakedLiquid;
    private final Supplier<ParticleOptions> particleOptions;

    public LiquidSoakedBlock(Supplier<FlowingFluid> soakedLiquid, Supplier<ParticleOptions> particleOptions, Properties properties) {
        super(properties);
        this.soakedLiquid = soakedLiquid;
        this.particleOptions = particleOptions;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);

        if (!level.isClientSide()) {
            level.setBlock(blockPos, soakedLiquid.get().defaultFluidState().createLegacyBlock(), 2);
            level.gameEvent(GameEvent.BLOCK_DESTROY, blockPos, GameEvent.Context.of(blockState));
            level.levelEvent(2001, blockPos, Block.getId(blockState));
        }
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        for (Direction dir : Direction.values()) {
            animateFace(dir, blockState, level, blockPos, randomSource);
        }
    }

    private void animateFace(Direction direction, BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (direction == Direction.UP) return;

        BlockPos dirPos = blockPos.relative(direction);
        BlockState dirState = level.getBlockState(dirPos);

        if (blockState.canOcclude() && dirState.isFaceSturdy(level, dirPos, direction.getOpposite())) return;

        double x = blockPos.getX();
        double y = blockPos.getY();
        double z = blockPos.getZ();

        double r1 = randomSource.nextDouble() * 0.8;
        double r2 = randomSource.nextDouble() * 0.8;

        if (direction.getAxis() == Direction.Axis.X) {
            double step = direction.getStepX();

            x += Math.max(step, 0) + 0.05 * step;
            y += r1 + 0.1;
            z += r2 + 0.1;
        } else if (direction.getAxis() == Direction.Axis.Y) {
            double step = direction.getStepY();

            x += r1 + 0.1;
            y += Math.max(step, 0) + 0.05 * step;
            z += r2 + 0.1;
        } else if (direction.getAxis() == Direction.Axis.Z) {
            double step = direction.getStepZ();

            x += r1 + 0.1;
            y += r2 + 0.1;
            z += Math.max(step, 0) + 0.05 * step;
        }

        level.addParticle(particleOptions.get(), x, y, z, 0.0, 0.0, 0.0);
    }
}
