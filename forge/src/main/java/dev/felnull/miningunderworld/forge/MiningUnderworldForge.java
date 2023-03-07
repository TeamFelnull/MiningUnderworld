package dev.felnull.miningunderworld.forge;

import com.google.common.collect.ImmutableList;
import dev.architectury.platform.forge.EventBuses;
import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.integration.EquipmentAccessoryIntegration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod(MiningUnderworld.MODID)
public class MiningUnderworldForge {
    public MiningUnderworldForge() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(MiningUnderworld.MODID, eventBus);
        MiningUnderworld.init();
        eventBus.addListener(this::enqueue);
    }

    private void enqueue(final InterModEnqueueEvent event) {

        /*
        Curiosの装備品タイプを追加
        https://www.curseforge.com/minecraft/mc-mods/curios
        https://github.com/TheIllusiveC4/Curios/wiki/How-to-Use:-Developers
        https://github.com/TheIllusiveC4/Curios/blob/1.19.3/src/test/java/top/theillusivec4/curiostest/CuriosTest.java
         */

        if (EquipmentAccessoryIntegration.INSTANCE.isEnable()) {
            InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
                    () -> ImmutableList.of(
                            SlotTypePreset.RING.getMessageBuilder().size(2).build(),
                            SlotTypePreset.BELT.getMessageBuilder().size(1).build()));
        }
    }
}
