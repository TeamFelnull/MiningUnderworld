package dev.felnull.miningunderworld.client.model;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.felnull.miningunderworld.client.model.entity.ExcretaModel;
import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class MUModelLayers {
    public static final ModelLayerLocation EXCRETA = create("excreta");

    private static ModelLayerLocation create(String name) {
        return new ModelLayerLocation(MUUtils.modLoc(name), "main");
    }

    public static void init() {
        EntityModelLayerRegistry.register(EXCRETA, ExcretaModel::createBodyLayer);
    }
}
