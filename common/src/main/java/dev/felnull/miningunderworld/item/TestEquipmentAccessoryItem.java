package dev.felnull.miningunderworld.item;

import dev.felnull.miningunderworld.item.accessory.AccessoryType;
import dev.felnull.miningunderworld.item.accessory.EquipmentAccessoryItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TestEquipmentAccessoryItem extends Item implements EquipmentAccessoryItem {
    public TestEquipmentAccessoryItem(Properties properties) {
        super(properties.stacksTo(1));
        initAccessoryItem();
    }

    @Override
    public @NotNull AccessoryType getAccessoryType() {
        return AccessoryType.RING;
    }

    @Override
    public void accessoryTick(ItemStack stack, LivingEntity livingEntity) {
        System.out.println("ｳｧｧ!!ｵﾚﾓｲｯﾁｬｳｩｩｩ!!!ｳｳｳｳｳｳｳｳｳｩｩｩｩｩｩｩｩｳｳｳｳｳｳｳｳ!ｲｨｨｲｨｨｨｲｲｲｨｲｲｲｲ");
    }
}
