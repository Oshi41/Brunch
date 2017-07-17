package bk.Gui.Slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by User on 17.07.2017.
 */
public class CustomSlot extends Slot {
    
    public List<Item> WhiteListItems;
    public List<Item> BlackListItems;
    
    public List<ItemStack> WhiteListItemStacks;
    public List<ItemStack> BlackListItemStacks;
    
    Predicate<ItemStack> mathingItems;    
    Predicate predicate;    
    
    public boolean onlyOut = false;
    
    public CustomSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }
    
    @Override
    public boolean isItemValid(ItemStack stack) {
        if (onlyOut) return false;
        if (mathingItems != null){
            return mathingItems.test(stack);
        }
        
        if (isItemBlackListed(stack)) return false;
        
        return isItemWhiteListed(stack);
    }    
    
    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        if (predicate != null && predicate.test(null))
            return super.canTakeStack(playerIn);
        return false;
    }
    
    //region Helping methods
    public CustomSlot whiteListItems(Item... WhiteListItems){        
        this.WhiteListItems = Arrays.asList(WhiteListItems);
        return this;
    }    
    public CustomSlot whiteListItemStacks(ItemStack... WhiteListItemStacks) {
        this.WhiteListItemStacks = Arrays.asList(WhiteListItemStacks);
        return this;
    }    
    public CustomSlot blackListItems(Item... BlackListItems) {
        this.BlackListItems = Arrays.asList(BlackListItems);
        return this;
    }    
    public CustomSlot blackListItemStacks(ItemStack... BlackListItemStacks) {
        this.BlackListItemStacks = Arrays.asList(BlackListItemStacks);
        return this;
    }
    
    public CustomSlot setTakePredicat(Predicate predicat){
        this.predicate = predicat;
        return this;
    }
    public CustomSlot setItemsPredicat(Predicate<ItemStack> mathingItems){
        this.mathingItems = mathingItems;
        return this;
    }
    
    public CustomSlot setOutOnly(){
        onlyOut = true;
        return this;
    }
    

    private boolean isItemWhiteListed(ItemStack stack){
        boolean isEmpty = true;

        //Check for any coinsidence
        if (WhiteListItems != null && WhiteListItems.size() > 0)
        {
            if (isEmpty) isEmpty = false;
            if (WhiteListItems.stream().anyMatch(x -> stack.getItem() == x)) return true;
        }            

        //Check exactly coinsidence
        if (WhiteListItemStacks != null && WhiteListItemStacks.size() > 0) {
            if (isEmpty) isEmpty = false;
            if (WhiteListItemStacks.stream().anyMatch(x -> stack.isItemEqualIgnoreDurability(x))) return true;
        }

        return isEmpty;
    }
    
    private boolean isItemBlackListed(ItemStack stack){
        boolean isEmpty = true;
    
        //Check for any coinsidence
        if (BlackListItems != null && BlackListItems.size() > 0) {
            if (isEmpty) isEmpty = false;
            if (BlackListItems.stream().anyMatch(x -> stack.getItem() == x)) return true;
        }
    
        //Check exactly coinsidence
        if (BlackListItemStacks != null && BlackListItemStacks.size() > 0) {
            if (isEmpty) isEmpty = false;
            if (BlackListItemStacks.stream().anyMatch(x -> stack.isItemEqualIgnoreDurability(x))) return true;
        }
    
        return isEmpty;
    }    
    
    //endregion
}
