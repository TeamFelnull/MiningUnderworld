package dev.felnull.miningunderworld.world.dimension.generation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.felnull.miningunderworld.block.MUBlocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.stream.IntStream;

public class TestFeature extends Feature<TestFeature.Config> {

    public TestFeature() {
        super(Config.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        var level = context.level();
        var pos = context.origin();
        var config = context.config();

        IntStream.rangeClosed(1, config.length).forEach(i ->
                level.setBlock(pos.above(i), MUBlocks.TEST_BLOCK.get().defaultBlockState(), 3));

        return true;
    }

    public record Config(int length) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(Codec.intRange(1, 32).fieldOf("length").forGetter((c) -> c.length))
                        .apply(instance, Config::new));
    }
}

