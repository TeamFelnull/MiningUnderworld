package dev.felnull.miningunderworld.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class SemisolidTarBlock extends FallingBlock {
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
    private static final VoxelShape[] SHAPE_BY_LAYER = {Shapes.empty(),
            Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)};

    public SemisolidTarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, 1));
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        var belowState = serverLevel.getBlockState(blockPos.below());
        if ((isFree(belowState) || (belowState.is(this) && belowState.getValue(LAYERS) < 8)) && blockPos.getY() >= serverLevel.getMinBuildHeight()) {
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(serverLevel, blockPos, blockState);
            this.falling(fallingBlockEntity);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE_BY_LAYER[blockState.getValue(LAYERS) - 1];
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        if (pathComputationType == PathComputationType.LAND)
            return blockState.getValue(LAYERS) < 5;

        return false;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.getValue(LAYERS) == 8 ? 0.2F : 1.0F;
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        if (blockPlaceContext.getItemInHand().is(this.asItem())) {
            int layer = blockState.getValue(LAYERS);
            return layer < 8 && blockPlaceContext.replacingClickedOnBlock() && blockPlaceContext.getClickedFace() == Direction.UP;
        }

        return false;
    }

    public static boolean onFallBlockEntity(FallingBlockEntity entity, Level level, BlockPos pos, BlockState fallingBlock, BlockState onBlock) {
        BlockState aboveState = level.getBlockState(pos.above());

        if (onBlock.getValue(LAYERS) >= 8 && (aboveState.is(MUBlocks.SEMISOLID_TAR.get()) && aboveState.getValue(LAYERS) == 1)) {
            pos = pos.above();
            onBlock = aboveState;
            aboveState = level.getBlockState(pos.above());
        }

        var rep = computeFallState(fallingBlock, onBlock);
        boolean ret = level.setBlock(pos, rep.getLeft(), 3);

        if (!rep.getRight().isAir() && aboveState.canBeReplaced())
            level.setBlock(pos.above(), rep.getRight(), 3);

        return ret;
    }

    private static Pair<BlockState, BlockState> computeFallState(BlockState fallingBlock, BlockState onBlock) {
        BlockState ret;
        BlockState retOn = Blocks.AIR.defaultBlockState();

        int layer = onBlock.getValue(LAYERS) + fallingBlock.getValue(LAYERS);

        ret = MUBlocks.SEMISOLID_TAR.get().defaultBlockState().setValue(LAYERS, Math.min(8, layer));

        if (layer > 8) {
            retOn = MUBlocks.SEMISOLID_TAR.get().defaultBlockState().setValue(LAYERS, Math.min(8, layer - 8));
        }

        return Pair.of(ret, retOn);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos());
        if (blockState.is(this)) {
            int i = blockState.getValue(LAYERS);
            return blockState.setValue(LAYERS, Math.min(8, i + 1));
        } else {
            return super.getStateForPlacement(blockPlaceContext);
        }
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);

        if (!level.isClientSide) {
            int layer = blockState.getValue(LAYERS);
            Stream<Direction> dirs = layer < 8 ? Direction.stream().filter(dir -> dir == Direction.DOWN) : Direction.stream();

            BlockState state = ((TarStainsBlock) MUBlocks.TAR_STAINS.get()).getAllAttachedFace(level, blockPos, dirs);

            level.setBlock(blockPos, state, 2);
            level.gameEvent(GameEvent.BLOCK_DESTROY, blockPos, GameEvent.Context.of(blockState));
            level.levelEvent(2001, blockPos, Block.getId(blockState));
        }
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        super.stepOn(level, blockPos, blockState, entity);
        if (level.getRandom().nextInt(80) == 0)
            slide(level, blockState, blockPos, 1);
    }

    @Override
    public void fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float f) {
        entity.causeFallDamage(f, 0.1F, entity.damageSources().fall());
        slide(level, blockState, blockPos, Mth.floor(f));
    }

    private void slide(Level level, BlockState state, BlockPos blockPos, int count) {
        if (count == 0) return;
        if (level.isClientSide) return;
        count = Math.min(8, count);

        int layer = state.getValue(LAYERS);
        int disLayer = Math.max(level.getBlockState(blockPos.below()).is(this) ? 0 : 1, layer - count);
        int discount = layer - disLayer;

        List<Direction> dirs = Util.toShuffledList(Direction.stream()
                .filter(dir -> dir.getAxis().isHorizontal())
                .filter(dir -> canBeSlideReplaced(state, level.getBlockState(blockPos.relative(dir)))), level.random);

        if (dirs.isEmpty()) return;

        for (int i = 0; i < discount; i++) {
            if (dirs.size() <= i) break;

            Direction dir = dirs.get(i);
            BlockPos dirPos = blockPos.relative(dir);
            BlockState dirState = level.getBlockState(dirPos);

            if (!dirState.isAir() && !dirState.is(this))
                level.destroyBlock(blockPos, true);

            BlockState sideState = this.defaultBlockState();

            if (dirState.is(this))
                sideState = sideState.setValue(LAYERS, Math.min(dirState.getValue(LAYERS) + 1, 8));

            level.setBlockAndUpdate(dirPos, sideState);
        }


        BlockState newState = disLayer == 0 ? Blocks.AIR.defaultBlockState() : state.setValue(LAYERS, disLayer);
        level.setBlockAndUpdate(blockPos, newState);

        level.playSound(null, blockPos, SoundEvents.ROOTED_DIRT_BREAK, SoundSource.BLOCKS, 1f, level.getRandom().nextFloat() * 0.2F + 0.9F);
    }

    private boolean canBeSlideReplaced(BlockState blockState, BlockState targetState) {
        if (targetState.is(this)) {
            int layer = blockState.getValue(LAYERS);
            int targetLayer = targetState.getValue(LAYERS);
            return layer > targetLayer + 1;
        }

        return targetState.isAir() || targetState.canBeReplaced();
    }
}
