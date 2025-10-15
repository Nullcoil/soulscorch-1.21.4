package net.nullcoil.soulscorch.event;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.util.math.Box;

import net.nullcoil.soulscorch.entity.custom.SoullessEntity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static net.nullcoil.soulscorch.util.ModTags.Blocks.SOULBASED_BLOCKS;

public class SoulbreakEventHandler {
    private static final int LISTENING_RANGE = 24;
    private static final int CHAIN_RANGE = 16;

    public static void register() {
        PlayerBlockBreakEvents.AFTER.register(SoulbreakEventHandler::onBlockBreak);
    }

    private static void onBlockBreak(World world, PlayerEntity player, BlockPos pos,
                                     BlockState state, BlockEntity blockEntity) {

        if (world.isClient) return; // server-only logic

        if (!state.isIn(SOULBASED_BLOCKS)) return;

        // BFS to find all Soulless in chain
        Set<SoullessEntity> visited = new HashSet<>();

        // Step 1: find initial Soulless within LISTENING_RANGE of broken block
        Vec3d center = Vec3d.of(pos);
        Box initialBox = new Box(
                center.add(-LISTENING_RANGE,-LISTENING_RANGE,-LISTENING_RANGE),
                center.add(LISTENING_RANGE,LISTENING_RANGE,LISTENING_RANGE)
        );

        Queue<SoullessEntity> queue = new LinkedList<>(world.getEntitiesByClass(SoullessEntity.class, initialBox, e -> true));

        // Step 2: BFS chain
        while (!queue.isEmpty()) {
            SoullessEntity soulless = queue.poll();
            if (visited.contains(soulless)) continue;

            soulless.raiseActivity();
            visited.add(soulless);

            // Find nearby Soulless within CHAIN_RANGE
            Vec3d soullessCenter = Vec3d.of(soulless.getBlockPos());
            Box chainBox = new Box(
                    soullessCenter.add(-CHAIN_RANGE,-CHAIN_RANGE,-CHAIN_RANGE),
                    soullessCenter.add(CHAIN_RANGE,CHAIN_RANGE,CHAIN_RANGE)
            );

            queue.addAll(world.getEntitiesByClass(SoullessEntity.class, chainBox, e -> !visited.contains(e)));
        }
    }
}
