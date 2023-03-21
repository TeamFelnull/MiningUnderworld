package dev.felnull.miningunderworld.recipe;

import dev.felnull.miningunderworld.item.WeatheringItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class WaxedWeatheringItemRecipe extends CustomRecipe {
    public WaxedWeatheringItemRecipe(ResourceLocation resourceLocation, CraftingBookCategory craftingBookCategory) {
        super(resourceLocation, craftingBookCategory);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        int honeycomb = 0;
        int witem = 0;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);

            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof WeatheringItem && !WeatheringItem.isWaxed(stack)) {
                    witem++;
                } else if (stack.is(Items.HONEYCOMB)) {
                    honeycomb++;
                } else {
                    return false;
                }

                if (honeycomb > 1 || witem > 1)
                    return false;
            }
        }

        return honeycomb == 1 && witem == 1;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack witem = ItemStack.EMPTY;

        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof WeatheringItem) {
                witem = stack.copy();
                break;
            }
        }

        WeatheringItem.setWaxed(witem, true);
        return witem;
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return x * y >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MURecipeSerializers.WAXED_WEATHERING_ITEM.get();
    }
}
