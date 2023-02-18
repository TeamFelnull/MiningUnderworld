package dev.felnull.miningunderworld.data;

import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.RecipeProviderWrapper;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
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
    }
}
