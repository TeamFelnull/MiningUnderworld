package dev.felnull.miningunderworld.world;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.felnull.miningunderworld.item.MUArmorMaterials;
import dev.felnull.miningunderworld.item.MUItems;
import dev.felnull.miningunderworld.item.WeatheringItem;
import dev.felnull.miningunderworld.util.MUAccessoryUtils;
import dev.felnull.miningunderworld.util.MUUtils;
import dev.felnull.otyacraftengine.event.MoreEntityEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class CommonHandler {
    public static void init() {
        EntityEvent.LIVING_HURT.register(CommonHandler::onHurt);
        MoreEntityEvent.LIVING_ENTITY_TICK.register(CommonHandler::onLivingEntityTick);
    }

    private static EventResult onHurt(LivingEntity entity, DamageSource source, float amount) {

        //雷のダメージを受けたときに装備してる銅ツールの錆がはがれるように
        if (source.is(DamageTypes.LIGHTNING_BOLT)) {
            MUUtils.getAllHaveItem(entity)
                    .filter(stack -> stack.getItem() instanceof WeatheringItem && !WeatheringItem.isWaxed(stack))
                    .forEach(stack -> WeatheringItem.setWeatheringState(stack, WeatheringItem.WeatheringState.NONE));
        }


        return EventResult.pass();
    }

    private static EventResult onLivingEntityTick(@NotNull LivingEntity livingEntity) {

        if (MUUtils.isFullArmor(livingEntity, MUArmorMaterials.REDSTONE) || MUAccessoryUtils.isEquipped(livingEntity, MUItems.REDSTONE_BELT.get()))
            DynamicSignal.signaledLivingTick(livingEntity);

        return EventResult.pass();
    }
}
