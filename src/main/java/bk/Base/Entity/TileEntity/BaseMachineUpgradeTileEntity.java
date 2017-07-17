package bk.Base.Entity.TileEntity;

import bk.Items.UpgradeItem;
import com.sun.istack.internal.NotNull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.world.IInteractionObject;

import java.util.List;
import java.util.Optional;

/**
 * Created by User on 12.07.2017.
 */
public abstract class BaseMachineUpgradeTileEntity extends TileEntity implements ITickable, IInventory, IInteractionObject {  
    
    private final String remainingCookTimeName = "cooktime";
    private final String burnTimeName = "burntime";
    
    public static int remainingCookTime;
    public static int burnTime;
    public static int totalBurnTime;
    
    //region Abstract    
    /**
     * MUST BE NOT NULL AND HAS TAG COMPAUND
     * @return Returns fuel slots
     */
    @NotNull
    public abstract List<ItemStack> getFuelStacks();
    
    /**
     * MUST BE NOT NULL AND HAS TAG COMPAUND 
     * @return Retirns upgrade slots
     */
    @NotNull
    public abstract List<ItemStack> getUpgradeSlots();
    
    /**
     * Returns base cook time (default furnace is 200)
     * @return
     */
    public abstract int getTotalCookTime();
    
    /**
     * Finds out if machine has recipe to perform
     * @return
     */
    public abstract boolean hasReipes();
    
    /**
     * When finished cooking and need to get result
     */
    public abstract void performRecipe();
    
//    /**
//     * Saving inventory
//     * @param inventory
//     * @param compound
//     */
//    public abstract void saveInventory(NonNullList<ItemStack> inventory, NBTTagCompound compound);
//    
//    /**
//     * Loading inventory
//     * @param inventory
//     * @param compound
//     */
//    public abstract void loadInventory(NonNullList<ItemStack> inventory, NBTTagCompound compound);
    
    /**
     * Returns inventory
     * @return
     */
    public abstract NonNullList<ItemStack> getInventory();
    
    //endregion
    
    //region Upgrade Maths
    
    //Tries to add cooktime with upgrades
    public int addCookTime(){
        final double[] result = {1};
        getUpgradeSlots().stream().forEach(x -> result[0] *= getAmplifier(x.getTagCompound().getInteger(UpgradeItem.speedName)));
        return (int) result[0];
    }
    
    //Tries to consume with upgrade power
    public boolean canConsumeItem(){
        Optional<ItemStack> stack = getFuelStacks().stream().filter(x -> !x.isEmpty()).findFirst();
        if (!stack.isPresent()) return false;     
        ItemStack itemstack = stack.get();
        int index = getFuelStacks().indexOf(itemstack);
        
        int tempBurnTime = TileEntityFurnace.getItemBurnTime(itemstack);
        int result = tempBurnTime;
        itemstack.shrink(1);
        
        //Return Container item
        if (itemstack.isEmpty() && index > 0) {
            getFuelStacks().set(index, new ItemStack(itemstack.getItem().getContainerItem()));
        }
        
        for (ItemStack stack1: getUpgradeSlots()) {
            if (!stack1.isEmpty() && stack1.hasTagCompound()){
                result += tempBurnTime * getAmplifier(stack1.getTagCompound().getInteger(UpgradeItem.rangeName));
            }
        }
        burnTime = result;
        totalBurnTime = burnTime;
        return burnTime > 0;
    }
    
    public double getAmplifier(int upgradeAmount){        
        return (upgradeAmount / (UpgradeItem.maxValue / Math.sqrt(8)));
    }
    //endregion
    
    //region Helping methods
    public boolean isBurning(){
        return burnTime > 0;
    }
    public boolean isCooking(){return remainingCookTime > 0;}
    //endregion
    
    //region ITickable
    @Override
    public void update() {
        
        if (isBurning())
            burnTime--;
        
        boolean inventoryChangedFlag = false;
        
        if (this.world.isRemote){
            
            if (hasReipes()){
                if (isBurning() || canConsumeItem()){
                    remainingCookTime += addCookTime();
                    
                    if (remainingCookTime >= getTotalCookTime()){
                        performRecipe();
                        remainingCookTime = 0;
                        inventoryChangedFlag = true;
                    }
                }
                //Decrease cooktime because we haven't fuel
                else {
                    if (remainingCookTime > 0)
                        remainingCookTime -= 2;
                }
            }         
            //Decrease cooktime becase we've lost recipe
            else {
                if (remainingCookTime > 0)
                    remainingCookTime -= 2;
            }
        }
        
        if (inventoryChangedFlag)
            this.markDirty();
    }
    
    //endregion
    
    //region TileEntity
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        ItemStackHelper.loadAllItems(compound, getInventory());
        burnTime = compound.getInteger(burnTimeName);
        remainingCookTime = compound.getInteger(remainingCookTimeName);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, getInventory());
        compound.setInteger(burnTimeName, burnTime);
        compound.setInteger(remainingCookTimeName, remainingCookTime);
        return compound;
    }
    
    //endregion
    
    //region IInventory
    
    @Override
    public boolean isEmpty() {
        return getInventory().stream().allMatch(x -> x.isEmpty());
    }    
    @Override
    public ItemStack getStackInSlot(int index) {
        if (index < 0 || index >= getInventory().size()) return ItemStack.EMPTY;
        return getInventory().get(index);
    }    
    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(getInventory(), index, count);
    }    
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(getInventory(), index);
    }    
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index < 0 || index >= getInventory().size()) return;
        
        NonNullList<ItemStack> list = getInventory();
        list.set(index, stack);
    
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
    }    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }    
    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, 
                (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D; 
    }    
    @Override
    public void openInventory(EntityPlayer player) {
        
    }    
    @Override
    public void closeInventory(EntityPlayer player) {
        
    }
    
    /**
     * 0 - total Cook Time
     * 1 - remaining cook time
     * 2 - total burn Time
     * 3 - remaining burn time
     * @param id
     * @return
     */
    @Override
    public int getField(int id) {
        switch (id){
            case 0:
                return getTotalCookTime();
            case 1:
                return remainingCookTime;
            case 2:
                return totalBurnTime;
            case 3:
                return burnTime;
            default:
                return -1;
        }
    }
    
    /**
     * 0 - total Cook Time
     * 1 - remaining cook time
     * 2 - total burn Time
     * 3 - remaining burn time 
     * @param id
     * @param value
     */
    @Override
    public void setField(int id, int value) {
        switch (id){
            case 1:
                remainingCookTime = value;
            case 2:
                totalBurnTime = value;
            case 3:
                burnTime = value;
        }
    }    
    @Override
    public int getFieldCount() {
        return 4;
    }    
    @Override
    public void clear() {
        NonNullList<ItemStack> list = getInventory();
        int size = list.size();
        list = NonNullList.withSize(size, ItemStack.EMPTY);
    }    
    @Override
    public int getSizeInventory() {
        return getInventory().size();
    }
    //endregion
}
