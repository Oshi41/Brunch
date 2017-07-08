package bk.Gui.Container;

import bk.Gui.Slot.UnlimitedSlot;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Set;

/**
 * Created by User on 07.07.2017.
 */
public class SingularityContainer extends Container {
    
    public final IInventory inventory;
    private int dragEvent1;
    private int dragMode1;
    private Set<Slot> dragSlots1 = Sets.newHashSet();
    //private boolean isMeging = false;
    
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
    
    public SingularityContainer(IInventory tileEntity, EntityPlayer player){
        this.inventory = tileEntity;
        tileEntity.openInventory(player);
//        if (tileEntity instanceof IListenableInventory)
//            ((IListenableInventory) tileEntity).addInventoryChangeListener(this);
    
        for (int chestRow = 0; chestRow < numRows; chestRow++) {
            for (int chestCol = 0; chestCol < numCols; chestCol++) {
                addSlotToContainer(new UnlimitedSlot(tileEntity, chestCol + chestRow * numCols, 12 +
                        chestCol * 18, 8 + chestRow * 18));
            }
        }
    
        int leftCol = (textureSizeX - 162) / 2 + 1;
        for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++) {
            for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++) {
                addSlotToContainer(new Slot(player.inventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 
                        18, textureSizeY - (4 - playerInvRow) * 18
                        - 10));
            }
        
        }
    
        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            addSlotToContainer(new Slot(player.inventory, hotbarSlot, leftCol + hotbarSlot * 18, textureSizeY - 24));
        }
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return inventory.isUsableByPlayer(playerIn);
    }
    
    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
//        if (inventory instanceof IListenableInventory)
//            ((IListenableInventory) inventory).removeInventoryChangeListener(this);
        super.onContainerClosed(playerIn);
        this.inventory.closeInventory(playerIn);
    }
        
    //region Boldly stolen and overriden Methods
    
    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        Slot slot = this.inventorySlots.get(index);
        if (slot == null || !slot.getHasStack()) return ItemStack.EMPTY;
    
        ItemStack itemstack = slot.getStack();
        ItemStack itemStack1 = itemstack.copy();
        //Unlimited slots
        if (index < numCols * numRows) {
            //Transfer unlimited amount of items            
            if (!super.mergeItemStack(itemstack, numCols * numRows,
                    this.inventorySlots.size(), true)) {
                return ItemStack.EMPTY;
            }
        }
        else {
            //Transfer only 64 or less
            if (!this.mergeItemStack(itemstack, 0, numCols * numRows, false))
                return ItemStack.EMPTY;
        }
    
        if (itemstack.isEmpty()) {
            slot.putStack(ItemStack.EMPTY);
        }
        else {
            slot.onSlotChanged();
        }
    
        return itemStack1;
    }
    
    /**
     * Merges provided ItemStack with the first avaliable one in the container/player inventor between minIndex
     * (included) and maxIndex (excluded). Args : stack, minIndex, maxIndex, negativDirection. /!\ the Container
     * implementation do not check if the item is valid for the slot
     */
    @Override
    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;
    
        if (reverseDirection) {
            i = endIndex - 1;
        }
    
        if (stack.isStackable()) {
            while (!stack.isEmpty()) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                }
                else if (i >= endIndex) {
                    break;
                }
            
                Slot slot = this.inventorySlots.get(i);
                ItemStack itemstack = slot.getStack();
            
                if (!itemstack.isEmpty() && itemstack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = slot.getSlotStackLimit();
                
                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.onSlotChanged();
                        flag = true;
                    }
                    else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.onSlotChanged();
                        flag = true;
                    }
                }
            
                if (reverseDirection) {
                    --i;
                }
                else {
                    ++i;
                }
            }
        }
    
        if (!stack.isEmpty()) {
            if (reverseDirection) {
                i = endIndex - 1;
            }
            else {
                i = startIndex;
            }
        
            while (true) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                }
                else if (i >= endIndex) {
                    break;
                }
            
                Slot slot1 = this.inventorySlots.get(i);
                ItemStack itemstack1 = slot1.getStack();
            
                if (itemstack1.isEmpty() && slot1.isItemValid(stack)) {
                    if (stack.getCount() > slot1.getSlotStackLimit()) {
                        slot1.putStack(stack.splitStack(slot1.getSlotStackLimit()));
                    }
                    else {
                        slot1.putStack(stack.splitStack(stack.getCount()));
                    }
                
                    slot1.onSlotChanged();
                    flag = true;
                    break;
                }
            
                if (reverseDirection) {
                    --i;
                }
                else {
                    ++i;
                }
            }
        }
    
        return flag;
    }
    
    /**
     * Trying to escape 64 size border, so I overrided it
     * @param slotId
     * @param dragType
     * @param clickTypeIn
     * @param player
     * @return
     */
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        ItemStack itemstack = ItemStack.EMPTY;
        InventoryPlayer inventoryplayer = player.inventory;
        
        if (clickTypeIn == ClickType.QUICK_CRAFT) {
            int i = this.dragEvent1;
            this.dragEvent1 = getDragEvent(dragType);
            
            if ((i != 1 || this.dragEvent1 != 2) && i != this.dragEvent1) {
                this.resetDrag();
            }
            else if (inventoryplayer.getItemStack().isEmpty()) {
                this.resetDrag();
            }
            else if (this.dragEvent1 == 0) {
                this.dragMode1 = extractDragMode(dragType);
                
                if (isValidDragMode(this.dragMode1, player)) {
                    this.dragEvent1 = 1;
                    this.dragSlots1.clear();
                }
                else {
                    this.resetDrag();
                }
            }
            else if (this.dragEvent1 == 1) {
                Slot slot = this.inventorySlots.get(slotId);
                ItemStack itemstack1 = inventoryplayer.getItemStack();
                
                if (slot != null && canAddItemToSlot(slot, itemstack1, true) && slot.isItemValid(itemstack1) && (this.dragMode1 == 2 || itemstack1.getCount() > this.dragSlots1.size()) && this.canDragIntoSlot(slot)) {
                    this.dragSlots1.add(slot);
                }
            }
            else if (this.dragEvent1 == 2) {
                if (!this.dragSlots1.isEmpty()) {
                    ItemStack itemstack5 = inventoryplayer.getItemStack().copy();
                    int l = inventoryplayer.getItemStack().getCount();
                    
                    for (Slot slot1 : this.dragSlots1) {
                        ItemStack itemstack2 = inventoryplayer.getItemStack();
                        
                        if (slot1 != null && canAddItemToSlot(slot1, itemstack2, true) && slot1.isItemValid(itemstack2) && (this.dragMode1 == 2 || itemstack2.getCount() >= this.dragSlots1.size()) && this.canDragIntoSlot(slot1)) {
                            ItemStack itemstack3 = itemstack5.copy();
                            int j = slot1.getHasStack() ? slot1.getStack().getCount() : 0;
                            computeStackSize(this.dragSlots1, this.dragMode1, itemstack3, j);
                            int k = Math.min(itemstack3.getMaxStackSize(), slot1.getItemStackLimit(itemstack3));
                            
                            if (itemstack3.getCount() > k) {
                                itemstack3.setCount(k);
                            }
                            
                            l -= itemstack3.getCount() - j;
                            slot1.putStack(itemstack3);
                        }
                    }
                    
                    itemstack5.setCount(l);
                    inventoryplayer.setItemStack(itemstack5);
                }
                
                this.resetDrag();
            }
            else {
                this.resetDrag();
            }
        }
        else if (this.dragEvent1 != 0) {
            this.resetDrag();
        }
        else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
            if (slotId == -999) {
                if (!inventoryplayer.getItemStack().isEmpty()) {
                    if (dragType == 0) {
                        player.dropItem(inventoryplayer.getItemStack(), true);
                        inventoryplayer.setItemStack(ItemStack.EMPTY);
                    }
                    
                    if (dragType == 1) {
                        player.dropItem(inventoryplayer.getItemStack().splitStack(1), true);
                    }
                }
            }
            else if (clickTypeIn == ClickType.QUICK_MOVE) {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }
                
                Slot slot6 = this.inventorySlots.get(slotId);
                
                if (slot6 != null && slot6.canTakeStack(player)) {
                    ItemStack itemstack10 = this.transferStackInSlot(player, slotId);
                    
                    if (!itemstack10.isEmpty()) {
                        Item item = itemstack10.getItem();
                        itemstack = itemstack10.copy();
                        
                        if (slot6.getStack().getItem() == item) {
                            this.retrySlotClick(slotId, dragType, true, player);
                        }
                    }
                }
            }
            else {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }
                
                Slot slot7 = this.inventorySlots.get(slotId);
                
                if (slot7 != null) {
                    ItemStack itemstack11 = slot7.getStack();
                    ItemStack itemstack13 = inventoryplayer.getItemStack();
                    
                    if (!itemstack11.isEmpty()) {
                        itemstack = itemstack11.copy();
                    }
                    
                    if (itemstack11.isEmpty()) {
                        if (!itemstack13.isEmpty() && slot7.isItemValid(itemstack13)) {
                            int l2 = dragType == 0 ? itemstack13.getCount() : 1;
                            
                            if (l2 > slot7.getItemStackLimit(itemstack13)) {
                                l2 = slot7.getItemStackLimit(itemstack13);
                            }
                            
                            slot7.putStack(itemstack13.splitStack(l2));
                        }
                    }
                    else if (slot7.canTakeStack(player)) {
                        if (itemstack13.isEmpty()) {
                            if (itemstack11.isEmpty()) {
                                slot7.putStack(ItemStack.EMPTY);
                                inventoryplayer.setItemStack(ItemStack.EMPTY);
                            }
                            else {
                                int k2 = dragType == 0 ? itemstack11.getCount() : (itemstack11.getCount() + 1) / 2;
                                inventoryplayer.setItemStack(slot7.decrStackSize(k2));
                                
                                if (itemstack11.isEmpty()) {
                                    slot7.putStack(ItemStack.EMPTY);
                                }
                                
                                slot7.onTake(player, inventoryplayer.getItemStack());
                            }
                        }
                        else if (slot7.isItemValid(itemstack13)) {
                            if (itemstack11.getItem() == itemstack13.getItem() && itemstack11.getMetadata() == itemstack13.getMetadata() && ItemStack.areItemStackTagsEqual(itemstack11, itemstack13)) {
                                int j2 = dragType == 0 ? itemstack13.getCount() : 1;
                                
                                if (j2 > slot7.getItemStackLimit(itemstack13) - itemstack11.getCount()) {
                                    j2 = slot7.getItemStackLimit(itemstack13) - itemstack11.getCount();
                                }

//                                if (j2 > itemstack13.getMaxStackSize() - itemstack11.getCount()) {
//                                    j2 = itemstack13.getMaxStackSize() - itemstack11.getCount();
//                                }
                                
                                itemstack13.shrink(j2);
                                itemstack11.grow(j2);
                            }
                            else if (itemstack13.getCount() <= slot7.getItemStackLimit(itemstack13)) {
                                slot7.putStack(itemstack13);
                                inventoryplayer.setItemStack(itemstack11);
                            }
                        }
                        else if (itemstack11.getItem() == itemstack13.getItem() && itemstack13.getMaxStackSize() > 1 && (!itemstack11.getHasSubtypes() || itemstack11.getMetadata() == itemstack13.getMetadata()) && ItemStack.areItemStackTagsEqual(itemstack11, itemstack13) && !itemstack11.isEmpty()) {
                            int i2 = itemstack11.getCount();
                            
                            if (i2 + itemstack13.getCount() <= itemstack13.getMaxStackSize()) {
                                itemstack13.grow(i2);
                                itemstack11 = slot7.decrStackSize(i2);
                                
                                if (itemstack11.isEmpty()) {
                                    slot7.putStack(ItemStack.EMPTY);
                                }
                                
                                slot7.onTake(player, inventoryplayer.getItemStack());
                            }
                        }
                    }
                    
                    slot7.onSlotChanged();
                }
            }
        }
        else if (clickTypeIn == ClickType.SWAP && dragType >= 0 && dragType < 9) {
            Slot slot5 = this.inventorySlots.get(slotId);
            ItemStack itemstack9 = inventoryplayer.getStackInSlot(dragType);
            ItemStack itemstack12 = slot5.getStack();
            
            if (!itemstack9.isEmpty() || !itemstack12.isEmpty()) {
                if (itemstack9.isEmpty()) {
                    if (slot5.canTakeStack(player)) {
                        inventoryplayer.setInventorySlotContents(dragType, itemstack12);
                        //Dont know how in base class thay have acces, thats something
                        //impossible!
                        //((SlotCrafting) slot5).onSwapCraft(itemstack12.getCount());
                        slot5.putStack(ItemStack.EMPTY);
                        slot5.onTake(player, itemstack12);
                    }
                }
                else if (itemstack12.isEmpty()) {
                    if (slot5.isItemValid(itemstack9)) {
                        int k1 = slot5.getItemStackLimit(itemstack9);
                        
                        if (itemstack9.getCount() > k1) {
                            slot5.putStack(itemstack9.splitStack(k1));
                        }
                        else {
                            slot5.putStack(itemstack9);
                            inventoryplayer.setInventorySlotContents(dragType, ItemStack.EMPTY);
                        }
                    }
                }
                else if (slot5.canTakeStack(player) && slot5.isItemValid(itemstack9)) {
                    int l1 = slot5.getItemStackLimit(itemstack9);
                    
                    if (itemstack9.getCount() > l1) {
                        slot5.putStack(itemstack9.splitStack(l1));
                        slot5.onTake(player, itemstack12);
                        
                        if (!inventoryplayer.addItemStackToInventory(itemstack12)) {
                            player.dropItem(itemstack12, true);
                        }
                    }
                    else {
                        slot5.putStack(itemstack9);
                        inventoryplayer.setInventorySlotContents(dragType, itemstack12);
                        slot5.onTake(player, itemstack12);
                    }
                }
            }
        }
        else if (clickTypeIn == ClickType.CLONE && player.capabilities.isCreativeMode && inventoryplayer.getItemStack().isEmpty() && slotId >= 0) {
            Slot slot4 = this.inventorySlots.get(slotId);
            
            if (slot4 != null && slot4.getHasStack()) {
                ItemStack itemstack8 = slot4.getStack().copy();
                itemstack8.setCount(itemstack8.getMaxStackSize());
                inventoryplayer.setItemStack(itemstack8);
            }
        }
        else if (clickTypeIn == ClickType.THROW && inventoryplayer.getItemStack().isEmpty() && slotId >= 0) {
            Slot slot3 = this.inventorySlots.get(slotId);
            
            if (slot3 != null && slot3.getHasStack() && slot3.canTakeStack(player)) {
                ItemStack itemstack7 = slot3.decrStackSize(dragType == 0 ? 1 : slot3.getStack().getCount());
                slot3.onTake(player, itemstack7);
                player.dropItem(itemstack7, true);
            }
        }
        else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
            Slot slot2 = this.inventorySlots.get(slotId);
            ItemStack itemstack6 = inventoryplayer.getItemStack();
            
            if (!itemstack6.isEmpty() && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(player))) {
                int i1 = dragType == 0 ? 0 : this.inventorySlots.size() - 1;
                int j1 = dragType == 0 ? 1 : -1;
                
                for (int i3 = 0; i3 < 2; ++i3) {
                    for (int j3 = i1; j3 >= 0 && j3 < this.inventorySlots.size() && itemstack6.getCount() < itemstack6.getMaxStackSize(); j3 += j1) {
                        Slot slot8 = (Slot) this.inventorySlots.get(j3);
                        
                        if (slot8.getHasStack() && canAddItemToSlot(slot8, itemstack6, true) && slot8.canTakeStack(player) && this.canMergeSlot(itemstack6, slot8)) {
                            ItemStack itemstack14 = slot8.getStack();
                            
                            if (i3 != 0 || itemstack14.getCount() != itemstack14.getMaxStackSize()) {
                                int k3 = Math.min(itemstack6.getMaxStackSize() - itemstack6.getCount(), itemstack14.getCount());
                                ItemStack itemstack4 = slot8.decrStackSize(k3);
                                itemstack6.grow(k3);
                                
                                if (itemstack4.isEmpty()) {
                                    slot8.putStack(ItemStack.EMPTY);
                                }
                                
                                slot8.onTake(player, itemstack4);
                            }
                        }
                    }
                }
            }
            
            super.detectAndSendChanges();
        }
        
        return itemstack;
    }
    
//    @Override
//    public void onInventoryChanged(IInventory invBasic) {
//        if (isMeging) return;
//        
//        isMeging = true;
//        mergeContent();
//        invBasic.markDirty();
//        isMeging = false;
//    }
    
    //endregion
    
    //region Helping methods
    @Override
    protected void resetDrag() {
        this.dragEvent1 = 0;
        this.dragSlots1.clear();
    }
    
//    /**
//     * Trying to merge itemStacks into one large
//     */
//    public void mergeContent() {        
//        for (int i = 0; i < numRows * numCols - 1; i++) {
//            Slot slot = this.inventorySlots.get(i);
//            if (slot == null || slot.getStack().isEmpty()) continue;
//            ItemStack stack = slot.getStack();
//            
//            for (int j = i + 1; j < numRows * numCols - 1; j++) {
//                ItemStack toMerge = this.inventorySlots.get(j).getStack();
//                //If find equal
//                if (stack.getItem() == toMerge.getItem() && stack.getMetadata() ==
//                        toMerge.getMetadata() && ItemStack.areItemStackTagsEqual(stack, toMerge)) {
//                    int increaseSize = toMerge.getCount();
//                    if (slot.getSlotStackLimit() - stack.getCount() < increaseSize)
//                        increaseSize -= slot.getSlotStackLimit() - stack.getCount();
//                    stack.grow(increaseSize);
//                    toMerge.shrink(increaseSize);
//                }
//            }
//        }
//    }
    //endregion
}
