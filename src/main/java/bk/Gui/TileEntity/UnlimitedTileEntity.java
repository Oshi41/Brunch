package bk.Gui.TileEntity;

import bk.Gui.Container.UnlimitedContainer;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;

import java.util.List;

/**
 * Created by User on 02.07.2017.
 */
public class UnlimitedTileEntity extends TileEntity implements IInventory, IInteractionObject {
    
    private boolean hasCustomName;
    private String inventoryTitle;
    private final NonNullList<ItemStack> inventoryContents;
    public final int slotsCount;
    private List<IInventoryChangedListener> changeListeners;
    
    public UnlimitedTileEntity(Tiles type){
        this(type.name, type.hasCustomName, type.slotCount);
    }
    public UnlimitedTileEntity(String title, boolean customName, int slotCount){
        this.slotsCount = slotCount;
        this.inventoryTitle = title;
        this.hasCustomName = customName;
        this.inventoryContents = NonNullList.<ItemStack>withSize(slotCount, ItemStack.EMPTY);
        //addInventoryChangeListener(this);
    }
    
    
    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getStackInSlot(int index) {
        return index >= 0 && index < this.inventoryContents.size() ? (ItemStack) this.inventoryContents.get(index) : ItemStack.EMPTY;
    }
    
//    /**
//     * Returns the stack in the given slot.
//     */
//    public ItemStack getStackInSlot(int index, boolean isRegularSize) {
//        return index >= 0 && index < this.inventoryContents.size() ? (ItemStack) this.inventoryContents.get(index).splitStack(64) : ItemStack.EMPTY;
//    }
    
    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventoryContents, index, count);
        
        if (!itemstack.isEmpty()) {
            this.markDirty();
        }
        
        return itemstack;
    }
    
//    public ItemStack addItem(ItemStack stack) {
//        ItemStack itemstack = stack.copy();
//        
//        for (int i = 0; i < this.slotsCount; ++i) {
//            ItemStack itemstack1 = this.getStackInSlot(i);
//            
//            if (itemstack1.isEmpty()) {
//                this.setInventorySlotContents(i, itemstack);
//                this.markDirty();
//                return ItemStack.EMPTY;
//            }
//            
//            if (ItemStack.areItemsEqual(itemstack1, itemstack)) {
//                int j = Math.min(this.getInventoryStackLimit(), itemstack1.getMaxStackSize());
//                int k = Math.min(itemstack.getCount(), j - itemstack1.getCount());
//                
//                if (k > 0) {
//                    itemstack1.grow(k);
//                    itemstack.shrink(k);
//                    
//                    if (itemstack.isEmpty()) {
//                        this.markDirty();
//                        return ItemStack.EMPTY;
//                    }
//                }
//            }
//        }
//        
//        if (itemstack.getCount() != stack.getCount()) {
//            this.markDirty();
//        }
//        
//        return itemstack;
//    }
    
    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index) {
        ItemStack itemstack = (ItemStack) this.inventoryContents.get(index);
        
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        else {
            this.inventoryContents.set(index, ItemStack.EMPTY);
            return itemstack;
        }
    }
    
    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventoryContents.set(index, stack);
        
        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        
        this.markDirty();
        
    }
    
    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return this.slotsCount;
    }
    
    public boolean isEmpty() {
        for (ItemStack itemstack : this.inventoryContents) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName() {
        return this.inventoryTitle;
    }
    
    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName() {
        return this.hasCustomName;
    }
    
    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public ITextComponent getDisplayName() {
        return (ITextComponent) (this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }
    
    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit() {
        return Integer.MAX_VALUE;
    }
    
    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty() {
        if (this.changeListeners != null) {
            for (int i = 0; i < this.changeListeners.size(); ++i) {
                ((IInventoryChangedListener) this.changeListeners.get(i)).onInventoryChanged(this);
            }
        }        
    }
    
    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }
    
    public void openInventory(EntityPlayer player) {
    }
    
    public void closeInventory(EntityPlayer player) {
    }
    
    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }
    
    public int getField(int id) {
        return 0;
    }
    
    public void setField(int id, int value) {
    }
    
    public int getFieldCount() {
        return 0;
    }
    
    public void clear() {
        this.inventoryContents.clear();
    }
    
    /**
     * Add a listener that will be notified when any item in this inventory is modified.
     */
    public void addInventoryChangeListener(IInventoryChangedListener listener) {
        if (this.changeListeners == null) {
            this.changeListeners = Lists.<IInventoryChangedListener>newArrayList();
        }
        
        this.changeListeners.add(listener);
    }
    
    /**
     * removes the specified IInvBasic from receiving further change notices
     */
    public void removeInventoryChangeListener(IInventoryChangedListener listener) {
        this.changeListeners.remove(listener);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        
        ItemStackHelper.loadAllItems(compound, inventoryContents);
//        for (int i = 0; i < inventoryContents.size(); i++) {
//            if (compound.hasKey("slot" + i)){
//                NBTTagCompound compound1 = compound.getCompoundTag("slot" + i);
//                ItemStack stack = ItemStack.EMPTY;
//                stack.deserializeNBT(compound1);
//                inventoryContents.set(i, stack);
//            }
//        }
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        
        ItemStackHelper.saveAllItems(compound, inventoryContents);
//        for (int i = 0; i < inventoryContents.size(); i++) {
//            if (!inventoryContents.get(i).isEmpty())
//                compound.setTag("slot" + i, inventoryContents.get(i).serializeNBT());
//        }
        
        return compound;
    }
    
    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new UnlimitedContainer(playerInventory, this);
    }
    
    @Override
    public String getGuiID() {
        return "bookcraft:unlimitedchest";
    }


//    @Override
//    public void onInventoryChanged(IInventory invBasic) {
//        LinkedList<ItemStack> sortedItems = Utils.mergeItems(new LinkedList<>(inventoryContents));
//        NonNullList<ItemStack> temp = NonNullList.withSize(sortedItems.size(), ItemStack.EMPTY);
//        for (int i = 0; i < sortedItems.size(); i++){
//            temp.set(i, sortedItems.get(i));
//        }
//        //inventoryContents = temp;
//    }
    
    public enum Tiles{
        Ultimate("ultimatechest", true, 81);
        
        public final String name;
        public final boolean hasCustomName;
        public final int slotCount;
        Tiles(String name, boolean hasCustomName, int slotCount){
            this.name = name;
            this.hasCustomName = hasCustomName;
            this.slotCount = slotCount;
        }
    }
}
