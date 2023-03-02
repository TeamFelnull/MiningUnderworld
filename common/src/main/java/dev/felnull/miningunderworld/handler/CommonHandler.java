package dev.felnull.miningunderworld.handler;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.felnull.miningunderworld.item.MUArmorMaterials;
import dev.felnull.miningunderworld.item.WeatheringItem;
import dev.felnull.miningunderworld.util.MUUtils;
import dev.felnull.miningunderworld.world.DynamicSignalLevel;
import dev.felnull.otyacraftengine.event.MoreEntityEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommonHandler {
    public static void init() {
        EntityEvent.LIVING_HURT.register(CommonHandler::onHurt);
        MoreEntityEvent.LIVING_ENTITY_TICK.register(CommonHandler::onLivingEntityTick);
    }

    private static EventResult onHurt(LivingEntity entity, DamageSource source, float amount) {

        //雷のダメージを受けたときに装備してる銅ツールの錆がはがれるように
        if (source == DamageSource.LIGHTNING_BOLT) {
            List<ItemStack> stacks = MUUtils.getAllHaveItem(entity);
            for (ItemStack stack : stacks) {
                if (stack.getItem() instanceof WeatheringItem && !WeatheringItem.isWaxed(stack))
                    WeatheringItem.setWeatheringState(stack, WeatheringItem.WeatheringState.NONE);
            }
        }


        return EventResult.pass();
    }

    private static EventResult onLivingEntityTick(@NotNull LivingEntity livingEntity) {

        if (MUUtils.isFullArmor(livingEntity, MUArmorMaterials.REDSTONE))
            ((DynamicSignalLevel) livingEntity.level).getDynamicSignal().putSignalBox(livingEntity.getBoundingBox());

        return EventResult.pass();
    }
}
