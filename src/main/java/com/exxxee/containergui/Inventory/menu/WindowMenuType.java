package com.exxxee.containergui.Inventory.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;

public class WindowMenuType<T extends AbstractBackpackMenu> {

    private final MenuSupplier<T> constructor;
    private final ContainerSupplier containerSupplier;

    public WindowMenuType(MenuSupplier<T> constructor, ContainerSupplier containerSupplier) {
        this.constructor = constructor;
        this.containerSupplier = containerSupplier;
    }

    public static void register(String name, WindowMenuType<?> type) {
        WindowMenuType<?> prev = WindowBuiltInRegistries.MENU.put(name, type);
        if (prev != null) {
            throw new IllegalStateException("Duplicate registration for " + name);
        }
    }

    public Container createContainer(Player player) {
        return this.containerSupplier.create(player);
    }

    public T create(int windowId, Player player, Container container) {
        T menu = this.constructor.create(windowId, player, container);
        menu.menuType = this;
        return menu;
    }

    public interface MenuSupplier<T extends AbstractBackpackMenu> {
        T create(int windowId, Player player, Container container);
    }

    public interface ContainerSupplier {
        Container create(Player player);
    }
}
