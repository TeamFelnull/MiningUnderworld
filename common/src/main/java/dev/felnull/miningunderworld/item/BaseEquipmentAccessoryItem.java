package dev.felnull.miningunderworld.item;

import dev.felnull.miningunderworld.integration.EquipmentAccessoryIntegration;
import dev.felnull.miningunderworld.item.accessory.EquipmentAccessoryItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * 装備できるアクセサリのベース
 */
public abstract class BaseEquipmentAccessoryItem extends Item implements EquipmentAccessoryItem {
    public BaseEquipmentAccessoryItem(Properties properties) {
        super(properties.stacksTo(1));
        initAccessoryItem();
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        super.inventoryTick(itemStack, level, entity, i, bl);

        if (entity instanceof LivingEntity livingEntity && i >= 0 && i <= 8)
            this.accessoryTick(itemStack, livingEntity);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!EquipmentAccessoryIntegration.INSTANCE.isEnable())
            return super.use(level, player, hand);

        ItemStack stack = player.getItemInHand(hand);
        if (EquipmentAccessoryIntegration.INSTANCE.equip(player, stack))
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());

        return super.use(level, player, hand);
    }
}
