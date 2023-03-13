package dev.felnull.miningunderworld.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public class EntityMixin {
   /* @Shadow
    public Level level;

    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tryCheckInsideBlocks()V", ordinal = 0))
    public void move(MoverType moverType, Vec3 vec3, CallbackInfo ci) {
        Entity tis = (Entity) (Object) this;

        BlockPos blockPos = tis.getOnPosLegacy();
        BlockState blockState = this.level.getBlockState(blockPos);

        if (!level.isClientSide && blockState.is(MUBlocks.MOST_LIKELY_COLLAPSING_BLOCK.get()) && (tis.xOld != tis.getX() || tis.zOld != tis.getZ())) {
            System.out.println("ｳｧｧ!!ｵﾚﾓｲｯﾁｬｳｩｩｩ!!!ｳｳｳｳｳｳｳｳｳｩｩｩｩｩｩｩｩｳｳｳｳｳｳｳｳ!ｲｨｨｲｨｨｨｲｲｲｨｲｲｲｲ");
        }
    }*/
}
