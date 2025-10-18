package net.nullcoil.soulscorch.mixin;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import net.nullcoil.soulscorch.Soulscorch;
import net.nullcoil.soulscorch.entity.ModEntities;
import net.nullcoil.soulscorch.entity.custom.SoulborneCatEntity;
import net.nullcoil.soulscorch.util.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CatEntity.class)
public abstract class CatEntityMixin extends TameableEntity {
    @Shadow
    protected abstract void onTamedChanged();

    protected CatEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.onTamedChanged();
    }

    @Inject(
            method = "createChild(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/PassiveEntity;)Lnet/minecraft/entity/passive/CatEntity;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void soulscorch$replaceChild(ServerWorld world, PassiveEntity other, CallbackInfoReturnable<CatEntity> cir) {
        if(!(other instanceof CatEntity otherCat)) return;

        if(this.isTamed() && otherCat.isTamed() && world.getBiome(this.getBlockPos()).isIn(ModTags.Biomes.SOUL_BIOMES)) {
            SoulborneCatEntity soulCat = ModEntities.SOULBORNE_CAT.create(world, SpawnReason.BREEDING);
            if(soulCat != null) {
                soulCat.setTamed(true, true);
                soulCat.setOwnerUuid(this.getOwnerUuid());

                if (soulCat.getOwner() instanceof ServerPlayerEntity owner) {
                    AdvancementEntry advancement = world.getServer().getAdvancementLoader()
                            .get(Identifier.of(Soulscorch.MOD_ID, "soulcat_breeder"));
                    if (advancement != null) {
                        owner.getAdvancementTracker().grantCriterion(advancement, "soulcat_breed");
                    }
                }
                cir.setReturnValue(soulCat);
                cir.cancel();
            }
            if(soulCat == null) System.out.println("Failed to create Soulborne Cat");
        }
    }
}
