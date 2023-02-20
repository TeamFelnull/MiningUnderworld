package dev.felnull.miningunderworld.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class WeatheringAxeItem extends AxeItem implements WeatheringItem {
    public WeatheringAxeItem(Tier tier, float baseDamage, float attackSpeed, Properties properties) {
        super(tier, baseDamage, attackSpeed, properties);
        registerWeatheringItems();
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotId, boolean selected) {
        weatheringInventoryTick(itemStack, level, entity);
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return getWeatheringName(itemStack, super.getName(itemStack));
    }
}
