package dev.felnull.miningunderworld.entity;

import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.miningunderworld.MiningUnderworld;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class MUEntityTypes {
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(MiningUnderworld.MODID, Registries.ENTITY_TYPE);
    public static final RegistrySupplier<EntityType<PrimedMiningTnt>> MINING_TNT = register("mining_tnt", EntityType.Builder.of((EntityType.EntityFactory<PrimedMiningTnt>) PrimedMiningTnt::new, MobCategory.MISC).fireImmune().sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(10));
    public static final RegistrySupplier<EntityType<CaveBat>> CAVE_BAT = register("cave_bat", EntityType.Builder.of(CaveBat::new, MobCategory.CREATURE).sized(0.5F, 0.9F).clientTrackingRange(5));
    public static final RegistrySupplier<EntityType<Excreta>> EXCRETA = register("excreta", EntityType.Builder.of((EntityType.EntityFactory<Excreta>) Excreta::new, MobCategory.MISC).sized(0.1F, 0.1F).clientTrackingRange(4).updateInterval(10));
    public static final RegistrySupplier<EntityType<StrictFallingBlockEntity>> STRICT_FALLING_BLOCK = register("strict_falling_block", EntityType.Builder.of(StrictFallingBlockEntity::new, MobCategory.MISC).sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(20));

    private static <T extends Entity> RegistrySupplier<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return ENTITY_TYPES.register(name, () -> builder.build(name));
    }

    public static void init() {
        ENTITY_TYPES.register();

        EntityAttributeRegistry.register(CAVE_BAT, CaveBat::createAttributes);
    }
}
