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
        Direction direction = Direction.getRandom(randomSource);
        if (direction != Direction.UP) {
            BlockPos blockPos2 = blockPos.relative(direction);
            BlockState blockState2 = level.getBlockState(blockPos2);
            if (!blockState.canOcclude() || !blockState2.isFaceSturdy(level, blockPos2, direction.getOpposite())) {
                double d = blockPos.getX();
                double e = blockPos.getY();
                double f = blockPos.getZ();
                if (direction == Direction.DOWN) {
                    e -= 0.05;
                    d += randomSource.nextDouble();
                    f += randomSource.nextDouble();
                } else {
                    e += randomSource.nextDouble() * 0.8;
                    if (direction.getAxis() == Direction.Axis.X) {
                        f += randomSource.nextDouble();
                        if (direction == Direction.EAST) {
                            ++d;
                        } else {
                            d += 0.05;
                        }
                    } else {
                        d += randomSource.nextDouble();
                        if (direction == Direction.SOUTH) {
                            ++f;
                        } else {
                            f += 0.05;
                        }
                    }
                }

                level.addParticle(particleOptions.get(), d, e, f, 0.0, 0.0, 0.0);
            }
        }
    }
}
