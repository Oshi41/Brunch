package bk.Gui.Container;

import bk.Gui.Slot.UnlimitedSlot;
import bk.Gui.TileEntity.UnlimitedTileEntity;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.Slot;

import java.util.Set;

/**
 * Created by User on 02.07.2017.
 */
public class UnlimitedContainer extends Container implements IInventoryChangedListener {
    
    private UnlimitedTileEntity tileEntity;
    private int dragEvent1;
    private int dragMode1;
    private Set<Slot> dragSlots1 = Sets.newHashSet();
    private boolean isMeging = false;
    
    //region Drawing poses
    private int size = 16;
    private int beginX = 12;
    private int beginY = 8;
    private int offset = 18;
    private int bigOffset = 22;
    
    private int textureSizeX = 183;
    private int textureSizeY = 183;
    
    private int numRows = 5;
    private int numCols = 9;
    
    
    
    //endregion
    
    public UnlimitedContainer(InventoryPlayer invPlayer, UnlimitedTileEntity tileEntity){
    
        this.tileEntity = tileEntity;
        tileEntity.addInventoryChangeListener(this);
        
        for (int chestRow = 0; chestRow < numRows; chestRow++) {
            for (int chestCol = 0; chestCol < numCols; chestCol++) {
                addSlotToContainer(new UnlimitedSlot(tileEntity, chestCol + chestRow * numCols, 12 + 
                        chestCol * 18, 8 + chestRow * 18));
            }
        }
    
        int leftCol = (textureSizeX - 162) / 2 + 1;
        for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++) {
            for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++) {
                addSlotToContainer(new Slot(invPlayer, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18, textureSizeY - (4 - playerInvRow) * 18
                        - 10));
            }
        
        }
    
        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            addSlotToContainer(new Slot(invPlayer, hotbarSlot, leftCol + hotbarSlot * 18, textureSizeY - 24));
        }   
        
    }
    
    
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }
    
    @Override
    public void onInventoryChanged(IInventory invBasic) {
        
    }
}
