package dev.felnull.miningunderworld.dimension;

import dev.felnull.miningunderworld.block.MUBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

public class MUSurfaceRule {

    //https://misode.github.io/worldgen/noise-settings/
    public static SurfaceRules.RuleSource create() {
        var BEDROCK = stateRule(Blocks.BEDROCK);
        var bedrockRules = SurfaceRules.sequence(
                SurfaceRules.ifTrue(decreaseGradiently("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)),
                        BEDROCK),
                SurfaceRules.ifTrue(increaseGradiently("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top()),
                        BEDROCK));//上下５マスに徐々岩盤

        var biomeRules = SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.isBiome(MUBiomes.CRYSTAL_CAVE),
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.ON_CEILING,
                                        stateRule(MUBlocks.BLUE_CLAY.get())),
                                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR,
                                        stateRule(MUBlocks.BLUE_SAND.get())),
                                stateRule(MUBlocks.BLUE_CLAY.get()))),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(MUBiomes.COLLAPSING_CAVE),
                        SurfaceRules.ifTrue(SurfaceRules.ON_CEILING,
                                stateRule(Blocks.GRAVEL)))
        );

        return SurfaceRules.sequence(bedrockRules, biomeRules, stateRule(Blocks.DEEPSLATE));
    }

    public static void example() {
        //RuleSource:場所ごとのブロック指定、地形色付けのルール
        var stateRule = stateRule(Blocks.STONE);//どこでも石
        //ConditionSource:コンディション、場所指定
        var condition = ijou(81);//yが81以上のとこ
        var ifRule = SurfaceRules.ifTrue(condition, stateRule);//yが81以上は常に石
        var rules = SurfaceRules.sequence(//ルールを順次に適用するルール
                ifRule, //yが81以上なら石
                SurfaceRules.ifTrue(ijou(36),
                        stateRule(Blocks.DEEPSLATE)),//そうでなくyが36以上なら安山岩
                stateRule(MUBlocks.TEST_BLOCK.get()));//そうでもないならTEST_BLOCK
    }

    public static SurfaceRules.RuleSource stateRule(Block p_194811_) {
        return SurfaceRules.state(p_194811_.defaultBlockState());
    }

    public static SurfaceRules.ConditionSource ijou(int y) {
        return ijou(VerticalAnchor.absolute(y));//y座標がy以上の場所
    }

    public static SurfaceRules.ConditionSource ijou(VerticalAnchor va) {
        return SurfaceRules.yBlockCheck(va, 0);
    }

    public static SurfaceRules.ConditionSource miman(int y) {
        return SurfaceRules.not(ijou(y));//y座標がy未満の場所
    }

    public static SurfaceRules.ConditionSource startIjou(int y) {
        return SurfaceRules.yStartCheck(VerticalAnchor.absolute(y), 0);
        //「そこより上で最初に空気に触れるとこ」のy座標がy以上の場所(「地表」は「そこより上で最後に空気に触れるとこ」と定義でき、その逆)
    }

    public static SurfaceRules.ConditionSource startMiman(int y) {
        return SurfaceRules.not(startIjou(y));
        //「そこより上で最初に空気に触れるとこ」のy座標がy未満の場所
    }

    public static SurfaceRules.ConditionSource decreaseGradiently(String randomFactoryName, VerticalAnchor startDecrease, VerticalAnchor endDecrease) {
        return SurfaceRules.verticalGradient(randomFactoryName, startDecrease, endDecrease);
        //startDecreaseより下は常にあって、endDecreaseより上は常にない
        //その間は存在確率が境界と直線でつながるようになる
    }

    public static SurfaceRules.ConditionSource increaseGradiently(String randomFactoryName, VerticalAnchor startIncrease, VerticalAnchor endIncrease) {
        return SurfaceRules.not(decreaseGradiently(randomFactoryName, startIncrease, endIncrease));
        //startIncreaseより下は常になく、endIncreaseより上は常にある
        //その間は存在確率が境界と直線でつながるようになる
    }

    private static SurfaceRules.ConditionSource surfaceNoiseAbove(double p_194809_) {
        return SurfaceRules.noiseCondition(Noises.SURFACE, p_194809_ / 8.25D, Double.MAX_VALUE);
    }
}
