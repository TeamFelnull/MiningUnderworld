package dev.felnull.miningunderworld.item;

import dev.felnull.otyacraftengine.tag.PlatformItemTags;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum MUArmorMaterials implements ArmorMaterial {
    COPPER("copper",
            13,
            new int[]{3, 5, 6, 2},
            10,
            SoundEvents.COPPER_PLACE,
            0.0F,
            0.0F,
            () -> Ingredient.of(PlatformItemTags.copperIngots())),
    EMERALD("emerald",
            15,
            new int[]{2, 5, 6, 2},
            9,
            SoundEvents.ARMOR_EQUIP_IRON,
            0.0F,
            0.0F,
            () -> Ingredient.of(Items.EMERALD)),
    AMETHYST("amethyst",
            15,
            new int[]{2, 5, 6, 2},
            9,
            SoundEvents.ARMOR_EQUIP_IRON,
            0.0F,
            0.0F, () -> Ingredient.of(Items.AMETHYST_BLOCK)),
    REDSTONE("redstone",
            15,
            new int[]{2, 5, 6, 2},
            9,
            SoundEvents.ARMOR_EQUIP_IRON,
            0.0F,
            0.0F, () -> Ingredient.of(PlatformItemTags.redstoneDusts())),
    LAPIS_LAZULI("lapis_lazuli",
            15,
            new int[]{2, 5, 6, 2},
            9,
            SoundEvents.ARMOR_EQUIP_IRON,
            0.0F,
            0.0F, () -> Ingredient.of(Items.LAPIS_LAZULI));

    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    MUArmorMaterials(String name, int durabilityMultiplier, int[] slotProtections, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.slotProtections = slotProtections;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlot slot) {
        return HEALTH_PER_SLOT[slot.getIndex()] * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlot slot) {
        return this.slotProtections[slot.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.sound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return /*MiningUnderworld.MODID + ":" +*/ this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
