package dev.felnull.miningunderworld.entity;

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

    private static <T extends Entity> RegistrySupplier<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return ENTITY_TYPES.register(name, () -> builder.build(name));
    }

    public static void init() {
        ENTITY_TYPES.register();
    }
}
