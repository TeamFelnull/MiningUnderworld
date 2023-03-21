package dev.felnull.miningunderworld.world.dimension;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.miningunderworld.MiningUnderworld;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;

import java.util.stream.Stream;

public class MUBiomeSource extends BiomeSource {
    public static final Codec<MUBiomeSource> MU_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(RegistryOps.retrieveGetter(Registries.BIOME))
                    .apply(instance, instance.stable(MUBiomeSource::new))
    );
    private final HolderGetter<Biome> biomes;

    public static MUBiomeSource create(HolderGetter<Biome> biomes) {
        return new MUBiomeSource(biomes);
    }

    private MUBiomeSource(HolderGetter<Biome> biomes) {
        super();
        this.biomes = biomes;
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return MU_CODEC;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return MUBiomes.MU_BIOMES.keySet().stream().map(biomes::getOrThrow);
    }

    @Override
    public Holder<Biome> getNoiseBiome(int xQpos, int yQpos, int zQpos, Climate.Sampler sampler) {
        ImmutableList.Builder<Pair<Climate.ParameterPoint, Holder<Biome>>> builder = ImmutableList.builder();
        MUBiomes.MU_BIOMES.keySet().stream()
                .map(biomes::getOrThrow)
                .forEach(biome -> builder.add(Pair.of(biomeToClimate(biome.value()), biome)));
        return new Climate.ParameterList<>(builder.build()).findValue(sampler.sample(xQpos, yQpos, zQpos));
    }

    public static Climate.ParameterPoint biomeToClimate(Biome biome) {
        //気候のなだらかな変化に沿ってバイオームが形成されるように、バイオーム形成に関係がありそうなものを入れるとそれに合わせてなだらかに生成してくれる
        //ほんとは湿度とか大陸か島かとか各引数に特定の意味があるけどbiomeインスタンスから取得できる値じゃないから知らん
        //バニラはバイオームごとにいちいち書いてる
        return Climate.parameters(
                biome.getBaseTemperature(),//温度
                0,//湿度
                0,//大陸度合い
                0,//ero
                0,//深さ
                0,//変さ（？）
                0);//オフセット（何の？）
    }

    public Holder<Biome> test(int x, int z) {
        if (x * x + z * z <= 64 * 64)//半径64ブロックの円内では
            return biomes.getOrThrow(Biomes.THE_END);//エンド
        else
            return biomes.getOrThrow(Biomes.NETHER_WASTES);//それ以外はネザー
    }

    public static final DeferredRegister<Codec<? extends BiomeSource>> BIOME_SOURCES = DeferredRegister.create(MiningUnderworld.MODID, Registries.BIOME_SOURCE);
    public static final RegistrySupplier<Codec<? extends BiomeSource>> MU_BIOME = BIOME_SOURCES.register("mu_biome", () -> MUBiomeSource.MU_CODEC);

    public static void init() {
        BIOME_SOURCES.register();
    }
}
