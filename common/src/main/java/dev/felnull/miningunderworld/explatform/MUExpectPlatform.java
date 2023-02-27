package dev.felnull.miningunderworld.explatform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.Entity;

public class MUExpectPlatform {
    @ExpectPlatform
    public static boolean isInTar(Entity entity) {
        throw new AssertionError();
    }
}
