package net.nullcoil.soulscorch.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.nullcoil.soulscorch.block.ModBlockEntities;
import net.nullcoil.soulscorch.nurvis.blockentity.organ.GlarynxBlockEntity;
import net.nullcoil.soulscorch.nurvis.blockentity.organ.GlarynxBlockShapes;
import net.nullcoil.soulscorch.enums.GlarynxState;
import org.jetbrains.annotations.Nullable;

public class GlarynxBlock extends BlockWithEntity {
    public static final MapCodec<GlarynxBlock> CODEC = createCodec(GlarynxBlock::new);
    public static final EnumProperty<GlarynxState> STATE = EnumProperty.of("state", GlarynxState.class);
    public static final EnumProperty<Direction> FACING = Properties.FACING;

    public GlarynxBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(STATE, GlarynxState.COOLDOWN));
    }

    public static boolean isInactive(BlockState state) {
        return state.contains(STATE) && state.get(STATE) == GlarynxState.COOLDOWN;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, STATE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getSide());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GlarynxBlockEntity(pos, state);
    }

    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(
            BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.GLARYNX, (world1, pos, state1, be) -> {
            if (!world1.isClient()) {
                GlarynxBlockEntity.tick((ServerWorld) world1, pos, state1, (GlarynxBlockEntity) be);
            }
        });
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return GlarynxBlockShapes.getShape(state.get(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return GlarynxBlockShapes.getShape(state.get(FACING));
    }
}
