package dev.felnull.miningunderworld.recipe;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.miningunderworld.MiningUnderworld;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import java.util.function.Supplier;

public class MURecipeSerializers {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(MiningUnderworld.MODID, Registries.RECIPE_SERIALIZER);
    public static final RegistrySupplier<SimpleCraftingRecipeSerializer<WaxedWeatheringItemRecipe>> WAXED_WEATHERING_ITEM = register("waxed_weathering_item", () -> new SimpleCraftingRecipeSerializer<>(WaxedWeatheringItemRecipe::new));

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> RegistrySupplier<S> register(String name, Supplier<S> recipe) {
        return RECIPE_SERIALIZERS.register(name, recipe);
    }

    public static void init() {
        RECIPE_SERIALIZERS.register();

    }
}
