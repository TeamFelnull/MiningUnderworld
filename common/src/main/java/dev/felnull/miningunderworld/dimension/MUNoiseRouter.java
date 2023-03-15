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
        var solid = DensityFunctions.constant(1);//常に密度１，全てブロック
        var noodle = getDensity(densities, "overworld/caves/noodle");//密度関数をリソースから取ってくる
        //NoiseRouterDataにいろいろある、なぜか一部privateだから上はString書いてるけど下みたいにかけるのは書く
        var erosion = getDensity(densities, NoiseRouterData.EROSION);

        //NoiseParameters:ノイズ。DensityFunction作るのに使える。
        //https://misode.github.io/worldgen/noise/?version=1.19.3
        var noises = context.lookup(Registries.NOISE);
        //DensityFunctionとSurfaceRule両方に使えるけど、それぞれどちらか一方に使うものとして作られている模様
        var cave_cheese = DensityFunctions.noise(noises.getOrThrow(Noises.CAVE_CHEESE));//地形生成用のノイズ
        var nether_wart = DensityFunctions.noise(noises.getOrThrow(Noises.NETHER_WART));//地形色付け用のノイズ
        var cave = DensityFunctions
                .min(//より小さい値で上書き→空洞合体！
                        DensityFunctions.add(//ヌードルの空気部分増やす
                                noodle,
                                DensityFunctions.constant(-.2)),
                        DensityFunctions.add(
                                cave_cheese,
                                DensityFunctions.constant(.3)));//チーズの空気部分減らす
        var bedrockLayers = DensityFunctions.max(
                DensityFunctions.add(getDensity(densities, "overworld/depth"), DensityFunctions.constant(-.7)),
                DensityFunctions.add(getDensity(densities, "y"), DensityFunctions.constant(-315)));
        cave = DensityFunctions.max(cave, bedrockLayers);//岩盤層を追加

        //各引数の動作は謎だらけ
        return new NoiseRouter(
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.shiftedNoise2d(
                        getDensity(densities, "shift_x"),
                        getDensity(densities, "shift_z"),
                        0.25D,
                        noises.getOrThrow(Noises.TEMPERATURE)),//オーバーワールドの温度分布、バイオーム生成に使用
                DensityFunctions.zero()/*DensityFunctions.shiftedNoise2d(
                        getDensity(densities, "shift_x"),
                        getDensity(densities, "shift_z"),
                        0.25D,
                        noises.getOrThrow(Noises.VEGETATION))*/,//植生のある場所
                DensityFunctions.zero(),//下３つはバイオーム生成に使えるノイズ
                DensityFunctions.zero(),//
                DensityFunctions.zero(),//
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                cave,//ブロックがある場所を決める
                DensityFunctions.constant(1),//鉱石生成可能な場所
                DensityFunctions.zero(),//鉱脈生成可能な場所
                DensityFunctions.zero());
    }

    private static DensityFunction getDensity(HolderGetter<DensityFunction> densities, String resourceLocation) {
        return getDensity(densities, ResourceKey.create(Registries.DENSITY_FUNCTION, new ResourceLocation(resourceLocation)));
    }

    private static DensityFunction getDensity(HolderGetter<DensityFunction> densities, ResourceKey<DensityFunction> resourceLocation) {
        return new DensityFunctions.HolderHolder(densities.getOrThrow(resourceLocation));
    }
}
