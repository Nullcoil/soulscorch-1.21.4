//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.nullcoil.soulscorch.screen;

import java.util.Optional;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.nullcoil.soulscorch.item.ModItems;

public class SoulBrewingStandScreenHandler extends ScreenHandler {
    static final Identifier EMPTY_BREWING_FUEL_SLOT_TEXTURE = Identifier.ofVanilla("container/slot/brewing_fuel");
    static final Identifier EMPTY_POTION_SLOT_TEXTURE = Identifier.ofVanilla("container/slot/potion");
    private static final int field_30763 = 0;
    private static final int field_30764 = 2;
    private static final int INGREDIENT_SLOT_ID = 3;
    private static final int FUEL_SLOT_ID = 4;
    private static final int BREWING_STAND_INVENTORY_SIZE = 5;
    private static final int PROPERTY_COUNT = 2;
    private static final int INVENTORY_START = 5;
    private static final int INVENTORY_END = 32;
    private static final int HOTBAR_START = 32;
    private static final int HOTBAR_END = 41;
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    private final Slot ingredientSlot;

    public SoulBrewingStandScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(5), new ArrayPropertyDelegate(2));
    }

    public SoulBrewingStandScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ModScreenHandlers.SOUL_BREWING_STAND_SCREEN_HANDLER, syncId);
        checkSize(inventory, 5);
        checkDataCount(propertyDelegate, 2);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        BrewingRecipeRegistry brewingRecipeRegistry = playerInventory.player.getWorld().getBrewingRecipeRegistry();
        this.addSlot(new PotionSlot(inventory, 0, 56, 51));
        this.addSlot(new PotionSlot(inventory, 1, 79, 58));
        this.addSlot(new PotionSlot(inventory, 2, 102, 51));
        this.ingredientSlot = this.addSlot(new IngredientSlot(brewingRecipeRegistry, inventory, 3, 79, 17));
        this.addSlot(new FuelSlot(inventory, 4, 17, 17));
        this.addProperties(propertyDelegate);
        this.addPlayerSlots(playerInventory, 8, 84);
    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = (Slot)this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if ((slot < 0 || slot > 2) && slot != 3 && slot != 4) {
                if (SoulBrewingStandScreenHandler.FuelSlot.matches(itemStack)) {
                    if (this.insertItem(itemStack2, 4, 5, false) || this.ingredientSlot.canInsert(itemStack2) && !this.insertItem(itemStack2, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.ingredientSlot.canInsert(itemStack2)) {
                    if (!this.insertItem(itemStack2, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (SoulBrewingStandScreenHandler.PotionSlot.matches(itemStack)) {
                    if (!this.insertItem(itemStack2, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slot >= 5 && slot < 32) {
                    if (!this.insertItem(itemStack2, 32, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slot >= 32 && slot < 41) {
                    if (!this.insertItem(itemStack2, 5, 32, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.insertItem(itemStack2, 5, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.insertItem(itemStack2, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }

                slot2.onQuickTransfer(itemStack2, itemStack);
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot2.onTakeItem(player, itemStack);
        }

        return itemStack;
    }

    public int getFuel() {
        return this.propertyDelegate.get(1);
    }

    public int getBrewTime() {
        return this.propertyDelegate.get(0);
    }

    static class PotionSlot extends Slot {
        public PotionSlot(Inventory inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }

        public boolean canInsert(ItemStack stack) {
            return matches(stack);
        }

        public int getMaxItemCount() {
            return 1;
        }

        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            Optional<RegistryEntry<Potion>> optional = ((PotionContentsComponent)stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT)).potion();
            if (optional.isPresent() && player instanceof ServerPlayerEntity serverPlayerEntity) {
                Criteria.BREWED_POTION.trigger(serverPlayerEntity, (RegistryEntry)optional.get());
            }

            super.onTakeItem(player, stack);
        }

        public static boolean matches(ItemStack stack) {
            return stack.isOf(Items.POTION) || stack.isOf(Items.SPLASH_POTION) || stack.isOf(Items.LINGERING_POTION) || stack.isOf(Items.GLASS_BOTTLE);
        }

        public Identifier getBackgroundSprite() {
            return SoulBrewingStandScreenHandler.EMPTY_POTION_SLOT_TEXTURE;
        }
    }

    static class IngredientSlot extends Slot {
        private final BrewingRecipeRegistry brewingRecipeRegistry;

        public IngredientSlot(BrewingRecipeRegistry brewingRecipeRegistry, Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
            this.brewingRecipeRegistry = brewingRecipeRegistry;
        }

        public boolean canInsert(ItemStack stack) {
            return this.brewingRecipeRegistry.isValidIngredient(stack);
        }
    }

    static class FuelSlot extends Slot {
        public FuelSlot(Inventory inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }

        public boolean canInsert(ItemStack stack) {
            return matches(stack);
        }

        public static boolean matches(ItemStack stack) {
            return stack.isOf(ModItems.BLAZT_POWDER);
        }

        public Identifier getBackgroundSprite() {
            return SoulBrewingStandScreenHandler.EMPTY_BREWING_FUEL_SLOT_TEXTURE;
        }
    }
}
