package bk.Gui.Container;

import bk.Utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.awt.*;

/**
 * Created by User on 13.07.2017.
 */
public class PotionCompressorContainer extends Container {
    
    private final IInventory tileEntity;
    
    //region Drawind
    private final int size = 16; 
    //endregion
    
    public PotionCompressorContainer(IInventory tileEntity, EntityPlayer player) {
        this.tileEntity = tileEntity;
        
        //0 - upgrade
        addSlotToContainer(new Slot(tileEntity, 0, 8, 9));     
        
        //1,2 - buckets
        addSlotToContainer(new Slot(tileEntity, 1, 8, 53));
        addSlotToContainer(new Slot(tileEntity, 2, 26, 53));
        
        //3 - fuel
        addSlotToContainer(new Slot(tileEntity, 3, 81, 97));
        
        // 4 - 12 crafting slots
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++){
                int index = 4 + j * (i * 3);
                int x = 60 + j * 16;
                int y = 16 + i * 16;
                addSlotToContainer(new Slot(tileEntity, index, x, y));
            }
        //13 - fuel
        addSlotToContainer(new Slot(tileEntity, 13, 146, 34));
        
        //player inventory
        Slot[] slots = Utils.getPlayerSlots(player.inventory, new Point(7,183), new Point(7,125));
        for (int i = 0; i < slots.length; i++)
            addSlotToContainer(slots[i]);
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        Slot slot = inventorySlots.get(index);
        if (slot == null || !slot.getHasStack()) return ItemStack.EMPTY;
    
        ItemStack itemstack = slot.getStack();
        ItemStack itemStack1 = itemstack.copy();
        //Transfer to player inventory
        if (index <= 13){
            if (!mergeItemStack(itemstack, 14, inventorySlots.size(), true))
                return ItemStack.EMPTY;
        }
        //Transfer to machine inventory
        else {
            if (!mergeItemStack(itemstack, 0, 14, false))
                return ItemStack.EMPTY;
        }
        
        return ItemStack.EMPTY;
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileEntity.isUsableByPlayer(playerIn);
    }
}
