package bk.Base;

import net.minecraft.inventory.IInventoryChangedListener;

import java.util.List;

/**
 * Created by User on 07.07.2017.
 */
public interface IListenableInventory {
    /**
     * Listeners notified when any item in this inventory is changed.
     */
    List<IInventoryChangedListener> getChangeListeners();
    
    /**
     * Add a listener that will be notified when any item in this inventory is modified.
     */
    void addInventoryChangeListener(IInventoryChangedListener listener);
    
    /**
     * removes the specified IInvBasic from receiving further change notices
     */
    void removeInventoryChangeListener(IInventoryChangedListener listener);
    
}
