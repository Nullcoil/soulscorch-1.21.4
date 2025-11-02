package net.nullcoil.soulscorch.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import net.nullcoil.soulscorch.effect.ModEffects;

public class SoulSlagBlock extends Block {
    public static final MapCodec<SoulSlagBlock> CODEC = createCodec(SoulSlagBlock::new);
    private static final int SCHEDULED_TICK_DELAY = 20;

    public MapCodec<SoulSlagBlock> getCodec() {
        return CODEC;
    }

    public SoulSlagBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.bypassesSteppingEffects() && entity instanceof LivingEntity) {
            entity.serverDamage(world.getDamageSources().hotFloor(), 2.0F);
            ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(
                    ModEffects.SOULSCORCH,
                    600, // Duration in ticks (30 seconds)
                    0,   // Amplifier
                    false, // Show ambient
                    false,  // Show particles
                    true   // Show icon
            ));
        }

        super.onSteppedOn(world, pos, state, entity);
    }

    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BubbleColumnBlock.update(world, pos.up(), state);
    }

    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (direction == Direction.UP && neighborState.isOf(Blocks.WATER)) {
            tickView.scheduleBlockTick(pos, this, 20);
        }

        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleBlockTick(pos, this, 20);
    }
}

