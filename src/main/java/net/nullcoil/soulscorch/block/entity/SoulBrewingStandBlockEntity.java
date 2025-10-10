package net.nullcoil.soulscorch.block.entity;

import java.util.Arrays;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.nullcoil.soulscorch.block.SoulBrewingStandBlock;
import net.nullcoil.soulscorch.item.ModItems;
import org.jetbrains.annotations.Nullable;

public class SoulBrewingStandBlockEntity extends LockableContainerBlockEntity implements SidedInventory {
    private static final int INPUT_SLOT_INDEX = 3;
    private static final int FUEL_SLOT_INDEX = 4;
    private static final int[] TOP_SLOTS = new int[]{3};
    private static final int[] BOTTOM_SLOTS = new int[]{0, 1, 2, 3};
    private static final int[] SIDE_SLOTS = new int[]{0, 1, 2, 4};
    public static final int MAX_FUEL_USES = 20;
    public static final int BREW_TIME_PROPERTY_INDEX = 0;
    public static final int FUEL_PROPERTY_INDEX = 1;
    public static final int PROPERTY_COUNT = 2;
    private DefaultedList<ItemStack> inventory;
    int brewTime;
    private boolean[] slotsEmptyLastTick;
    private Item itemBrewing;
    int fuel;
    protected final PropertyDelegate propertyDelegate;

    public SoulBrewingStandBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SOUL_BREWING_STAND, pos, state);
        this.inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                int var10000;
                switch (index) {
                    case 0 -> var10000 = SoulBrewingStandBlockEntity.this.brewTime;
                    case 1 -> var10000 = SoulBrewingStandBlockEntity.this.fuel;
                    default -> var10000 = 0;
                }

                return var10000;
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> SoulBrewingStandBlockEntity.this.brewTime = value;
                    case 1 -> SoulBrewingStandBlockEntity.this.fuel = value;
                }

            }

            public int size() {
                return 2;
            }
        };
    }

    protected Text getContainerName() {
        return Text.translatable("container.brewing");
    }

    public int size() {
        return this.inventory.size();
    }

    protected DefaultedList<ItemStack> getHeldStacks() {
        return this.inventory;
    }

    protected void setHeldStacks(DefaultedList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    public static void tick(World world, BlockPos pos, BlockState state, SoulBrewingStandBlockEntity blockEntity) {
        ItemStack itemStack = (ItemStack)blockEntity.inventory.get(4);
        if (blockEntity.fuel <= 0 && itemStack.isOf(ModItems.BLAZT_POWDER)) {
            blockEntity.fuel = 20;
            itemStack.decrement(1);
            markDirty(world, pos, state);
        }

        boolean bl = canCraft(world.getBrewingRecipeRegistry(), blockEntity.inventory);
        boolean bl2 = blockEntity.brewTime > 0;
        ItemStack itemStack2 = (ItemStack)blockEntity.inventory.get(3);
        if (bl2) {
            --blockEntity.brewTime;
            boolean bl3 = blockEntity.brewTime == 0;
            if (bl3 && bl) {
                craft(world, pos, blockEntity.inventory);
            } else if (!bl || !itemStack2.isOf(blockEntity.itemBrewing)) {
                blockEntity.brewTime = 0;
            }

            markDirty(world, pos, state);
        } else if (bl && blockEntity.fuel > 0) {
            --blockEntity.fuel;
            blockEntity.brewTime = 200;
            blockEntity.itemBrewing = itemStack2.getItem();
            markDirty(world, pos, state);
        }

        boolean[] bls = blockEntity.getSlotsEmpty();
        if (!Arrays.equals(bls, blockEntity.slotsEmptyLastTick)) {
            blockEntity.slotsEmptyLastTick = bls;
            BlockState blockState = state;
            if (!(state.getBlock() instanceof SoulBrewingStandBlock)) {
                return;
            }

            for(int i = 0; i < SoulBrewingStandBlock.BOTTLE_PROPERTIES.length; ++i) {
                blockState = (BlockState)blockState.with(SoulBrewingStandBlock.BOTTLE_PROPERTIES[i], bls[i]);
            }

            world.setBlockState(pos, blockState, 2);
        }

    }

    private boolean[] getSlotsEmpty() {
        boolean[] bls = new boolean[3];

        for(int i = 0; i < 3; ++i) {
            if (!((ItemStack)this.inventory.get(i)).isEmpty()) {
                bls[i] = true;
            }
        }

        return bls;
    }

    private static boolean canCraft(BrewingRecipeRegistry brewingRecipeRegistry, DefaultedList<ItemStack> slots) {
        ItemStack itemStack = (ItemStack)slots.get(3);
        if (itemStack.isEmpty()) {
            return false;
        } else if (!brewingRecipeRegistry.isValidIngredient(itemStack)) {
            return false;
        } else {
            for(int i = 0; i < 3; ++i) {
                ItemStack itemStack2 = (ItemStack)slots.get(i);
                if (!itemStack2.isEmpty() && brewingRecipeRegistry.hasRecipe(itemStack2, itemStack)) {
                    return true;
                }
            }

            return false;
        }
    }

    private static void craft(World world, BlockPos pos, DefaultedList<ItemStack> slots) {
        ItemStack itemStack = (ItemStack)slots.get(3);
        BrewingRecipeRegistry brewingRecipeRegistry = world.getBrewingRecipeRegistry();

        for(int i = 0; i < 3; ++i) {
            slots.set(i, brewingRecipeRegistry.craft(itemStack, (ItemStack)slots.get(i)));
        }

        itemStack.decrement(1);
        ItemStack itemStack2 = itemStack.getItem().getRecipeRemainder();
        if (!itemStack2.isEmpty()) {
            if (itemStack.isEmpty()) {
                itemStack = itemStack2;
            } else {
                ItemScatterer.spawn(world, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), itemStack2);
            }
        }

        slots.set(3, itemStack);
        world.syncWorldEvent(1035, pos, 0);
    }

    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory, registries);
        this.brewTime = nbt.getShort("BrewTime");
        if (this.brewTime > 0) {
            this.itemBrewing = ((ItemStack)this.inventory.get(3)).getItem();
        }

        this.fuel = nbt.getByte("Fuel");
    }

    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        nbt.putShort("BrewTime", (short)this.brewTime);
        Inventories.writeNbt(nbt, this.inventory, registries);
        nbt.putByte("Fuel", (byte)this.fuel);
    }

    public boolean isValid(int slot, ItemStack stack) {
        if (slot == 3) {
            BrewingRecipeRegistry brewingRecipeRegistry = this.world != null ? this.world.getBrewingRecipeRegistry() : BrewingRecipeRegistry.EMPTY;
            return brewingRecipeRegistry.isValidIngredient(stack);
        } else if (slot == 4) {
            return stack.isOf(ModItems.BLAZT_POWDER);
        } else {
            return (stack.isOf(Items.POTION) || stack.isOf(Items.SPLASH_POTION) || stack.isOf(Items.LINGERING_POTION) || stack.isOf(Items.GLASS_BOTTLE)) && this.getStack(slot).isEmpty();
        }
    }

    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.UP) {
            return TOP_SLOTS;
        } else {
            return side == Direction.DOWN ? BOTTOM_SLOTS : SIDE_SLOTS;
        }
    }

    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }

    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == 3 ? stack.isOf(Items.GLASS_BOTTLE) : true;
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new BrewingStandScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}
