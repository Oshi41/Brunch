package bk.Gui.TileEntity;

import bk.Base.Entity.TileEntity.BaseMachineUpgradeTileEntity;
import bk.Gui.Container.PotionCompressorContainer;
import bk.Initialize.ItemsInit;
import bk.Items.UpgradeItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

import java.util.List;

/**
 * Created by User on 08.07.2017.
 */
public class PotionCompressorTileEntity extends BaseMachineUpgradeTileEntity implements ISidedInventory {
    
    //0 - upgrade
    //1,2 - buckets
    //3 - fuel
    //4 - 12 - potions
    //13 - result
    public static NonNullList<ItemStack> inventory = NonNullList.withSize(14, ItemStack.EMPTY);
    
    @Override
    public List<ItemStack> getFuelStacks() {
        return inventory.subList(1, 2 + 1);
    }
    
    @Override
    public List<ItemStack> getUpgradeSlots() {
        return inventory.subList(0, 0 +1);
    }
    
    /**
     * Increase cook time by three times as vanilla furnace
     * @return
     */
    @Override
    public int getTotalCookTime() {
        return 200 * 3;
    }
    
    @Override
    public boolean hasReipes() {
        return inventory.subList(4, inventory.size() - 1).stream().filter(x -> x.isEmpty()).count() < 8;
    }
    
    @Override
    public void performRecipe() {
        List<ItemStack> stacks = inventory.subList(4, inventory.size() - 1);
        ItemStack result = new ItemStack(new UpgradeItem());
        stacks.stream().filter(x -> !x.isEmpty() && PotionUtils.getEffectsFromStack(x).size() > 0).forEach(x -> UpgradeItem.addTags(result, x));
        inventory.set(inventory.size() - 1, result);
    }
    
    @Override
    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }
    
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 0)
            return stack.getItem() == ItemsInit.upgradeItem;
        if (index <= 2)
            return stack.getItem() == Items.WATER_BUCKET;
        if (index == 3)
            return TileEntityFurnace.isItemFuel(stack);
        if (index == 13)
            return true;
        //Retirn things with potion effects
        return PotionUtils.getEffectsFromStack(stack).size() > 0;
    }
    
    @Override
    public String getName() {
        return "bk:potioncopressor";
    }
    
    @Override
    public boolean hasCustomName() {
        return false;
    }
    
    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new PotionCompressorContainer(this, playerIn);
    }
    
    @Override
    public String getGuiID() {
        return "bk:potioncompressors";
    }    
    
    //region ISidedInventory
    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
    }
    
    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index != 13;
    }
    
    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index == 13;
    }
    //endregion
}
