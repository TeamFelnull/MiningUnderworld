package dev.felnull.miningunderworld.data;

import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.miningunderworld.item.MUItems;
import dev.felnull.miningunderworld.recipe.MURecipeSerializers;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.RecipeProviderWrapper;
import dev.felnull.otyacraftengine.tag.PlatformItemTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class MURecipeProviderWrapper extends RecipeProviderWrapper {
    public MURecipeProviderWrapper(PackOutput packOutput, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, crossDataGeneratorAccess);
    }

    @Override
    public void generateRecipe(Consumer<FinishedRecipe> exporter, RecipeProviderAccess providerAccess) {

        //錆止めレシピ
        SpecialRecipeBuilder.special(MURecipeSerializers.WAXED_WEATHERING_ITEM.get())
                .save(exporter, modLoc("waxed_weathering_item").toString());

        //銅
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.COPPER_PICKAXE.get())
                .define('C', Items.COPPER_INGOT)
                .define('S', Items.STICK)
                .pattern("CCC")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(Items.COPPER_INGOT), providerAccess.has(Items.COPPER_INGOT))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.COPPER_SHOVEL.get())
                .define('C', Items.COPPER_INGOT)
                .define('S', Items.STICK)
                .pattern(" C ")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(Items.COPPER_INGOT), providerAccess.has(Items.COPPER_INGOT))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.COPPER_SWORD.get())
                .define('C', Items.COPPER_INGOT)
                .define('S', Items.STICK)
                .pattern(" C ")
                .pattern(" C ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(Items.COPPER_INGOT), providerAccess.has(Items.COPPER_INGOT))
                .save(exporter);

        //エメラルド
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.EMERALD_SHOVEL.get())
                .define('E', Items.EMERALD)
                .define('S', Items.STICK)
                .pattern(" E ")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(Items.EMERALD), providerAccess.has(Items.EMERALD))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.EMERALD_SWORD.get())
                .define('E', Items.EMERALD)
                .define('S', Items.STICK)
                .pattern(" E ")
                .pattern(" E ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(Items.EMERALD), providerAccess.has(Items.EMERALD))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.EMERALD_AXE.get())
                .define('E', Items.EMERALD)
                .define('S', Items.STICK)
                .pattern("EE ")
                .pattern("ES ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(Items.EMERALD), providerAccess.has(Items.EMERALD))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.EMERALD_PICKAXE.get())
                .define('E', Items.EMERALD)
                .define('S', Items.STICK)
                .pattern("EEE")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(Items.EMERALD), providerAccess.has(Items.EMERALD))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.EMERALD_HOE.get())
                .define('E', Items.EMERALD)
                .define('S', Items.STICK)
                .pattern("EE ")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(Items.EMERALD), providerAccess.has(Items.EMERALD))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.EMERALD_HELMET.get())
                .define('E', Items.EMERALD)
                .pattern("EEE")
                .pattern("E E")
                .unlockedBy(providerAccess.getHasName(Items.EMERALD), providerAccess.has(Items.EMERALD))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.EMERALD_CHESTPLATE.get())
                .define('E', Items.EMERALD)
                .pattern("E E")
                .pattern("EEE")
                .pattern("EEE")
                .unlockedBy(providerAccess.getHasName(Items.EMERALD), providerAccess.has(Items.EMERALD))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.EMERALD_LEGGINGS.get())
                .define('E', Items.EMERALD)
                .pattern("EEE")
                .pattern("E E")
                .pattern("E E")
                .unlockedBy(providerAccess.getHasName(Items.EMERALD), providerAccess.has(Items.EMERALD))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.EMERALD_BOOTS.get())
                .define('E', Items.EMERALD)
                .pattern("E E")
                .pattern("E E")
                .unlockedBy(providerAccess.getHasName(Items.EMERALD), providerAccess.has(Items.EMERALD))
                .save(exporter);

        //アメジスト
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.AMETHYST_SWORD.get())
                .define('A', MUItems.AMETHYST_INGOT.get())
                .define('S', Items.STICK)
                .pattern(" A ")
                .pattern(" A ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(MUItems.AMETHYST_INGOT.get()), providerAccess.has(MUItems.AMETHYST_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.AMETHYST_PICKAXE.get())
                .define('A', MUItems.AMETHYST_INGOT.get())
                .define('S', Items.STICK)
                .pattern("AAA")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(MUItems.AMETHYST_INGOT.get()), providerAccess.has(MUItems.AMETHYST_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.AMETHYST_AXE.get())
                .define('A', MUItems.AMETHYST_INGOT.get())
                .define('S', Items.STICK)
                .pattern("AA")
                .pattern("AS")
                .pattern(" S")
                .unlockedBy(providerAccess.getHasName(MUItems.AMETHYST_INGOT.get()), providerAccess.has(MUItems.AMETHYST_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.AMETHYST_SHOVEL.get())
                .define('A', MUItems.AMETHYST_INGOT.get())
                .define('S', Items.STICK)
                .pattern(" A ")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(MUItems.AMETHYST_INGOT.get()), providerAccess.has(MUItems.AMETHYST_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.AMETHYST_HOE.get())
                .define('A', MUItems.AMETHYST_INGOT.get())
                .define('S', Items.STICK)
                .pattern("AA ")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(MUItems.AMETHYST_INGOT.get()), providerAccess.has(MUItems.AMETHYST_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.AMETHYST_HELMET.get())
                .define('A', MUItems.AMETHYST_INGOT.get())
                .pattern("AAA")
                .pattern("A A")
                .unlockedBy(providerAccess.getHasName(MUItems.AMETHYST_INGOT.get()), providerAccess.has(MUItems.AMETHYST_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.AMETHYST_CHESTPLATE.get())
                .define('A', MUItems.AMETHYST_INGOT.get())
                .pattern("A A")
                .pattern("AAA")
                .pattern("AAA")
                .unlockedBy(providerAccess.getHasName(MUItems.AMETHYST_INGOT.get()), providerAccess.has(MUItems.AMETHYST_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.AMETHYST_LEGGINGS.get())
                .define('A', MUItems.AMETHYST_INGOT.get())
                .pattern("AAA")
                .pattern("A A")
                .pattern("A A")
                .unlockedBy(providerAccess.getHasName(MUItems.AMETHYST_INGOT.get()), providerAccess.has(MUItems.AMETHYST_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.AMETHYST_BOOTS.get())
                .define('A', MUItems.AMETHYST_INGOT.get())
                .pattern("A A")
                .pattern("A A")
                .unlockedBy(providerAccess.getHasName(MUItems.AMETHYST_INGOT.get()), providerAccess.has(MUItems.AMETHYST_INGOT.get()))
                .save(exporter);

        //レッドストーン
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.REDSTONE_SWORD.get())
                .define('R', MUItems.REDSTONE_INGOT.get())
                .define('S', Items.STICK)
                .pattern(" R ")
                .pattern(" R ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(MUItems.REDSTONE_INGOT.get()), providerAccess.has(MUItems.REDSTONE_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.REDSTONE_PICKAXE.get())
                .define('R', MUItems.REDSTONE_INGOT.get())
                .define('S', Items.STICK)
                .pattern("RRR")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(MUItems.REDSTONE_INGOT.get()), providerAccess.has(MUItems.REDSTONE_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.REDSTONE_AXE.get())
                .define('R', MUItems.REDSTONE_INGOT.get())
                .define('S', Items.STICK)
                .pattern("RR")
                .pattern("RS")
                .pattern(" S")
                .unlockedBy(providerAccess.getHasName(MUItems.REDSTONE_INGOT.get()), providerAccess.has(MUItems.REDSTONE_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.REDSTONE_SHOVEL.get())
                .define('R', MUItems.REDSTONE_INGOT.get())
                .define('S', Items.STICK)
                .pattern(" R ")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(MUItems.REDSTONE_INGOT.get()), providerAccess.has(MUItems.REDSTONE_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.REDSTONE_HOE.get())
                .define('R', MUItems.REDSTONE_INGOT.get())
                .define('S', Items.STICK)
                .pattern("RR ")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(MUItems.REDSTONE_INGOT.get()), providerAccess.has(MUItems.REDSTONE_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.REDSTONE_HELMET.get())
                .define('R', MUItems.REDSTONE_INGOT.get())
                .pattern("RRR")
                .pattern("R R")
                .unlockedBy(providerAccess.getHasName(MUItems.REDSTONE_INGOT.get()), providerAccess.has(MUItems.REDSTONE_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.REDSTONE_CHESTPLATE.get())
                .define('R', MUItems.REDSTONE_INGOT.get())
                .pattern("R R")
                .pattern("RRR")
                .pattern("RRR")
                .unlockedBy(providerAccess.getHasName(MUItems.REDSTONE_INGOT.get()), providerAccess.has(MUItems.REDSTONE_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.REDSTONE_LEGGINGS.get())
                .define('R', MUItems.REDSTONE_INGOT.get())
                .pattern("RRR")
                .pattern("R R")
                .pattern("R R")
                .unlockedBy(providerAccess.getHasName(MUItems.REDSTONE_INGOT.get()), providerAccess.has(MUItems.REDSTONE_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.REDSTONE_BOOTS.get())
                .define('R', MUItems.REDSTONE_INGOT.get())
                .pattern("R R")
                .pattern("R R")
                .unlockedBy(providerAccess.getHasName(MUItems.REDSTONE_INGOT.get()), providerAccess.has(MUItems.REDSTONE_INGOT.get()))
                .save(exporter);

        //ラピスラズリ
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.LAPIS_LAZULI_SWORD.get())
                .define('L', MUItems.LAPIS_LAZULI_INGOT.get())
                .define('S', Items.STICK)
                .pattern(" L ")
                .pattern(" L ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(MUItems.REDSTONE_INGOT.get()), providerAccess.has(MUItems.REDSTONE_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.LAPIS_LAZULI_PICKAXE.get())
                .define('L', MUItems.LAPIS_LAZULI_INGOT.get())
                .define('S', Items.STICK)
                .pattern("LLL")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(MUItems.LAPIS_LAZULI_INGOT.get()), providerAccess.has(MUItems.LAPIS_LAZULI_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.LAPIS_LAZULI_AXE.get())
                .define('L', MUItems.LAPIS_LAZULI_INGOT.get())
                .define('S', Items.STICK)
                .pattern("LL")
                .pattern("LS")
                .pattern(" S")
                .unlockedBy(providerAccess.getHasName(MUItems.LAPIS_LAZULI_INGOT.get()), providerAccess.has(MUItems.LAPIS_LAZULI_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.LAPIS_LAZULI_SHOVEL.get())
                .define('L', MUItems.LAPIS_LAZULI_INGOT.get())
                .define('S', Items.STICK)
                .pattern(" L ")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(MUItems.LAPIS_LAZULI_INGOT.get()), providerAccess.has(MUItems.LAPIS_LAZULI_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.LAPIS_LAZULI_HOE.get())
                .define('L', MUItems.LAPIS_LAZULI_INGOT.get())
                .define('S', Items.STICK)
                .pattern("LL ")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(providerAccess.getHasName(MUItems.LAPIS_LAZULI_INGOT.get()), providerAccess.has(MUItems.LAPIS_LAZULI_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.LAPIS_LAZULI_HELMET.get())
                .define('L', MUItems.LAPIS_LAZULI_INGOT.get())
                .pattern("LLL")
                .pattern("L L")
                .unlockedBy(providerAccess.getHasName(MUItems.LAPIS_LAZULI_INGOT.get()), providerAccess.has(MUItems.LAPIS_LAZULI_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.LAPIS_LAZULI_CHESTPLATE.get())
                .define('L', MUItems.LAPIS_LAZULI_INGOT.get())
                .pattern("L L")
                .pattern("LLL")
                .pattern("LLL")
                .unlockedBy(providerAccess.getHasName(MUItems.LAPIS_LAZULI_INGOT.get()), providerAccess.has(MUItems.LAPIS_LAZULI_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.LAPIS_LAZULI_LEGGINGS.get())
                .define('L', MUItems.LAPIS_LAZULI_INGOT.get())
                .pattern("LLL")
                .pattern("L L")
                .pattern("L L")
                .unlockedBy(providerAccess.getHasName(MUItems.LAPIS_LAZULI_INGOT.get()), providerAccess.has(MUItems.LAPIS_LAZULI_INGOT.get()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.LAPIS_LAZULI_BOOTS.get())
                .define('L', MUItems.LAPIS_LAZULI_INGOT.get())
                .pattern("L L")
                .pattern("L L")
                .unlockedBy(providerAccess.getHasName(MUItems.LAPIS_LAZULI_INGOT.get()), providerAccess.has(MUItems.LAPIS_LAZULI_INGOT.get()))
                .save(exporter);

        //中間素材
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.RING.get())
                .define('G', Items.GOLD_INGOT)
                .pattern("GGG")
                .pattern("G G")
                .pattern("GGG")
                .unlockedBy(providerAccess.getHasName(Items.GOLD_INGOT), providerAccess.has(Items.GOLD_INGOT))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.JEWELRYS_STAR.get())
                .define('N', Items.NETHER_STAR)
                .define('D', Items.DIAMOND_BLOCK)
                .define('E', Items.EMERALD_BLOCK)
                .pattern("DDD")
                .pattern("DNE")
                .pattern("EEE")
                .unlockedBy(providerAccess.getHasName(Items.NETHER_STAR), providerAccess.has(Items.NETHER_STAR))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.REDSTONE_INGOT.get())
                .define('R', Items.REDSTONE_BLOCK)
                .pattern("RRR")
                .pattern("RRR")
                .pattern("RRR")
                .unlockedBy(providerAccess.getHasName(Items.REDSTONE_BLOCK), providerAccess.has(Items.REDSTONE_BLOCK))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.AMETHYST_INGOT.get())
                .define('A', Items.AMETHYST_BLOCK)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .unlockedBy(providerAccess.getHasName(Items.AMETHYST_BLOCK), providerAccess.has(Items.AMETHYST_BLOCK))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.LAPIS_LAZULI_INGOT.get())
                .define('L', Items.LAPIS_BLOCK)
                .pattern("LLL")
                .pattern("LLL")
                .pattern("LLL")
                .unlockedBy(providerAccess.getHasName(Items.LAPIS_BLOCK), providerAccess.has(Items.LAPIS_BLOCK))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, MUBlocks.MINING_TNT.get(), 4)
                .define('T', Blocks.TNT)
                .define('P', Items.IRON_PICKAXE)
                .define('S', PlatformItemTags.stone().getKey())
                .pattern("TST")
                .pattern("SPS")
                .pattern("TST")
                .unlockedBy(providerAccess.getHasName(Items.IRON_PICKAXE), providerAccess.has(Items.IRON_PICKAXE))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, MUBlocks.SOLID_TAR.get())
                .define('T', MUItems.TAR_CHUNK.get())
                .pattern("TT")
                .pattern("TT")
                .unlockedBy(providerAccess.getHasName(MUItems.TAR_CHUNK.get()), providerAccess.has(MUItems.TAR_CHUNK.get()))
                .save(exporter);

    }
}
