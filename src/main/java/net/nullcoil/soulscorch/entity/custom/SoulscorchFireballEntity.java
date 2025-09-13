//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.nullcoil.soulscorch.entity.custom;

import java.util.List;

import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.nullcoil.soulscorch.effect.ModEffects;
import net.nullcoil.soulscorch.entity.ModEntities;
import net.nullcoil.soulscorch.item.ModItems;

public class SoulscorchFireballEntity extends ExplosiveProjectileEntity implements FlyingItemEntity {
    public static final float DAMAGE_RANGE = 4.0F;

    public SoulscorchFireballEntity(EntityType<? extends SoulscorchFireballEntity> entityType, World world) {
        super(entityType, world);
    }


    public SoulscorchFireballEntity(World world, LivingEntity owner, Vec3d velocity) {
        super(ModEntities.SOUL_CHARGE, owner, velocity, world);
    }


    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (hitResult.getType() != Type.ENTITY || !this.isOwner(((EntityHitResult)hitResult).getEntity())) {
            if (!this.getWorld().isClient) {
                List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand((double)4.0F, (double)2.0F, (double)4.0F));
                AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(this.getWorld(), this.getX(), this.getY(), this.getZ());
                Entity entity = this.getOwner();
                if (entity instanceof LivingEntity) {
                    areaEffectCloudEntity.setOwner((LivingEntity)entity);
                }

                areaEffectCloudEntity.setParticleType(ParticleTypes.SOUL_FIRE_FLAME);
                areaEffectCloudEntity.setRadius(3.0F);
                areaEffectCloudEntity.setDuration(600);
                areaEffectCloudEntity.setRadiusGrowth((7.0F - areaEffectCloudEntity.getRadius()) / (float)areaEffectCloudEntity.getDuration());
                areaEffectCloudEntity.addEffect(new StatusEffectInstance(ModEffects.SOULSCORCH, 600, 0));
                if (!list.isEmpty()) {
                    for(LivingEntity livingEntity : list) {
                        double d = this.squaredDistanceTo(livingEntity);
                        if (d < (double)16.0F) {
                            areaEffectCloudEntity.setPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                            break;
                        }
                    }
                }

                this.getWorld().syncWorldEvent(2006, this.getBlockPos(), this.isSilent() ? -1 : 1);
                this.getWorld().spawnEntity(areaEffectCloudEntity);
                this.discard();
            }

        }
    }

    protected ParticleEffect getParticleType() {
        return ParticleTypes.SOUL_FIRE_FLAME;
    }

    protected boolean isBurning() {
        return false;
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(ModItems.SOUL_CHARGE);
    }
}
