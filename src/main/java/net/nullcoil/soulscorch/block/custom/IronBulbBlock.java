package net.nullcoil.soulscorch.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BulbBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

public class IronBulbBlock extends BulbBlock {
    public IronBulbBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        if (!world.isClient()) {
            boolean powered = world.isReceivingRedstonePower(pos);
            boolean wasPowered = state.get(POWERED);

            // Detect a change in redstone power
            if (powered != wasPowered) {
                // Schedule a delayed tick (1 tick = instant, 20 = debug)
                world.scheduleBlockTick(pos, this, 2);
                // Temporarily store the new powered state
                world.setBlockState(pos, state.with(POWERED, powered), 2);
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean powered = state.get(POWERED);
        boolean lit = state.get(LIT);

        // Only toggle when power is ON
        if (powered) {
            BlockState newState = state.with(LIT, !lit);
            world.setBlockState(pos, newState, 3);
            world.playSound(null,
                    pos,
                    newState.get(LIT)
                            ? SoundEvents.BLOCK_COPPER_BULB_TURN_ON
                            : SoundEvents.BLOCK_COPPER_BULB_TURN_OFF,
                    SoundCategory.BLOCKS,
                    1.0F,
                    1.0F
            );
        }
    }
}
