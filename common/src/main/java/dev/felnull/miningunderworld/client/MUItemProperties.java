package dev.felnull.miningunderworld.client;

import dev.architectury.registry.item.ItemPropertiesRegistry;
import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.item.WeatheringItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class MUItemProperties {
    public static final ResourceLocation OXIDIZING = new ResourceLocation(MiningUnderworld.MODID, "oxidizing");

    public static void init() {
        WeatheringItem.OXIDIZING_ITEMS.forEach(it -> ItemPropertiesRegistry.register(it, OXIDIZING, MUItemProperties::oxidizing));
    }

    private static float oxidizing(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
        return (float) WeatheringItem.getWeatheringState(itemStack).ordinal() / 10f + 0.05f;
    }
}
