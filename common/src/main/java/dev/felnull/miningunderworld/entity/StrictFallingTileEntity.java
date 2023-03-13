package dev.felnull.miningunderworld.entity;

import dev.felnull.miningunderworld.mixin.FallingBlockEntityAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

//ブロックエンティティをサポートする落下ブロック
//WIP...
public class StrictFallingTileEntity extends FallingBlockEntity {
    public StrictFallingTileEntity(EntityType<? extends FallingBlockEntity> entityType, Level level) {
        super(entityType, level);
    }

    private StrictFallingTileEntity(Level level, double x, double y, double z, BlockState blockState, CompoundTag blockEntityData) {
        this(MUEntityTypes.STRICT_FALLING_BLOCK.get(), level);
        ((FallingBlockEntityAccessor) this).setBlockState(blockState);
        this.blocksBuilding = true;
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setStartPos(this.blockPosition());
    }

    /*public static StrictFallingTileEntity strictFall(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        CompoundTag blockEntityData = blockEntity.saveWithoutMetadata();

        var entity = new StrictFallingTileEntity(level, (double) blockPos.getX() + 0.5, blockPos.getY(), (double) blockPos.getZ() + 0.5,
                blockState.hasProperty(BlockStateProperties.WATERLOGGED) ? blockState.setValue(BlockStateProperties.WATERLOGGED, false) : blockState, blockEntityData);

        level.setBlock(blockPos, blockState.getFluidState().createLegacyBlock(), 3);
        level.addFreshEntity(entity);

        return entity;
    }*/
}
