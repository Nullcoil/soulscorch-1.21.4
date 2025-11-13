package net.nullcoil.soulscorch.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import net.nullcoil.soulscorch.enums.NurvisPacketType;
import net.nullcoil.soulscorch.nurvis.blockentity.NurvisConduitBlockEntity;
import net.nullcoil.soulscorch.nurvis.blockentity.NurvisPacketHolderBlockEntity;
import net.nullcoil.soulscorch.nurvis.packet.PacketInstruction;
import org.jetbrains.annotations.Nullable;

public class NurvisConduitBlock extends BlockWithEntity {
    public static final MapCodec<NurvisConduitBlock> CODEC = createCodec(NurvisConduitBlock::new);
    public static final EnumProperty<Direction> FACING = Properties.FACING;
    public static final BooleanProperty POWERED;

    public NurvisConduitBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(POWERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
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

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new NurvisConduitBlockEntity(pos, state);
    }

    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        if (!world.isClient) {
            boolean bl = (Boolean)state.get(POWERED);
            if (bl != world.isReceivingRedstonePower(pos)) {
                if (bl) {
                    BlockEntity entity = world.getBlockEntity(pos);
                    if(entity instanceof NurvisPacketHolderBlockEntity holder) {
                        holder.insertPacket(NurvisPacketType.DECOY
                                .create(world)
                                .overrideOnArrive(p -> new PacketInstruction(() -> {
                                    holder.pushPacket((ServerWorld) world);
                                }, 4))
                                .build());
                        holder.iteratePackets();
                    }
                } else {
                    world.setBlockState(pos, (BlockState)state.with(POWERED, bl), 3);
                }
            }

        }
    }

    static {
        POWERED = RedstoneTorchBlock.LIT;
    }
}
