package dev.felnull.miningunderworld.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.miningunderworld.MiningUnderworld;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class MUItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MiningUnderworld.MODID, Registries.ITEM);
    public static final RegistrySupplier<Item> IKISUGI_TEST = register("ikisugi_test");

    public static final RegistrySupplier<Item> COPPER_SWORD = register("copper_sword", () -> new OxidizingSwordItem(MUTiers.COPPER, 3, -2.4F, new Item.Properties().arch$tab(MUCreativeModeTab.MOD_TAB)));
    public static final RegistrySupplier<Item> COPPER_PICKAXE = register("copper_pickaxe", () -> new OxidizingPickaxeItem(MUTiers.COPPER, 1, -2.8F, new Item.Properties().arch$tab(MUCreativeModeTab.MOD_TAB)));
    public static final RegistrySupplier<Item> COPPER_AXE = register("copper_axe", () -> new OxidizingAxeItem(MUTiers.COPPER, 6.0F, -3.1F, new Item.Properties().arch$tab(MUCreativeModeTab.MOD_TAB)));
    public static final RegistrySupplier<Item> COPPER_SHOVEL = register("copper_shovel", () -> new OxidizingShovelItem(MUTiers.COPPER, 1.5F, -3.0F, new Item.Properties().arch$tab(MUCreativeModeTab.MOD_TAB)));

    private static RegistrySupplier<Item> register(String name) {
        return register(name, () -> new Item(new Item.Properties().arch$tab(MUCreativeModeTab.MOD_TAB)));
    }

    private static RegistrySupplier<Item> register(String name, Supplier<Item> item) {
        return ITEMS.register(name, item);
    }

    public static void init() {
        ITEMS.register();
    }
}
