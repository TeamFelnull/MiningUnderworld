package dev.felnull.miningunderworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public class LiquidSoakedBlock extends Block {
    private final Supplier<FlowingFluid> soakedLiquid;

    public LiquidSoakedBlock(Supplier<FlowingFluid> soakedLiquid, Properties properties) {
        super(properties);
        this.soakedLiquid = soakedLiquid;
    }

    @Override
    public void destroy(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        super.destroy(levelAccessor, blockPos, blockState);

        if (!levelAccessor.isClientSide())
            levelAccessor.setBlock(blockPos, soakedLiquid.get().defaultFluidState().createLegacyBlock(), 3);
    }
}
