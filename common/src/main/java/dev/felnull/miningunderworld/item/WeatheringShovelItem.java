package dev.felnull.miningunderworld.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class WeatheringShovelItem extends ShovelItem implements WeatheringItem {
    public WeatheringShovelItem(Tier tier, float baseDamage, float attackSpeed, Properties properties) {
        super(tier, baseDamage, attackSpeed, properties);
        registerWeatheringItems();
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotId, boolean selected) {
        weatheringInventoryTick(itemStack, level, entity);
    }
}
