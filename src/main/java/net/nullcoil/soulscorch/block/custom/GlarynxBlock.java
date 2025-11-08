package net.nullcoil.soulscorch.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.nullcoil.soulscorch.block.ModBlockEntities;
import net.nullcoil.soulscorch.block.entity.GlarynxBlockEntity;
import net.nullcoil.soulscorch.block.entity.glarynx.GlarynxState;
import org.jetbrains.annotations.Nullable;

public class GlarynxBlock extends BlockWithEntity {
    public static final MapCodec<GlarynxBlock> CODEC = createCodec(GlarynxBlock::new);
    public static final EnumProperty<GlarynxState> STATE = EnumProperty.of("state", GlarynxState.class);

    public GlarynxBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    public static boolean isInactive(BlockState cachedState) {
        return cachedState.contains(STATE) && cachedState.get(STATE) == GlarynxState.COOLDOWN;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
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
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }
}
