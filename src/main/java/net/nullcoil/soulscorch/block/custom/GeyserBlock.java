package net.nullcoil.soulscorch.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.nullcoil.soulscorch.effect.ModEffects;

public class GeyserBlock extends Block {
    public GeyserBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);
        double strength = 3.25d;

        if(entity instanceof LivingEntity mob) {
            mob.addVelocity(0d, 0.4 * strength, 0d); // Goes up 8 blocks
            mob.serverDamage(world.getDamageSources().hotFloor(), 1f);
            mob.addStatusEffect(new StatusEffectInstance(ModEffects.SOULSCORCH, 300, 0, false, false, true));
        }
    }
}
