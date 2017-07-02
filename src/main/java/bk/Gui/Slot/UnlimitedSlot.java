package bk.Gui.Slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by User on 02.07.2017.
 */
public class UnlimitedSlot extends Slot {
    
    public UnlimitedSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack) {
        return this.inventory.getInventoryStackLimit();
    }
}
