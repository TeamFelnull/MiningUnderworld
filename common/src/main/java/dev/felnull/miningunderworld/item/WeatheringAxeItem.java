package dev.felnull.miningunderworld.item;

import com.google.common.collect.Multimap;
import dev.felnull.otyacraftengine.item.StackAttributeModifierItem;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class WeatheringAxeItem extends AxeItem implements WeatheringItem, StackAttributeModifierItem {
    private final Function<WeatheringState, Multimap<Attribute, AttributeModifier>> attributeCache = Util.memoize(state -> weatheringToolAttribute(state, EquipmentSlot.MAINHAND, this.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND)));

    public WeatheringAxeItem(Tier tier, float baseDamage, float attackSpeed, Properties properties) {
        super(tier, baseDamage, attackSpeed, properties);
        registerWeatheringItems();
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        return this.weatheringDestroySpeed(itemStack, blockState, super.getDestroySpeed(itemStack, blockState));
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotId, boolean selected) {
        weatheringInventoryTick(itemStack, level, entity);
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return getWeatheringName(itemStack, super.getName(itemStack));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getStackAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        WeatheringItem.WeatheringState state;
        if (slot == EquipmentSlot.MAINHAND && (state = WeatheringItem.getWeatheringState(stack)) != WeatheringState.NONE)
            return attributeCache.apply(state);

        return this.getDefaultAttributeModifiers(slot);
    }
}
