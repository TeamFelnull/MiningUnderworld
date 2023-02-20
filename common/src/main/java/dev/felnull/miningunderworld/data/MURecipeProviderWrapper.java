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
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.DIAMOND)
                .requires(Items.DIRT)
                .requires(ItemTags.BOATS)
                .unlockedBy(providerAccess.getHasName(Items.DIRT), providerAccess.has(Items.DIRT))
                .save(exporter);

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
    }
}
