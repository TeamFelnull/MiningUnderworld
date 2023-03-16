package dev.felnull.miningunderworld.dimension.generation;

import dev.felnull.miningunderworld.MixinTemp;
import dev.felnull.miningunderworld.block.CrystalBlock;
import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CrystalFeature extends Feature<NoneFeatureConfiguration> {

    public CrystalFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        var level = context.level();
        var origin = context.origin();

        //ランダムな方向と大きさを持つBlockPosの直線を生成
        //far chunkとかで無理ならまた生成、実際に置けるまで繰り返す
        List<BlockPos> line;
        Vec3 baseVector;
        int length;
        MixinTemp.testEnsuring.set(true);
        do {
            line = new ArrayList<>();
            baseVector = MUUtils.randomBaseVector(level.getRandom());
            length = level.getRandom().nextInt(4, 32);
            for (int i = 0; i <= length; i++)
                line.add(origin.offset(MUUtils.toI(baseVector.scale(i))));
        } while (!level.ensureCanWrite(origin.offset(MUUtils.toI(baseVector.scale(length + 6)))));//32/16 * 3 = 6先を見れば大体OK（これでエラー無い）
        MixinTemp.testEnsuring.set(false);

        //表面から生えるようにしたい
        if (isTouchingBlock(level, line)) {
            //何を置くかはこの時点で決める（データパック生成時には知り得ない情報故）
            var crystal = MUUtils.getRandomlyFrom(MUBlocks.CRYSTALS.stream().map(Supplier::get).toList(), level.getRandom()).defaultBlockState();
            line.forEach(pos -> addCrystal(level, pos, crystal, true));
            return true;
        }

        return false;
    }

    public static void addCrystal(ServerLevelAccessor level, BlockPos pos, BlockState block, boolean genWhiteSand) {
        setBlockExceptBedrockLayer(level, pos, block);
        Direction.stream().map(pos::relative).forEach(nPos -> {
            setBlockExceptBedrockLayer(level, nPos, block);
            if (genWhiteSand)
                Direction.stream().map(nPos::relative).forEach(nnPos -> {
                    if (!(level.isEmptyBlock(nnPos) || level.getBlockState(nnPos).getBlock() instanceof CrystalBlock)) {
                        setBlockExceptBedrockLayer(level, nnPos, MUBlocks.WHITE_SAND.get().defaultBlockState());
                        Direction.stream().map(nnPos::relative).forEach(nnnPos -> {
                            if (!(level.isEmptyBlock(nnnPos) || level.getBlockState(nnnPos).getBlock() instanceof CrystalBlock))
                                setBlockExceptBedrockLayer(level, nnnPos, MUBlocks.WHITE_SAND.get().defaultBlockState());
                        });
                    }
                });
        });
    }

    public static void setBlockExceptBedrockLayer(ServerLevelAccessor level, BlockPos pos, BlockState block) {
        if (level.getBlockState(pos).getBlock() != Blocks.BEDROCK)
            level.setBlock(pos, block, 2);
    }

    public static boolean isTouchingBlock(WorldGenLevel level, List<BlockPos> line) {
        var start = level.isEmptyBlock(line.get(0));
        var toEnd = line.subList(1, line.size() - 1).stream().allMatch(level::isEmptyBlock);
        var end = level.isEmptyBlock(line.get(line.size() - 1));
        var toStart = line.subList(0, line.size() - 2).stream().allMatch(level::isEmptyBlock);
        return (!start && toEnd) || (!end && toStart);
    }
}
