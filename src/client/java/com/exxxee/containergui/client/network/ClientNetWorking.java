package com.exxxee.containergui.client.network;

import com.exxxee.containergui.Inventory.menu.AbstractBackpackMenu;
import com.exxxee.containergui.Inventory.menu.WindowBuiltInRegistries;
import com.exxxee.containergui.Inventory.menu.WindowMenuType;
import com.exxxee.containergui.Network.payload.CloseWindowPayload;
import com.exxxee.containergui.Network.payload.OpenWindowPayload;
import com.exxxee.containergui.Network.payload.WindowContentPayload;
import com.exxxee.containergui.Network.payload.WindowSlotPayload;
import com.exxxee.containergui.api.IBackpackHost;
import com.exxxee.containergui.client.gui.windows.BackpackWindowHost;
import com.exxxee.containergui.client.gui.windows.MenuWindows;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ClientNetWorking {
    private ClientNetWorking() {
    }

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(OpenWindowPayload.TYPE, ClientNetWorking::openWindow);
        ClientPlayNetworking.registerGlobalReceiver(CloseWindowPayload.TYPE, ClientNetWorking::closeWindow);
        ClientPlayNetworking.registerGlobalReceiver(WindowContentPayload.TYPE, ClientNetWorking::syncWindow);
        ClientPlayNetworking.registerGlobalReceiver(WindowSlotPayload.TYPE, ClientNetWorking::syncWindowSlot);
    }

    private static void openWindow(OpenWindowPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            WindowMenuType<?> menuType = WindowBuiltInRegistries.MENU.get(payload.menuType());
            if (menuType == null) return;
            MenuWindows.create(menuType, context.client(), payload.windowId());
        });
    }

    private static void syncWindow(WindowContentPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            AbstractBackpackMenu menu = ((IBackpackHost) context.player()).getBackpackMenu(payload.windowId());
            if (menu == null) return;
            List<ItemStack> items = payload.items();
            int count = Math.min(items.size(), menu.slots.size());
            for (int i = 0; i < count; i++) {
                menu.slots.get(i).set(items.get(i).copy());
            }
        });
    }

    private static void syncWindowSlot(WindowSlotPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            AbstractBackpackMenu menu = ((IBackpackHost) context.player()).getBackpackMenu(payload.windowId());
            if (menu == null) return;
            int slot = payload.slot();
            if (slot < 0 || slot >= menu.slots.size()) return;
            menu.slots.get(slot).set(payload.item().copy());
        });
    }

    private static void closeWindow(CloseWindowPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            Player player = context.player();
            AbstractBackpackMenu menu = ((IBackpackHost) player).getBackpackMenu(payload.windowId());
            if (menu == null || menu.containerId != payload.windowId()) return;
            ((IBackpackHost) player).setBackpackContainer(menu.containerId, null);
            ((IBackpackHost) player).setBackpackMenu(menu.containerId,  null);
            if (context.client().screen instanceof BackpackWindowHost host) {
                host.setBackpackWindow(payload.windowId(), null);
            }
        });
    }

}
