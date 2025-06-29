package net.IneiTsuki.regen.block.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * A simple {@link SidedInventory} implementation with default logic.
 *
 * <p>Use {@link Inventories#writeNbt(NbtCompound, DefaultedList, RegistryWrapper.WrapperLookup)}
 * and {@link Inventories#readNbt(NbtCompound, DefaultedList, RegistryWrapper.WrapperLookup)}
 * for reading/writing item data.
 *
 * <p>License: <a href="https://creativecommons.org/publicdomain/zero/1.0/">CC0</a>
 */
public interface ImplementedInventory extends SidedInventory {

    /** Returns the backing item list. Must always return the same instance. */
    DefaultedList<ItemStack> getItems();

    /** Creates an inventory backed by the given item list. */
    static ImplementedInventory of(DefaultedList<ItemStack> items) {
        return () -> items;
    }

    /** Creates a new inventory of the given size filled with empty item stacks. */
    static ImplementedInventory ofSize(int size) {
        return of(DefaultedList.ofSize(size, ItemStack.EMPTY));
    }

    // --------------------------
    // SidedInventory Defaults
    // --------------------------

    @Override
    default int[] getAvailableSlots(Direction side) {
        int[] slots = new int[getItems().size()];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = i;
        }
        return slots;
    }

    @Override
    default boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        return true;
    }

    @Override
    default boolean canExtract(int slot, ItemStack stack, Direction side) {
        return true;
    }

    // --------------------------
    // Inventory Defaults
    // --------------------------

    @Override
    default int size() {
        return getItems().size();
    }

    @Override
    default boolean isEmpty() {
        for (ItemStack stack : getItems()) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    default ItemStack getStack(int slot) {
        return getItems().get(slot);
    }

    @Override
    default ItemStack removeStack(int slot, int count) {
        ItemStack result = Inventories.splitStack(getItems(), slot, count);
        if (!result.isEmpty()) markDirty();
        return result;
    }

    @Override
    default ItemStack removeStack(int slot) {
        return Inventories.removeStack(getItems(), slot);
    }

    @Override
    default void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
    }

    @Override
    default void clear() {
        getItems().clear();
    }

    @Override
    default void markDirty() {
        // Optional: override to mark block entity dirty
    }

    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
}
