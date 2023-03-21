package dev.felnull.miningunderworld.item;

import com.google.common.collect.Multimap;
import dev.felnull.otyacraftengine.item.StackAttributeModifierItem;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public class WeatheringArmorItem extends ArmorItem implements WeatheringItem, StackAttributeModifierItem {
    private final Function<WeatheringState, Multimap<Attribute, AttributeModifier>> attributeCache = Util.memoize(state -> weatheringToolAttribute(state, type.getSlot(), this.getDefaultAttributeModifiers(type.getSlot())));

    public WeatheringArmorItem(ArmorMaterial armorMaterial, ArmorItem.Type type, Properties properties) {
        super(armorMaterial, type, properties);
        registerWeatheringItems();
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        weatheringInventoryTick(itemStack, level, entity);
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return getWeatheringName(itemStack, super.getName(itemStack));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getStackAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        WeatheringItem.WeatheringState state;
        if (slot == this.type.getSlot() && (state = WeatheringItem.getWeatheringState(stack)) != WeatheringState.NONE)
            return attributeCache.apply(state);

        return this.getDefaultAttributeModifiers(slot);
    }
}
