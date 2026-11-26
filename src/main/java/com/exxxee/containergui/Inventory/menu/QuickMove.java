package com.exxxee.containergui.Inventory.menu;

import com.exxxee.containergui.mixin.AbstractContainerMenuAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class QuickMove {
    private QuickMove() {
    }

    public static void moveOutOfBackpack(Player player, ItemStack stack) {
        AbstractContainerMenu host = player.containerMenu;
        if (host == null || stack.isEmpty()) return;
        if (!isCreativePicker(host) && host != player.inventoryMenu && moveRanges(host, player, stack, false, false)) {
            return;
        }
        moveRanges(host, player, stack, true, true);
    }

    private static boolean isCreativePicker(AbstractContainerMenu menu) {
        return menu.getClass().getName().endsWith("CreativeModeInventoryScreen$ItemPickerMenu");
    }

    private static boolean moveRanges(AbstractContainerMenu host, Player player, ItemStack stack, boolean inventorySlots, boolean reverse) {
        int before = stack.getCount();
        AbstractContainerMenuAccessor access = (AbstractContainerMenuAccessor) host;
        if (reverse) {
            int index = host.slots.size();
            while (index > 0 && !stack.isEmpty()) {
                if (isPlayerInventory(host.slots.get(index - 1), player) != inventorySlots) {
                    index--;
                    continue;
                }
                int end = index;
                while (index > 0 && isPlayerInventory(host.slots.get(index - 1), player) == inventorySlots) {
                    index--;
                }
                access.invokeMoveItemStackTo(stack, index, end, true);
            }
        } else {
            int index = 0;
            while (index < host.slots.size() && !stack.isEmpty()) {
                if (isPlayerInventory(host.slots.get(index), player) != inventorySlots) {
                    index++;
                    continue;
                }
                int start = index;
                while (index < host.slots.size() && isPlayerInventory(host.slots.get(index), player) == inventorySlots) {
                    index++;
                }
                access.invokeMoveItemStackTo(stack, start, index, false);
            }
        }
        return stack.getCount() != before;
    }

    private static boolean isPlayerInventory(Slot slot, Player player) {
        return slot.container == player.getInventory() && slot.getContainerSlot() < 36;
    }
}
