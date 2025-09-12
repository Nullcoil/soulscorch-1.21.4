package net.nullcoil.soulscorch.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.util.shape.VoxelShape;

public class BlockUtils {

    /**
     * Returns the BlockState that the entity's feet are actually touching (collision-aware).
     * Returns null if none found or world/entity invalid.
     */
    public static BlockState getBlockUnderEntity(Entity entity) {
        if (entity == null || entity.getWorld() == null) return null;
        World world = entity.getWorld();

        // A tiny box slice at the bottom of the entity's bounding box, touching the entity's feet.
        // Using a very small epsilon so we still pick up slabs, carpets, etc.
        double minX = entity.getBoundingBox().minX;
        double maxX = entity.getBoundingBox().maxX;
        double minZ = entity.getBoundingBox().minZ;
        double maxZ = entity.getBoundingBox().maxZ;
        double topY = entity.getBoundingBox().minY; // feet y
        double bottomY = topY - 0.125D; // 1/8 block below feet

        Box feetBox = new Box(minX, bottomY, minZ, maxX, topY, maxZ);

        // iterate candidates inside that slice
        int x0 = (int) Math.floor(feetBox.minX);
        int y0 = (int) Math.floor(feetBox.minY);
        int z0 = (int) Math.floor(feetBox.minZ);
        int x1 = (int) Math.floor(feetBox.maxX);
        int y1 = (int) Math.floor(feetBox.maxY);
        int z1 = (int) Math.floor(feetBox.maxZ);

        for (int x = x0; x <= x1; x++) {
            for (int y = y0; y <= y1; y++) {
                for (int z = z0; z <= z1; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (!world.isInBuildLimit(pos)) continue;
                    BlockState state = world.getBlockState(pos);
                    if (state.isAir()) continue;

                    // get collision shape and test intersection
                    VoxelShape shape = state.getCollisionShape(world, pos);
                    if (shape == VoxelShapes.empty()) {
                        // Some blocks (like non-solid decorative blocks) may have empty collision - skip
                        continue;
                    }

                    // offset shape to world position and test intersects feetBox
                    if (!shape.isEmpty() && shape.offset(pos.getX(), pos.getY(), pos.getZ()).getBoundingBoxes().stream()
                            .anyMatch(b -> b.intersects(feetBox))) {
                        return state;
                    }
                }
            }
        }

        return null;
    }
}
