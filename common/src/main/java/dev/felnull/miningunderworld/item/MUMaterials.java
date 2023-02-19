//package dev.felnull.miningunderworld.item;
//
//import dev.felnull.miningunderworld.MiningUnderworld;
//import net.minecraft.sounds.SoundEvent;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.util.LazyLoadedValue;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.item.ArmorMaterial;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.crafting.Ingredient;
//
//import java.util.function.Supplier;
//
//public enum MUMaterials implements ArmorMaterial {
//    COPPER_MATERIALS(MiningUnderworld.MOD_ID + ":miningunderworld" , 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> {
//        return Ingredient.of(Items.COPPER_INGOT);
//    }),
//
//    EXPOSED_COPPER(MiningUnderworld.MOD_ID + ":miningunderworld" , 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> {
//        return Ingredient.of(Items.COPPER_INGOT);
//    }),
//
//    WEATHERED_COPPER(MiningUnderworld.MOD_ID + ":miningunderworld" , 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> {
//        return Ingredient.of(Items.COPPER_INGOT);
//    }),
//
//    OXIDIZED_COPPER(MiningUnderworld.MOD_ID + ":miningunderworld" , 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> {
//        return Ingredient.of(Items.COPPER_INGOT);
//    });
//
//    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
//    private final String name;
//    private final int durabilityMultiplier;
//    private final int[] slotProtections;
//    private final int enchantmentValue;
//    private final SoundEvent sound;
//    private final float toughness;
//    private final float knockbackResistance;
//    private final LazyLoadedValue<Ingredient> repairIngredient;
//
//    private ArmorMaterials(String string2, int j, int[] is, int k, SoundEvent soundEvent, float f, float g, Supplier supplier) {
//        this.name = string2;
//        this.durabilityMultiplier = j;
//        this.slotProtections = is;
//        this.enchantmentValue = k;
//        this.sound = soundEvent;
//        this.toughness = f;
//        this.knockbackResistance = g;
//        this.repairIngredient = new LazyLoadedValue(supplier);
//    }
//
//    MUMaterials(String name, int durabilityMultiplier, int[] slotProtections, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, LazyLoadedValue<Ingredient> repairIngredient) {
//        this.name = name;
//        this.durabilityMultiplier = durabilityMultiplier;
//        this.slotProtections = slotProtections;
//        this.enchantmentValue = enchantmentValue;
//        this.sound = sound;
//        this.toughness = toughness;
//        this.knockbackResistance = knockbackResistance;
//        this.repairIngredient = repairIngredient;
//    }
//
//    public int getDurabilityForSlot(EquipmentSlot equipmentSlot) {
//        return HEALTH_PER_SLOT[equipmentSlot.getIndex()] * this.durabilityMultiplier;
//    }
//
//    public int getDefenseForSlot(EquipmentSlot equipmentSlot) {
//        return this.slotProtections[equipmentSlot.getIndex()];
//    }
//
//    public int getEnchantmentValue() {
//        return this.enchantmentValue;
//    }
//
//    public SoundEvent getEquipSound() {
//        return this.sound;
//    }
//
//    public Ingredient getRepairIngredient() {
//        return (Ingredient)this.repairIngredient.get();
//    }
//
//    public String getName() {
//        return this.name;
//    }
//
//    public float getToughness() {
//        return this.toughness;
//    }
//
//    public float getKnockbackResistance() {
//        return this.knockbackResistance;
//    }
//}
