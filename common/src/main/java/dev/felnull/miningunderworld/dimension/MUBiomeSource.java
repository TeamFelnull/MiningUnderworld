package dev.felnull.miningunderworld.dimension;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.*;

public class MUBiomeSource extends BiomeSource {
    public static final Codec<MUBiomeSource> MU_CODEC = RecordCodecBuilder.create(instance ->
         instance.group(RegistryOps.retrieveElement(Biomes.THE_END), RegistryOps.retrieveElement(Biomes.NETHER_WASTES))
                 .apply(instance, instance.stable(MUBiomeSource::new))
    );
    private final Holder<Biome> end;
    private final Holder<Biome> nether_wastes;

    public static MUBiomeSource create(HolderGetter<Biome> biomes) {
        return new MUBiomeSource(biomes.getOrThrow(Biomes.THE_END), biomes.getOrThrow(Biomes.NETHER_WASTES));
    }

    private MUBiomeSource(Holder<Biome> arg, Holder<Biome> arg2) {
        super(ImmutableList.of(arg, arg2));
        this.end = arg;
        this.nether_wastes = arg2;
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return MU_CODEC;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int i, int j, int k, Climate.Sampler sampler) {
        var x = QuartPos.toBlock(i);
        var z = QuartPos.toBlock(k);
        if (x*x + z*z <= 64*64)//半径64ブロックの円内では
            return end;//エンド
        else
            return nether_wastes;//それ以外はネザー
    }
}
