package dev.felnull.miningunderworld.block;

import com.google.common.collect.ImmutableList;
import dev.architectury.core.block.ArchitecturyLiquidBlock;
import dev.felnull.miningunderworld.fluid.MUFluidTags;
import dev.felnull.miningunderworld.fluid.MUFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.function.Supplier;

public class TarLiquidBlock extends ArchitecturyLiquidBlock {
    public TarLiquidBlock(Supplier<? extends FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return true;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.randomTick(blockState, serverLevel, blockPos, randomSource);
        var fluidState = blockState.getFluidState();

        if (randomSource.nextInt(5) >= 1)
            return;

        int range = Mth.clamp(5 - (fluidState.isSource() ? 3 : (int) ((float) fluidState.getValue(FlowingFluid.LEVEL) * 0.9f)), 1, 5);

        for (int x = 0; x < range * 2 + 1; x++) {
            for (int y = 0; y < range * 2 + 1; y++) {
                for (int z = 0; z < range * 2 + 1; z++) {
                    BlockPos pos = blockPos.offset(x - range, y - range, z - range);
                    if (blockPos.equals(pos))
                        continue;

                    dirty(serverLevel, blockPos, pos, randomSource, range);
                }
            }
        }
    }

    private void dirty(ServerLevel serverLevel, BlockPos origin, BlockPos blockPos, RandomSource randomSource, int range) {
        //  serverLevel.sendParticles(ParticleTypes.END_ROD, c.x(), c.y(), c.z(), 1, 0, 0, 0, 0);

        if (randomSource.nextInt(10) >= 1)
            return;

        var state = serverLevel.getBlockState(blockPos);

        boolean repl = (state.canBeReplaced(MUFluids.TAR.get()) && state.getFluidState().isEmpty() && !(state.getBlock() instanceof TarStainsBlock)) || state.isAir();

        if (!repl && !(state.getBlock() instanceof TarStainsBlock))
            return;

        var v = blockPos.getCenter().subtract(origin.getCenter());
        var nDir = Direction.getNearest(v.x, v.y, v.z);
        var ppos = blockPos.relative(nDir);

        RandomSource rnd = RandomSource.create(blockPos.asLong());
        double dis = Math.sqrt(origin.distSqr(blockPos));

        if (range - dis <= 0.5 && rnd.nextFloat() < 0.45f)
            return;

        var hit = serverLevel.isBlockInLine(new ClipBlockStateContext(origin.getCenter(), ppos.getCenter(), (it) -> !it.canBeReplaced(MUFluids.TAR.get()) && !it.isAir() && !(it.getBlock() instanceof TarStainsBlock) && !it.getFluidState().is(MUFluidTags.TAR)));

        if (hit.getType() == HitResult.Type.BLOCK)
            return;

        boolean near = dis / (double) range - rnd.nextFloat() * 0.35f <= 0.5f;

        List<Direction> preDirs = !(state.getBlock() instanceof TarStainsBlock) ? ImmutableList.of() : Direction.stream()
                .filter(it -> state.getValue(MultifaceBlock.getFaceProperty(it)))
                .toList();

        BlockState newState = null;

        if (near && state.is(MUBlocks.SMALL_TAR_STAINS.get()) && randomSource.nextInt(5) == 0) {
            newState = MUBlocks.TAR_STAINS.get().defaultBlockState();

            for (Direction dir : preDirs) {
                newState = newState.setValue(MultifaceBlock.getFaceProperty(dir), Boolean.TRUE);
            }

        } else {
            List<Direction> dirs = Direction.stream()
                    .filter(it -> !preDirs.contains(it))
                    .filter(it -> ((TarStainsBlock) MUBlocks.SMALL_TAR_STAINS.get()).isValidStateForPlacement(serverLevel, state, blockPos, it))
                    .toList();

            if (!dirs.isEmpty()) {
                var dir = dirs.get(randomSource.nextInt(dirs.size()));
                newState = (repl ? MUBlocks.SMALL_TAR_STAINS.get().defaultBlockState() : state).setValue(MultifaceBlock.getFaceProperty(dir), Boolean.TRUE);
            }
        }

        if (newState != null) {
            if (!state.isAir())
                serverLevel.destroyBlock(blockPos, true);
            
            serverLevel.setBlockAndUpdate(blockPos, newState);
        }
    }
}
