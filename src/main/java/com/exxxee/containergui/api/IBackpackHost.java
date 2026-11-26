package com.exxxee.containergui.api;

import com.exxxee.containergui.Inventory.menu.AbstractBackpackMenu;
import net.minecraft.world.Container;

import java.util.Collection;

public interface IBackpackHost {
    Collection<Container> getBackpackContainers();
    Container getBackpackContainer(int windowId);
    void setBackpackContainer(int windowId, Container container);
    Collection<AbstractBackpackMenu> getBackpackMenus();
    AbstractBackpackMenu getBackpackMenu(int windowId);
    void setBackpackMenu(int windowId, AbstractBackpackMenu menu);
}
