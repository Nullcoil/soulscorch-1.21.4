package net.nullcoil.soulscorch.event;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.nullcoil.soulscorch.util.ModTags.Blocks.SOULBASED_BLOCKS;

public class SoulbreakEventHandler {
    private static final double LISTENING_RANGE = 24.0;

    public static void register() {
        // Register once during mod initialization
        PlayerBlockBreakEvents.AFTER.register(SoulbreakEventHandler::onBlockBreak);
    }

    private static void onBlockBreak(World world, PlayerEntity player, BlockPos pos,
                                     BlockState state, BlockEntity blockEntity) {

        if (world.isClient) return; // server-only logic

        // Only care about soul-based blocks
        if (!state.isIn(SOULBASED_BLOCKS)) return;

        // Print debug
        System.out.println("=== SOULBREAK EVENT TRIGGERED ===");
        System.out.println("Player: " + player.getName().getString());
        System.out.println("Block: " + state.getBlock());
        System.out.println("Position: " + pos);
        System.out.println("World: " + (world.isClient() ? "CLIENT" : "SERVER"));
        System.out.println("================================");
    }
}
