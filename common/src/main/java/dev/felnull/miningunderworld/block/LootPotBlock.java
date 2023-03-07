package dev.felnull.miningunderworld.block;

import dev.felnull.otyacraftengine.util.OEVoxelShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LootPotBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    //当たり判定
    private static final VoxelShape TOP = OEVoxelShapeUtils.addBoxY0(3, 14, 3, 10, 2, 10);
    private static final VoxelShape MIDDLE = OEVoxelShapeUtils.addBoxY0(4, 12, 4, 8, 2, 8);
    private static final VoxelShape BOTTOM = OEVoxelShapeUtils.addBoxY0(2, 0, 2, 12, 12, 12);
    private static final VoxelShape ALL = Shapes.or(TOP, MIDDLE, BOTTOM);

    private final boolean golden;

    public LootPotBlock(Properties properties, boolean golden) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
        this.golden = golden;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return ALL;
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder builder) {

        ItemStack stack = builder.getOptionalParameter(LootContextParams.TOOL);
        if (stack != null && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) != 0)
            return super.getDrops(blockState, builder);

        LootContext lootContext = builder.withParameter(LootContextParams.BLOCK_STATE, blockState).create(LootContextParamSets.BLOCK);
        ServerLevel serverLevel = lootContext.getLevel();
        //仮置きルートテーブル
        LootTable lootTable = serverLevel.getServer().getLootTables().get(golden ? BuiltInLootTables.END_CITY_TREASURE : BuiltInLootTables.SIMPLE_DUNGEON);

        return lootTable.getRandomItems(lootContext);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return !blockState.getValue(WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (blockState.getValue(WATERLOGGED))
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));

        return direction == Direction.DOWN && !this.canSurvive(blockState, levelAccessor, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        return super.getStateForPlacement(blockPlaceContext).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return canSupportCenter(levelReader, blockPos.below(), Direction.UP);
    }

    @Override
    public void spawnAfterBreak(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, boolean bl) {
        super.spawnAfterBreak(blockState, serverLevel, blockPos, itemStack, bl);

        if (serverLevel.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) == 0)
            this.spawnMob(serverLevel, blockPos);
    }

    private void spawnMob(ServerLevel serverLevel, BlockPos blockPos) {
        EntityType<? extends Mob> type = null;

        if (golden) {
            if (serverLevel.getRandom().nextInt(100) == 0)
                type = EntityType.ALLAY;
        } else {
            if (serverLevel.getRandom().nextInt(15) == 0)
                type = serverLevel.getRandom().nextInt(15) == 0 ? EntityType.ENDERMITE : EntityType.SILVERFISH;
        }

        if (type == null)
            return;

        Mob mob = type.create(serverLevel);
        if (mob != null) {
            mob.moveTo((double) blockPos.getX() + 0.5, blockPos.getY(), (double) blockPos.getZ() + 0.5, 0.0F, 0.0F);
            serverLevel.addFreshEntity(mob);
            mob.spawnAnim();
        }
    }
}
