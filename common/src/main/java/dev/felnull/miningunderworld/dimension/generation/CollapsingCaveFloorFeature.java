package dev.felnull.miningunderworld.dimension.generation;

import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.miningunderworld.dimension.MiningUnderworldDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CollapsingCaveFloorFeature extends Feature<NoneFeatureConfiguration> {

    public CollapsingCaveFloorFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        var level = context.level();
        var origin = context.origin();

        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++) {
                int floorThickness = 0;
                for (int y = MiningUnderworldDimension.minY; y < MiningUnderworldDimension.maxY; y++) {
                    var pos = level.getChunk(origin).getPos().getBlockAt(x, y, z);
                    if (!level.getBlockState(pos).canBeReplaced())//固体が続く限り床の厚さを増す
                        floorThickness++;
                    else if (!level.getBlockState(pos.relative(Direction.DOWN)).canBeReplaced()) {//固体じゃなくなって、それが固体→空気の変わり目なら
                        replaceFloor(level, pos.relative(Direction.DOWN), floorThickness);//張替
                        floorThickness = 0;//厚さ0に戻す
                    }
                }
            }

        return true;
    }

    static float MAX_THICKNESS = 64;

    public void replaceFloor(WorldGenLevel level, BlockPos pos, int floorThickness) {
        if (level.getBlockState(pos).getBlock() == Blocks.BEDROCK || floorThickness > MAX_THICKNESS)
            return;

        //結局崩れやすさ同じになるような配置
        var block = level.getRandom().nextFloat() > floorThickness / MAX_THICKNESS ? MUBlocks.UNSTABLE_STANCE : MUBlocks.COLLAPSING_STANCE;
        level.setBlock(pos, block.get().defaultBlockState(), 2);
    }
}
