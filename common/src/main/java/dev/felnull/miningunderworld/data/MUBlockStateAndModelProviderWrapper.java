package dev.felnull.miningunderworld.data;

import com.mojang.datafixers.util.Pair;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.model.BlockStateAndModelProviderAccess;
import dev.felnull.otyacraftengine.data.model.FileTexture;
import dev.felnull.otyacraftengine.data.provider.BlockStateAndModelProviderWrapper;
import net.minecraft.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.function.Function;

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

        multiface(providerAccess, MUBlocks.TAR_STAINS.get(), modLoc("block/solid_tar"));
        multiface(providerAccess, MUBlocks.SMALL_TAR_STAINS.get(), modLoc("block/small_tar_stains"));
        providerAccess.simpleCubeBlockStateModelAndItemModel(MUBlocks.SOLID_TAR.get());
        semisolidTar(providerAccess);

        soakedBlock(providerAccess, MUBlocks.SOAKED_TAR_STONE.get(), new ResourceLocation("block/stone"), modLoc("block/soaked_tar"));
        soakedBlock(providerAccess, MUBlocks.SOAKED_TAR_DEEPSLATE.get(), new ResourceLocation("block/deepslate"), modLoc("block/soaked_tar"));
        soakedBlock(providerAccess, MUBlocks.SOAKED_LAVA_STONE.get(), new ResourceLocation("block/stone"), modLoc("block/soaked_lava"));
        soakedBlock(providerAccess, MUBlocks.SOAKED_LAVA_DEEPSLATE.get(), new ResourceLocation("block/deepslate"), modLoc("block/soaked_lava"));

        var miningTNTModel = providerAccess.cubeBottomTopBlockModel(MUBlocks.MINING_TNT.get(), modLoc("block/mining_tnt_bottom"), modLoc("block/mining_tnt_side"), modLoc("block/mining_tnt_top"));
        providerAccess.simpleBlockItemModel(MUBlocks.MINING_TNT.get(), miningTNTModel);
        providerAccess.simpleBlockState(MUBlocks.MINING_TNT.get(), miningTNTModel);

        MUBlocks.CRYSTALS.stream().map(RegistrySupplier::get).forEach(block -> {
            var model = providerAccess.cubeAllBlockModel(block, FileTexture.ofUncheck(block.arch$registryName().withPrefix("block/")));
            providerAccess.simpleBlockItemModel(block, model);
            providerAccess.simpleBlockState(block, model);
        });
    }

    private void semisolidTar(BlockStateAndModelProviderAccess providerAccess) {
        var allCube = providerAccess.cubeAllBlockModel(modLoc("semisolid_tar_height16"), modLoc("block/solid_tar"));

        providerAccess.addBlockStateGenerator(MultiVariantGenerator.multiVariant(MUBlocks.SEMISOLID_TAR.get())
                .with(PropertyDispatch.property(BlockStateProperties.LAYERS)
                        .generate(layer -> {
                            Variant variant = Variant.variant();
                            ResourceLocation loc;

                            if (layer < 8) {
                                loc = providerAccess.existingModel(ModelLocationUtils.getModelLocation(MUBlocks.SEMISOLID_TAR.get(), "_height" + layer * 2)).getLocation();
                            } else {
                                loc = allCube.getLocation();
                            }

                            return variant.with(VariantProperties.MODEL, loc);
                        })));

        providerAccess.simpleBlockItemModel(MUBlocks.SEMISOLID_TAR.get(), providerAccess.existingModel(modLoc("block/semisolid_tar_height2")));
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

    private void multiface(BlockStateAndModelProviderAccess providerAccess, Block block, ResourceLocation textureLocation) {
        providerAccess.itemModelProviderAccess().basicFlatItem(block.asItem(), textureLocation);

        MultiPartGenerator multiPartGenerator = MultiPartGenerator.multiPart(block);

        Condition.TerminalCondition terminalCondition = Util.make(Condition.condition(), (terminalConditionx) -> {
            BlockModelGenerators.MULTIFACE_GENERATOR.stream().map(Pair::getFirst).forEach((booleanProperty) -> {
                if (block.defaultBlockState().hasProperty(booleanProperty)) {
                    terminalConditionx.term(booleanProperty, false);
                }
            });
        });

        var loc = ModelLocationUtils.getModelLocation(block);

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
