package dev.felnull.miningunderworld.dimension;

import dev.felnull.miningunderworld.block.MUBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

public class MUSurfaceRule {

    public static SurfaceRules.RuleSource create() {
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

        var BEDROCK = stateRule(Blocks.BEDROCK);
        rules = SurfaceRules.sequence(
                SurfaceRules.ifTrue(jojoniNakunaru("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)),
                        BEDROCK),
                SurfaceRules.ifTrue(jojoniHueru("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top()),
                        BEDROCK),
                stateRule(Blocks.DEEPSLATE));//上下５マスに徐々岩盤追加

        return rules;
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

    public static SurfaceRules.ConditionSource jojoniNakunaru(String randomFactoryName, VerticalAnchor nakunaruStart, VerticalAnchor nakunaruEnd) {
        return SurfaceRules.verticalGradient(randomFactoryName, nakunaruStart, nakunaruEnd);
        //nakunaruStartより下は常にあって、nakunaruEndより上は常にない
        //その間は存在確率が境界と直線でつながるようになる
    }

    public static SurfaceRules.ConditionSource jojoniHueru(String randomFactoryName, VerticalAnchor hueruStart, VerticalAnchor hueruEnd) {
        return SurfaceRules.not(jojoniNakunaru(randomFactoryName, hueruStart, hueruEnd));
        //hueruStartより下は常になく、hueruEndより上は常にある
        //その間は存在確率が境界と直線でつながるようになる
    }
}
