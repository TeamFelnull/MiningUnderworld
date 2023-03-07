package dev.felnull.miningunderworld.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.NearPlane.class)
public interface CameraNearPlaneAccessor {
    @Accessor
    Vec3 getForward();
}
