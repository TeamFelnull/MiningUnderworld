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
