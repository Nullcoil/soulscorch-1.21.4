//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.nullcoil.soulscorch.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.nullcoil.soulscorch.block.entity.ModBlockEntities;
import net.nullcoil.soulscorch.block.entity.SoulBrewingStandBlockEntity;
import org.jetbrains.annotations.Nullable;

public class SoulBrewingStandBlock extends BlockWithEntity {
    public static final MapCodec<SoulBrewingStandBlock> CODEC = createCodec(SoulBrewingStandBlock::new);
    public static final BooleanProperty[] BOTTLE_PROPERTIES;
    protected static final VoxelShape SHAPE;

    public MapCodec<SoulBrewingStandBlock> getCodec() {
        return CODEC;
    }

    public SoulBrewingStandBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(BOTTLE_PROPERTIES[0], false)).with(BOTTLE_PROPERTIES[1], false)).with(BOTTLE_PROPERTIES[2], false));
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SoulBrewingStandBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : validateTicker(type, ModBlockEntities.SOUL_BREWING_STAND, SoulBrewingStandBlockEntity::tick);
    }

    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity var7 = world.getBlockEntity(pos);
            if (var7 instanceof SoulBrewingStandBlockEntity) {
                SoulBrewingStandBlockEntity brewingStandBlockEntity = (SoulBrewingStandBlockEntity)var7;
                player.openHandledScreen(brewingStandBlockEntity);
                player.incrementStat(Stats.INTERACT_WITH_BREWINGSTAND);
            }
        }

        return ActionResult.SUCCESS;
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        double d = (double)pos.getX() + 0.4 + (double)random.nextFloat() * 0.2;
        double e = (double)pos.getY() + 0.7 + (double)random.nextFloat() * 0.3;
        double f = (double)pos.getZ() + 0.4 + (double)random.nextFloat() * 0.2;
        world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d, e, f, (double)0.0F, (double)0.0F, (double)0.0F);
    }

    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        ItemScatterer.onStateReplaced(state, newState, world, pos);
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{BOTTLE_PROPERTIES[0], BOTTLE_PROPERTIES[1], BOTTLE_PROPERTIES[2]});
    }

    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    static {
        BOTTLE_PROPERTIES = new BooleanProperty[]{Properties.HAS_BOTTLE_0, Properties.HAS_BOTTLE_1, Properties.HAS_BOTTLE_2};
        SHAPE = VoxelShapes.union(Block.createCuboidShape((double)1.0F, (double)0.0F, (double)1.0F, (double)15.0F, (double)2.0F, (double)15.0F), Block.createCuboidShape((double)7.0F, (double)0.0F, (double)7.0F, (double)9.0F, (double)14.0F, (double)9.0F));
    }
}
