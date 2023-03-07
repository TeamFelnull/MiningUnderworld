package dev.felnull.miningunderworld.client.handler;

import com.mojang.blaze3d.shaders.FogShape;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.felnull.miningunderworld.client.util.MUClientUtils;
import dev.felnull.miningunderworld.world.DynamicSignalLevel;
import dev.felnull.otyacraftengine.client.event.ClientCameraEvent;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.level.material.FogType;

public class ClientHandler {
    public static void init() {
        ClientTickEvent.CLIENT_LEVEL_POST.register(ClientHandler::onClientLevelPostTick);
        ClientCameraEvent.RENDER_FOG.register(ClientHandler::onRenderFog);
        ClientCameraEvent.COMPUTE_FOG_COLOR.register(ClientHandler::onComputeFogColor);
    }

    private static void onClientLevelPostTick(ClientLevel level) {
        ((DynamicSignalLevel) level).getDynamicSignal().postTick();
    }

    private static EventResult onRenderFog(Camera camera, FogRenderer.FogMode fogMode, FogType fogType, float startDistance, float endDistance, FogShape fogShape, double delta, ClientCameraEvent.RenderFogSetter setter) {
        if (MUClientUtils.isTarInCamera(camera)) {
            setter.setStartDistance(0.25F);
            setter.setEndDistance(1.0F);
            setter.setFogShape(FogShape.SPHERE);
            return EventResult.interruptFalse();
        }
        return EventResult.pass();
    }

    private static EventResult onComputeFogColor(Camera camera, float red, float green, float blue, double delta, ClientCameraEvent.FogColorSetter fogColorSetter) {
        if (MUClientUtils.isTarInCamera(camera)) {
            float gray = 0.1f;
            fogColorSetter.setRed(gray);
            fogColorSetter.setGreen(gray);
            fogColorSetter.setBlue(gray);
            return EventResult.interruptTrue();
        }
        return EventResult.pass();
    }
}
