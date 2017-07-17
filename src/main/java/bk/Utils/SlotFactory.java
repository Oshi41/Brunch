package bk.Utils;

import bk.Gui.Slot.CustomSlot;
import bk.Initialize.ItemsInit;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.potion.PotionUtils;

/**
 * Created by User on 17.07.2017.
 */
public class SlotFactory {
    
    public static CustomSlot getOutupSlot(IInventory inventoryIn, int index, int xPosition, int yPosition){
        return new CustomSlot(inventoryIn, index, xPosition, yPosition).setOutOnly();
    }
    
    public static CustomSlot getWaterBucketSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        return new CustomSlot(inventoryIn, index, xPosition, yPosition).whiteListItems(Items.WATER_BUCKET);
    }
    
    public static CustomSlot getUpgradeSlot(IInventory inventoryIn, int index, int xPosition, int yPosition){
        return new CustomSlot(inventoryIn, index, xPosition, yPosition).whiteListItems(ItemsInit.upgradeItem);
    }
    
    public static CustomSlot getPotionSlot(IInventory inventoryIn, int index, int xPosition, int yPosition){
        return new CustomSlot(inventoryIn, index, xPosition, yPosition).setItemsPredicat(x -> PotionUtils.getEffectsFromStack(x).size() > 0);
    }
}
