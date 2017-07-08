package bk.Gui.TileEntity;

import bk.Base.IListenableInventory;
import bk.Gui.Container.SingularityContainer;
import bk.Utils.Utils;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.IInteractionObject;

import java.util.List;

/**
 * Created by User on 07.07.2017.
 */
public class SingularityTileEntity extends TileEntity implements IInventory, IInteractionObject, IListenableInventory {
    
    private final int slotsCount;
    private final String inventoryTitle;
    private final boolean hasCustomName;
    private NonNullList<ItemStack> inventoryContents;
    private List<IInventoryChangedListener> changeListeners;
    
    //Just need, do not delete!!!
    public SingularityTileEntity(){this(Types.SINGULARITY);}
    
    
    public SingularityTileEntity(Types type){
        this(type.name, type.hasCustomName, type.slotCount);
    }
    public SingularityTileEntity(String title, boolean customName, int slotCount) {
        this.slotsCount = slotCount;
        this.inventoryTitle = title;
        this.hasCustomName = customName;
        this.inventoryContents = NonNullList.withSize(slotCount, ItemStack.EMPTY);
    }
    
    @Override
    public int getSizeInventory() {
        return slotsCount;
    }
    
    @Override
    public boolean isEmpty() {
        return inventoryContents.stream().allMatch(x -> x.isEmpty());
    }
    
    @Override
    public ItemStack getStackInSlot(int index) {
        if (checkBounds(index)) return ItemStack.EMPTY;
        return inventoryContents.get(index);
    }
    
    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = getStackInSlot(index);
        if (stack.isEmpty()) return ItemStack.EMPTY;
            
        ItemStack tReturn =  splitStack(stack, count);
        super.markDirty();
        return tReturn;
    }
    
    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (checkBounds(index)) return ItemStack.EMPTY;
        return inventoryContents.set(index, ItemStack.EMPTY);
    }
    
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (checkBounds(index)) return;
        
        inventoryContents.set(index, stack);
        inventoryContents = Utils.mergeItems(inventoryContents);
        super.markDirty();
    }
    
    @Override
    public int getInventoryStackLimit() {
        return Integer.MAX_VALUE;
    }
    
    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.getPos().distanceSq(player.posX, player.posY, player.posZ) < 35;
    }
    
    @Override
    public void openInventory(EntityPlayer player) {
        
    }
    
    @Override
    public void closeInventory(EntityPlayer player) {
        
    }
    
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }
    
    @Override
    public int getField(int id) {
        return 0;
    }
    
    @Override
    public void setField(int id, int value) {
        
    }
    
    @Override
    public int getFieldCount() {
        return 0;
    }
    
    @Override
    public void clear() {
        this.inventoryContents = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
    }
    
    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new SingularityContainer(this, playerIn);
    }
    
    @Override
    public String getGuiID() {
        return "bk:" + inventoryTitle;
    }
    
    @Override
    public String getName() {
        return inventoryTitle;
    }
    
    @Override
    public boolean hasCustomName() {
        return true;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.inventoryContents = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        Utils.loadAllItems(compound, inventoryContents);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {        
        super.writeToNBT(compound);
        Utils.saveAllItems(compound, inventoryContents);
        return compound;
    }
    
    //region Helping methods
    
    private boolean checkBounds(int i){
        return 0 > i || i >= slotsCount;
    }
    //Split stack without 64 size border
    public ItemStack splitStack(ItemStack stack, int amount) {
        ItemStack itemstack = stack.copy();
        itemstack.setCount(amount);
        stack.shrink(amount);
        return itemstack;
    }
    
    @Override
    public List<IInventoryChangedListener> getChangeListeners() {
        return this.changeListeners;
    }
    
    /**
     * Add a listener that will be notified when any item in this inventory is modified.
     */
    public void addInventoryChangeListener(IInventoryChangedListener listener) {
        if (changeListeners == null)
            changeListeners = Lists.newArrayList();
        
        this.changeListeners.add(listener);
    }
    
    /**
     * removes the specified IInvBasic from receiving further change notices
     */
    public void removeInventoryChangeListener(IInventoryChangedListener listener) {
        this.changeListeners.remove(listener);
    }
    
    //endregion
    
    public enum Types {
        ULTIMATE("ultimatechest", true, 81),
        SINGULARITY("singularitychest", true, 81);
        
        public final String name;
        public final boolean hasCustomName;
        public final int slotCount;
    
        Types(String name, boolean hasCustomName, int slotCount) {
            this.name = name;
            this.hasCustomName = hasCustomName;
            this.slotCount = slotCount;
        }
    }
    
    //TODO move merging method here
}
