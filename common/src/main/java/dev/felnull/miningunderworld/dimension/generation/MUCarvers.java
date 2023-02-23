package dev.felnull.miningunderworld.dimension.generation;

import dev.felnull.miningunderworld.MiningUnderworld;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.TrapezoidFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;

public class MUCarvers {
    public static ResourceKey<ConfiguredWorldCarver<?>> COLLAPSING_CANYON = register("collapsing_canyon");

    public static ResourceKey<ConfiguredWorldCarver<?>> register(String name) {
        return ResourceKey.create(Registries.CONFIGURED_CARVER, MiningUnderworld.modLoc(name));
    }

    public static RegistrySetBuilder addToBuilder(RegistrySetBuilder builder) {//データパック自動生成に登録
        return builder.add(Registries.CONFIGURED_CARVER, context -> {
           context.register(COLLAPSING_CANYON, WorldCarver.CANYON.configured(new CanyonCarverConfiguration(
                    0.1F,//生成確率
                    UniformHeight.of(VerticalAnchor.bottom(), VerticalAnchor.top()),//生成範囲きっと
                    ConstantFloat.of(10.0F),//ランダムで決まる大きさの倍率？
                    VerticalAnchor.aboveBottom(8),//溶岩になるとこ
                    CarverDebugSettings.of(false, Blocks.WARPED_BUTTON.defaultBlockState()),
                    context.lookup(Registries.BLOCK).getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES),//carverでえぐれるブロック
                    UniformFloat.of(-0.125F, 0.125F),//回転..?
                    new CanyonCarverConfiguration.CanyonShapeConfiguration(
                            UniformFloat.of(0.75F, 1.0F),
                            TrapezoidFloat.of(0.0F, 6.0F, 2.0F),
                            3,
                            UniformFloat.of(0.75F, 1.0F),
                            1.0F,
                            0.0F)))) ;
        });
    }
}
