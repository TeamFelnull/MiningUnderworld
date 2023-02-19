package dev.felnull.miningunderworld.dimension;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.*;

public class MUNoiseRouter {

    public static NoiseRouter create(BootstapContext<NoiseGeneratorSettings> context) {
        //DensityFunction:座標に対応する値を返す、密度のようなものを表す関数。値が正ならブロック、負なら空気。
        //https://misode.github.io/worldgen/density-function/
        var densities = context.lookup(Registries.DENSITY_FUNCTION);
        var noodle = getDensity(densities, "overworld/caves/noodle");//密度関数をリソースから取ってくる
        //NoiseRouterDataにいろいろある、privateだけどな！
        var a = NoiseRouterData.EROSION;
        var solid = DensityFunctions.constant(1);//常に密度１，全てブロック

        //NoiseParameters:ノイズ。DensityFunction作るのに使える。
        //https://misode.github.io/worldgen/noise/?version=1.19.3
        //DensityFunctionとSurfaceRule両方に使えるけど、それぞれどちらか一方に使うものとして作られている模様
        var noises = context.lookup(Registries.NOISE);
        var cave_cheese = DensityFunctions.noise(noises.getOrThrow(Noises.CAVE_CHEESE));//地形生成用のノイズ
        var nether_wart = DensityFunctions.noise(noises.getOrThrow(Noises.NETHER_WART));//地形色付け用のノイズ
        var cave = DensityFunctions
                .min(
                DensityFunctions.add(
                                noodle,
                                DensityFunctions.constant(-1)),
                solid);//より小さい値で上書き→空気追加

        //各引数の動作は謎だらけ
        //finalDensityがブロックか空気か決めるってだけ分かってる
        return new NoiseRouter(
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                noodle,//ブロックがある場所を決める
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero());
    }

    private static DensityFunction getDensity(HolderGetter<DensityFunction> densities, String resourceLocation) {
        return new DensityFunctions.HolderHolder(densities.getOrThrow(
                ResourceKey.create(Registries.DENSITY_FUNCTION, new ResourceLocation(resourceLocation))
        ));
    }
}
