package dev.felnull.miningunderworld.dimension.generation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.phys.Vec3;

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

        var theta = level.getRandom().nextFloat() * Math.PI;
        var phi = level.getRandom().nextFloat() * 2 * Math.PI;
        var length = level.getRandom().nextInt(8, 16);
        var randomBaseVector = new Vec3(Math.sin(theta) * Math.cos(phi), Math.sin(theta) * Math.sin(phi) , Math.cos(theta));

        var line = new ArrayList<BlockPos>();
        IntStream.rangeClosed(0, length).forEach(i -> line.add(origin.offset(toI(randomBaseVector.scale(i)))));

        line.forEach(pos -> {
            level.setBlock(pos, config.crystal, 3);
            Direction.stream().forEach(d -> level.setBlock(pos.relative(d), config.crystal, 3));
        });

        return true;
    }

    public static Vec3i toI(Vec3 v){
        return new Vec3i(v.x, v.y, v.z);
    }

    public record Config(BlockState crystal) implements FeatureConfiguration {
        public static final Codec<CrystalFeature.Config> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(BlockState.CODEC.fieldOf("crystal").forGetter(c -> c.crystal))
                        .apply(instance, CrystalFeature.Config::new));
    }
}
