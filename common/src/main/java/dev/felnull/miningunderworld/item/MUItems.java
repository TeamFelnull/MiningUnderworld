package dev.felnull.miningunderworld.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.miningunderworld.MiningUnderworld;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;

import java.util.function.Function;

public class MUItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MiningUnderworld.MODID, Registries.ITEM);

    public static final RegistrySupplier<Item> IKISUGI_TEST = register("ikisugi_test");

    public static final RegistrySupplier<Item> COPPER_SWORD = register("copper_sword", (p) -> new WeatheringSwordItem(MUTiers.COPPER, 3, -2.4F, p));
    public static final RegistrySupplier<Item> COPPER_PICKAXE = register("copper_pickaxe", (p) -> new WeatheringPickaxeItem(MUTiers.COPPER, 1, -2.8F, p));
    public static final RegistrySupplier<Item> COPPER_AXE = register("copper_axe", (p) -> new WeatheringAxeItem(MUTiers.COPPER, 6.0F, -3.1F, p));
    public static final RegistrySupplier<Item> COPPER_SHOVEL = register("copper_shovel", (p) -> new WeatheringShovelItem(MUTiers.COPPER, 1.5F, -3.0F, p));
    public static final RegistrySupplier<Item> COPPER_HELMET = register("copper_helmet", (p) -> new WeatheringArmorItem(MUArmorMaterials.COPPER, EquipmentSlot.HEAD, p));
    public static final RegistrySupplier<Item> COPPER_CHESTPLATE = register("copper_chestplate", (p) -> new WeatheringArmorItem(MUArmorMaterials.COPPER, EquipmentSlot.CHEST, p));
    public static final RegistrySupplier<Item> COPPER_LEGGINGS = register("copper_leggings", (p) -> new WeatheringArmorItem(MUArmorMaterials.COPPER, EquipmentSlot.LEGS, p));
    public static final RegistrySupplier<Item> COPPER_BOOTS = register("copper_boots", (p) -> new WeatheringArmorItem(MUArmorMaterials.COPPER, EquipmentSlot.FEET, p));

    public static final RegistrySupplier<Item> EMERALD_SWORD = register("emerald_sword", p -> new SwordItem(MUTiers.EMERALD, 4, -1.0F, p));
    public static final RegistrySupplier<Item> EMERALD_PICKAXE = register("emerald_pickaxe", p -> new PickaxeItem(MUTiers.EMERALD, 1, -2.8F, p));
    public static final RegistrySupplier<Item> EMERALD_AXE = register("emerald_axe", p -> new AxeItem(MUTiers.EMERALD, 6.0F, -3.1F, p));
    public static final RegistrySupplier<Item> EMERALD_SHOVEL = register("emerald_shovel", p -> new ShovelItem(MUTiers.EMERALD, 1.5F, -3.0F, p));
    public static final RegistrySupplier<Item> EMERALD_HOE = register("emerald_hoe", p -> new HoeItem(MUTiers.EMERALD, 1, -3.0F, p));
    public static final RegistrySupplier<Item> EMERALD_HELMET = register("emerald_helmet", p -> new ArmorItem(MUArmorMaterials.EMERALD, EquipmentSlot.HEAD, p));
    public static final RegistrySupplier<Item> EMERALD_CHESTPLATE = register("emerald_chestplate", p -> new ArmorItem(MUArmorMaterials.EMERALD, EquipmentSlot.CHEST, p));
    public static final RegistrySupplier<Item> EMERALD_LEGGINGS = register("emerald_leggings", p -> new ArmorItem(MUArmorMaterials.EMERALD, EquipmentSlot.LEGS, p));
    public static final RegistrySupplier<Item> EMERALD_BOOTS = register("emerald_boots", p -> new ArmorItem(MUArmorMaterials.EMERALD, EquipmentSlot.FEET, p));

    public static final RegistrySupplier<Item> AMETHYST_INGOT = register("amethyst_ingot");
    public static final RegistrySupplier<Item> AMETHYST_HELMET = register("amethyst_helmet", p -> new ArmorItem(MUArmorMaterials.AMETHYST, EquipmentSlot.HEAD, p));
    public static final RegistrySupplier<Item> AMETHYST_CHESTPLATE = register("amethyst_chestplate", p -> new ArmorItem(MUArmorMaterials.AMETHYST, EquipmentSlot.CHEST, p));
    public static final RegistrySupplier<Item> AMETHYST_LEGGINGS = register("amethyst_leggings", p -> new ArmorItem(MUArmorMaterials.AMETHYST, EquipmentSlot.LEGS, p));
    public static final RegistrySupplier<Item> AMETHYST_BOOTS = register("amethyst_boots", p -> new ArmorItem(MUArmorMaterials.AMETHYST, EquipmentSlot.FEET, p));
    public static final RegistrySupplier<Item> AMETHYST_SWORD = register("amethyst_sword", p -> new SwordItem(MUTiers.AMETHYST, 4, -1.0F, p));
    public static final RegistrySupplier<Item> AMETHYST_PICKAXE = register("amethyst_pickaxe", p -> new PickaxeItem(MUTiers.AMETHYST, 1, -2.8F, p));
    public static final RegistrySupplier<Item> AMETHYST_AXE = register("amethyst_axe", p -> new AxeItem(MUTiers.AMETHYST, 6.0F, -3.1F, p));
    public static final RegistrySupplier<Item> AMETHYST_SHOVEL = register("amethyst_shovel", p -> new ShovelItem(MUTiers.AMETHYST, 1.5F, -3.0F, p));
    public static final RegistrySupplier<Item> AMETHYST_HOE = register("amethyst_hoe", p -> new HoeItem(MUTiers.AMETHYST, 1, -3.0F, p));

    private static RegistrySupplier<Item> register(String name) {
        return register(name, Item::new);
    }


    private static RegistrySupplier<Item> register(String name, Function<Item.Properties, Item> item) {
        return ITEMS.register(name, () -> item.apply(new Item.Properties().arch$tab(MUCreativeModeTab.MOD_TAB)));
    }

    public static void init() {
        ITEMS.register();
    }
}
