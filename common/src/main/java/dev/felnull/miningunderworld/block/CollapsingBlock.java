package dev.felnull.miningunderworld.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

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
}
