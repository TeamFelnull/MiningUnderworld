package dev.felnull.miningunderworld.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class WeatheringPickaxeItem extends PickaxeItem implements WeatheringItem {
    public WeatheringPickaxeItem(Tier tier, int baseDamage, float attackSpeed, Properties properties) {
        super(tier, baseDamage, attackSpeed, properties);
        registerWeatheringItems();
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotId, boolean selected) {
        weatheringInventoryTick(itemStack, level, entity);
    }
}
