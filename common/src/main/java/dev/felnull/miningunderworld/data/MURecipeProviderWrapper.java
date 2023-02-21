package dev.felnull.miningunderworld.data;

import dev.felnull.miningunderworld.item.MUItems;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.RecipeProviderWrapper;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class MURecipeProviderWrapper extends RecipeProviderWrapper {
    public MURecipeProviderWrapper(PackOutput packOutput, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, crossDataGeneratorAccess);
    }

    @Override
    public void generateRecipe(Consumer<FinishedRecipe> exporter, RecipeProviderAccess providerAccess) {
        //cppper
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

        //emerald
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

        //amethyst
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MUItems.AMETHYST_INGOT.get())
                .define('A', Items.AMETHYST_BLOCK)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .unlockedBy(providerAccess.getHasName(MUItems.AMETHYST_INGOT.get()), providerAccess.has(MUItems.AMETHYST_INGOT.get()))
                .save(exporter);

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
                .pattern("AS ")
                .pattern(" S ")
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
    }
}
