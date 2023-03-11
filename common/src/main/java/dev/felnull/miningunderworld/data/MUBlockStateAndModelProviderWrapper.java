package dev.felnull.miningunderworld.data;

import com.mojang.datafixers.util.Pair;
import dev.felnull.miningunderworld.block.CrystalBlock;
import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.miningunderworld.util.MUUtils;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.model.BlockStateAndModelProviderAccess;
import dev.felnull.otyacraftengine.data.model.FileTexture;
import dev.felnull.otyacraftengine.data.provider.BlockStateAndModelProviderWrapper;
import net.minecraft.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.blockstates.MultiPartGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.function.Function;
import java.util.stream.IntStream;

public class MUBlockStateAndModelProviderWrapper extends BlockStateAndModelProviderWrapper {
    public MUBlockStateAndModelProviderWrapper(PackOutput packOutput, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, crossDataGeneratorAccess);
    }

    @Override
    public void generateStatesAndModels(BlockStateAndModelProviderAccess providerAccess) {
        providerAccess.simpleCubeBlockStateModelAndItemModel(MUBlocks.TEST_BLOCK.get());

        existingModelAndState(providerAccess, MUBlocks.LOOT_POT.get(), modLoc("block/loot_pot"));
        existingModelAndState(providerAccess, MUBlocks.GOLDEN_LOOT_POT.get(), modLoc("block/golden_loot_pot"));

        fluidModelAndState(providerAccess, MUBlocks.TEST_FLUID.get(), modLoc("block/test_block"));
        fluidModelAndState(providerAccess, MUBlocks.TAR.get(), modLoc("block/tar_still"));

        multiface(providerAccess, MUBlocks.TAR_STAINS.get());
        multiface(providerAccess, MUBlocks.SMALL_TAR_STAINS.get());

        soakedBlock(providerAccess, MUBlocks.SOAKED_TAR_STONE.get(), new ResourceLocation("block/stone"), modLoc("block/soaked_tar"));
        soakedBlock(providerAccess, MUBlocks.SOAKED_TAR_DEEPSLATE.get(), new ResourceLocation("block/deepslate"), modLoc("block/soaked_tar"));
        soakedBlock(providerAccess, MUBlocks.SOAKED_LAVA_STONE.get(), new ResourceLocation("block/stone"), modLoc("block/soaked_lava"));
        soakedBlock(providerAccess, MUBlocks.SOAKED_LAVA_DEEPSLATE.get(), new ResourceLocation("block/deepslate"), modLoc("block/soaked_lava"));

        var miningTNTModel = providerAccess.cubeBottomTopBlockModel(MUBlocks.MINING_TNT.get(), modLoc("block/mining_tnt_bottom"), modLoc("block/mining_tnt_side"), modLoc("block/mining_tnt_top"));
        providerAccess.simpleBlockItemModel(MUBlocks.MINING_TNT.get(), miningTNTModel);
        providerAccess.simpleBlockState(MUBlocks.MINING_TNT.get(), miningTNTModel);


        //generate crystal blockstate & block model
        var mpg = MultiPartGenerator.multiPart(MUBlocks.CRYSTAL.get());
        IntStream.rangeClosed(0, CrystalBlock.MAX_ID).forEach(i -> {
            var loc = MUUtils.modLoc("block/crystal_" + i);
            providerAccess.cubeAllBlockModel(modLoc("crystal_" + i), loc);
            mpg.with(Condition.condition().term(CrystalBlock.ORE_ID, i), Variant.variant().with(VariantProperties.MODEL, loc));
        });/*TODO:アイテムモデル生成
        providerAccess.addBlockStateGenerator(mpg);
        IntStream.rangeClosed(0, CrystalBlock.MAX_ID).forEach(i -> providerAccess.itemModelProviderAccess().basicFlatItem(
                        CrystalItem.withId(i),
                        MUUtils.modLoc("block/crystal_" + i)));*/
    }

    private void soakedBlock(BlockStateAndModelProviderAccess providerAccess, Block block, ResourceLocation blockLoc, ResourceLocation soakedLoc) {
        var model = providerAccess.parentedBlockModel(block, modLoc("block/template_soaked_block"))
                .texture("0", blockLoc)
                .texture("1", soakedLoc)
                .texture("particle", blockLoc);

        providerAccess.simpleBlockItemModel(block, model);
        providerAccess.simpleBlockState(block, model);
    }

    private void existingModelAndState(BlockStateAndModelProviderAccess providerAccess, Block block, ResourceLocation existingModelLoc) {
        var exModel = providerAccess.existingModel(existingModelLoc);
        providerAccess.simpleBlockState(block, exModel);
        providerAccess.simpleBlockItemModel(block, exModel);
    }

    private void fluidModelAndState(BlockStateAndModelProviderAccess providerAccess, Block block, ResourceLocation particle) {
        providerAccess.simpleBlockState(block, providerAccess.particleOnlyModel(block, particle));
    }

    private void multiface(BlockStateAndModelProviderAccess providerAccess, Block block) {
        var loc = ModelLocationUtils.getModelLocation(block);

        providerAccess.itemModelProviderAccess().basicFlatItem(block.asItem(), loc);

        MultiPartGenerator multiPartGenerator = MultiPartGenerator.multiPart(block);

        Condition.TerminalCondition terminalCondition = Util.make(Condition.condition(), (terminalConditionx) -> {
            BlockModelGenerators.MULTIFACE_GENERATOR.stream().map(Pair::getFirst).forEach((booleanProperty) -> {
                if (block.defaultBlockState().hasProperty(booleanProperty)) {
                    terminalConditionx.term(booleanProperty, false);
                }
            });
        });

        for (Pair<BooleanProperty, Function<ResourceLocation, Variant>> propertyLoc : BlockModelGenerators.MULTIFACE_GENERATOR) {
            BooleanProperty booleanProperty = propertyLoc.getFirst();
            Function<ResourceLocation, Variant> function = propertyLoc.getSecond();
            if (block.defaultBlockState().hasProperty(booleanProperty)) {
                multiPartGenerator.with(Condition.condition().term(booleanProperty, true), function.apply(loc));
                multiPartGenerator.with(terminalCondition, function.apply(loc));
            }
        }

        providerAccess.addBlockStateGenerator(multiPartGenerator);
    }
}
