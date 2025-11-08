package net.nullcoil.soulscorch.block.entity.glarynx;

import net.minecraft.block.Block;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

public class GlarynxBlockShapes {
    private static final VoxelShape UP_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 12, 16);
    private static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(0, 4, 0, 16, 16, 16);
    private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0, 0, 4, 16, 16, 16);
    private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 16, 12);
    private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0, 0, 0, 12, 16, 16);
    private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(4, 0, 0, 16, 16, 16);

    public static VoxelShape getShape(Direction direction) {
        return switch (direction) {
            case UP -> UP_SHAPE;
            case DOWN -> DOWN_SHAPE;
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
        };
    }
}
