package bk.Base.GUI.Container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by User on 19.07.2017.
 */
public abstract class BaseContainer extends Container {
    
    private final TileEntity tileEntity;
    private final IInventory inventory;
    private final EntityPlayer player;
    
    //region Abstract
    public abstract int getTileSlotAmount();    
    public abstract int getSlotAmout();
    //endregion
    
    public BaseContainer(TileEntity entity, EntityPlayer player){
        this.tileEntity = entity;
        this.player = player;
        this.inventory = (IInventory) tileEntity;
    }    
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        if (inventory == null)
            return true;
        
        return inventory.isUsableByPlayer(playerIn);        
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
    
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
        
            if (index < getTileSlotAmount()) {
                if (!this.mergeItemStack(itemstack1, getTileSlotAmount(), getSlotAmout(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, getTileSlotAmount(), false)) {
                return ItemStack.EMPTY;
            }
        
            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }
        
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
        
            slot.onTake(playerIn, itemstack1);
        }
    
        return itemstack;
    }
}
