package dev.felnull.miningunderworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class CollapsingBlock extends Block implements CollapseStarter {

    public float startCollapseRate;
    private final float collapseCoefficient;

    public CollapsingBlock(float startCollapseRate, float collapseCoefficient, BlockBehaviour.Properties properties) {
        super(properties);
        this.startCollapseRate = startCollapseRate;
        this.collapseCoefficient = collapseCoefficient;
    }

    @Override
    public float getStartCollapseRate() {
        return startCollapseRate;
    }

    @Override
    public float getCollapseCoefficient() {
        return collapseCoefficient;
    }

    @Override
    public void fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float f) {
        super.fallOn(level, blockState, blockPos, entity, f);
        CollapseStarter.fallOn(entity);
    }
}
