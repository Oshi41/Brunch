package bk.Gui.TileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

/**
 * Created by User on 08.07.2017.
 */
public class PotionCompressorTileEntity extends TileEntity implements IInventory {
    
    private NonNullList<ItemStack> inventoryContents;
    
    @Override
    public int getSizeInventory() {
        return inventoryContents.size();
    }
    
    @Override
    public boolean isEmpty() {
        return inventoryContents.stream().allMatch(x -> x.isEmpty());
    }
    
    @Override
    public ItemStack getStackInSlot(int index) {
        return checkBounds(index) ? ItemStack.EMPTY : inventoryContents.get(index);
    }
    
    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(inventoryContents, index, count);
    
        if (!itemstack.isEmpty()) {
            this.markDirty();
        }
    
        return itemstack;
    }
    
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(inventoryContents, index);
    }
    
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventoryContents.set(index, stack);
    
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
    
        this.markDirty();
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }
    
    @Override
    public void openInventory(EntityPlayer player) { }
    
    @Override
    public void closeInventory(EntityPlayer player) { }
    
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        //TODO 
        // 0 - reagent
        // 1,2 - buckets
        // 3 - fuel
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
        
    }
    
    @Override
    public String getName() {
        return null;
    }
    
    @Override
    public boolean hasCustomName() {
        return false;
    }
    
    private boolean checkBounds(int i) {
        return 0 > i || i >= inventoryContents.size();
    }
}
