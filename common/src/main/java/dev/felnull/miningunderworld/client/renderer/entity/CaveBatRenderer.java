package dev.felnull.miningunderworld.client.renderer.entity;

import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.client.renderer.entity.BatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ambient.Bat;

public class CaveBatRenderer extends BatRenderer {
    private static final ResourceLocation CAVE_BAT_LOCATION = MUUtils.modLoc("textures/entity/cave_bat.png");

    public CaveBatRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Bat bat) {
        return CAVE_BAT_LOCATION;
    }
}
