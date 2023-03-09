package dev.felnull.miningunderworld.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.felnull.miningunderworld.client.model.MUModelLayers;
import dev.felnull.miningunderworld.client.model.entity.ExcretaModel;
import dev.felnull.miningunderworld.entity.Excreta;
import dev.felnull.miningunderworld.util.MUUtils;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ExcretaRenderer extends EntityRenderer<Excreta> {
    private static final ResourceLocation EXCRETA_LOCATION = MUUtils.modLoc("textures/entity/excreta.png");
    private final ExcretaModel model;

    protected ExcretaRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ExcretaModel(context.bakeLayer(MUModelLayers.EXCRETA));
    }

    @Override
    public void render(Excreta entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.06F, 0.0F);
        OERenderUtils.poseRotateY(poseStack, Mth.lerp(g, entity.yRotO, entity.getYRot()) - 90.0F);
        OERenderUtils.poseRotateZ(poseStack, Mth.lerp(g, entity.xRotO, entity.getXRot()));

        this.model.setupAnim(entity, g, 0.0F, 0, 0.0F, 0.0F);

        VertexConsumer vc = multiBufferSource.getBuffer(this.model.renderType(EXCRETA_LOCATION));
        this.model.renderToBuffer(poseStack, vc, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, f, g, poseStack, multiBufferSource, i);
    }

    @Override
    public ResourceLocation getTextureLocation(Excreta entity) {
        return EXCRETA_LOCATION;
    }
}
