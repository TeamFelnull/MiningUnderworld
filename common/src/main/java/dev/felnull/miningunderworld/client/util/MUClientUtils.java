package dev.felnull.miningunderworld.client.util;

import dev.felnull.miningunderworld.fluid.MUFluidTags;
import dev.felnull.miningunderworld.mixin.client.CameraNearPlaneAccessor;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

/**
 * クライアント側のユーティリティ
 */
public final class MUClientUtils {
    private static final Minecraft mc = Minecraft.getInstance();

    /**
     * カメラがタールの中にあるかどうか
     *
     * @param camera カメラ
     * @return 結果
     */
    public static boolean isCameraInTar(Camera camera) {
        Camera.NearPlane nearPlane = camera.getNearPlane();
        List<Vec3> list = Arrays.asList(((CameraNearPlaneAccessor) nearPlane).getForward(), nearPlane.getTopLeft(), nearPlane.getTopRight(), nearPlane.getBottomLeft(), nearPlane.getBottomRight());

        for (Vec3 panel : list) {
            Vec3 pos = camera.getPosition().add(panel);
            BlockPos blockPos = new BlockPos(pos);
            FluidState state = mc.level.getFluidState(blockPos);
            if (state.is(MUFluidTags.TAR)) {
                if (pos.y <= (double) (state.getHeight(mc.level, blockPos) + (float) blockPos.getY())) {
                    return true;
                }
            }
        }

        return false;
    }
}
