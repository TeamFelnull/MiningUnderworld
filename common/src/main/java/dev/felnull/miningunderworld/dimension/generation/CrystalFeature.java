package dev.felnull.miningunderworld.dimension.generation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.felnull.miningunderworld.block.CrystalBlock;
import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.miningunderworld.data.dynamic.OreHolder;
import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class CrystalFeature extends Feature<CrystalFeature.Config> {

    public CrystalFeature() {
        super(CrystalFeature.Config.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<CrystalFeature.Config> context) {
        var level = context.level();
        var origin = context.origin();
        var config = context.config();

        if (config.crystal.getBlock() instanceof CrystalBlock crystal && crystal.ORE_ID < OreHolder.idToOre.size()) {
            var baseVector = MUUtils.randomBaseVector(level.getRandom());
            var length = level.getRandom().nextInt(4, 16);

            var line = new ArrayList<BlockPos>();
            IntStream.rangeClosed(0, length).forEach(i ->
                    line.add(origin.offset(MUUtils.toI(baseVector.scale(i)))));

            if (isTouchingWall(level, line)) {
                line.forEach(pos -> addCrystal(level, pos, config.crystal, true));
                return true;
            }
        }

        return false;
    }

    public static void addCrystal(ServerLevelAccessor level, BlockPos pos, BlockState block, boolean replaceRootBlock) {
        level.setBlock(pos, block, 3);
        Direction.stream().map(pos::relative).forEach(nPos -> {
            level.setBlock(nPos, block, 3);
            if (replaceRootBlock)
                Direction.stream().map(nPos::relative).forEach(nnPos -> {
                    if (!(level.isEmptyBlock(nnPos) || level.getBlockState(nnPos).getBlock() instanceof CrystalBlock)) {
                        level.setBlock(nnPos, MUBlocks.WHITE_SAND.get().defaultBlockState(), 0);
                        Direction.stream().map(nnPos::relative).forEach(nnnPos -> {
                            if (!(level.isEmptyBlock(nnnPos) || level.getBlockState(nnnPos).getBlock() instanceof CrystalBlock))
                                level.setBlock(nnnPos, MUBlocks.WHITE_SAND.get().defaultBlockState(), 0);
                        });
                    }
                });
        });
    }

    public static boolean isTouchingWall(WorldGenLevel level, ArrayList<BlockPos> line) {
        var start = level.isEmptyBlock(line.get(0));
        var toEnd = line.subList(1, line.size() - 1).stream().allMatch(level::isEmptyBlock);
        var end = level.isEmptyBlock(line.get(line.size() - 1));
        var toStart = line.subList(0, line.size() - 2).stream().allMatch(level::isEmptyBlock);
        return (!start && toEnd) || (!end && toStart);
    }

    public record Config(BlockState crystal) implements FeatureConfiguration {
        public static final Codec<CrystalFeature.Config> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(BlockState.CODEC.fieldOf("crystal").forGetter(c -> c.crystal))
                        .apply(instance, CrystalFeature.Config::new));
    }
}
