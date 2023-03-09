package dev.felnull.miningunderworld.entity;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class Excreta extends Projectile {
    public Excreta(EntityType<? extends Excreta> entityType, Level level) {
        super(entityType, level);
    }

    public Excreta(Level level, LivingEntity owner) {
        this(MUEntityTypes.EXCRETA.get(), level);
        this.setOwner(owner);
        double c = owner.getBbWidth() / 2f;
        this.setPos(owner.getX() + c, owner.getY() - owner.getBbWidth() / 2f + 0.1f, owner.getZ() + c);
    }


    @Override
    public void tick() {
        super.tick();

        Vec3 move = getDeltaMovement();
        HitResult hitResult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        this.onHit(hitResult);

        double x = this.getX() + move.x;
        double y = this.getY() + move.y;
        double z = this.getZ() + move.z;

        this.updateRotation();

        float g = 0.99F;
        float h = 0.06F;
        if (this.level.getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
            this.discard();
        } else if (this.isInWaterOrBubble()) {
            this.discard();
        } else {
            this.setDeltaMovement(move.scale(g));
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -h, 0.0));
            }

            this.setPos(x, y, z);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity owner = this.getOwner();
        if (owner instanceof LivingEntity)
            entityHitResult.getEntity().hurt(DamageSource.indirectMobAttack(this, (LivingEntity) owner).setProjectile(), 1.0F);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (!this.level.isClientSide)
            this.discard();
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket) {
        super.recreateFromPacket(clientboundAddEntityPacket);
        double x = clientboundAddEntityPacket.getXa();
        double y = clientboundAddEntityPacket.getYa();
        double z = clientboundAddEntityPacket.getZa();

        this.setDeltaMovement(x, y, z);
    }

    @Override
    protected void defineSynchedData() {
    }
}
