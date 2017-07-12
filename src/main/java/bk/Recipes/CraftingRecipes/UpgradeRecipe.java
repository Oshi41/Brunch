package bk.Recipes.CraftingRecipes;

import bk.Initialize.ItemsInit;
import bk.Items.UpgradeItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by User on 10.07.2017.
 */
public class UpgradeRecipe extends ShapelessRecipes {
    
    protected static Map<ItemStack, Integer> speedItems = new HashMap<ItemStack, Integer>(){{
        put(new ItemStack(Items.REDSTONE), 1);            
        put(new ItemStack(Blocks.REDSTONE_BLOCK), 7);            
        put(new ItemStack(Items.GLOWSTONE_DUST), 2);
        put(new ItemStack(Blocks.GLOWSTONE), 4);
        put(new ItemStack(Blocks.RED_FLOWER), 2);
        put(new ItemStack(Items.DYE, EnumDyeColor.RED.getDyeDamage()), 1);
    }};
    protected static Map<ItemStack, Integer> rangeItems = new HashMap<ItemStack, Integer>() {{
        put(new ItemStack(Items.EMERALD), 7);
        put(new ItemStack(Blocks.EMERALD_BLOCK), 65);
        put(new ItemStack(Items.ENDER_EYE), 2);
        put(new ItemStack(Items.SPIDER_EYE), 1);
    }};
    protected static Map<ItemStack, Integer> powerItems = new HashMap<ItemStack, Integer>() {{
        put(new ItemStack(Blocks.BEDROCK), 100);
        put(new ItemStack(Blocks.REDSTONE_BLOCK), 5);
        put(new ItemStack(Blocks.GLOWSTONE), 4);
        put(new ItemStack(Items.COAL), 1);
        put(new ItemStack(Blocks.COAL_BLOCK), 5);        
    }};    
    
    public UpgradeRecipe() {
        super(null, null);
    }
    
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        int upgradeCount = 0;
        int emptyness = 0;
        
        for (int i = 0; i < inv.getHeight(); ++i)
            for (int j = 0; j < inv.getWidth(); ++j) {
                ItemStack itemstack = inv.getStackInRowAndColumn(j, i);
                
                if (itemstack.isEmpty()) {
                    emptyness++;
                    continue;
                }
                
                if (contains(itemstack, rangeItems)) continue;
                if (contains(itemstack, speedItems)) continue;
                if (contains(itemstack, powerItems)) continue;
        
                if (ItemsInit.upgradeItem == itemstack.getItem()) {
                    upgradeCount++;
                    continue;
                }
                return false;
            }
        if (upgradeCount < 1) return false;
        
        if (upgradeCount == 1){
            return emptyness < inv.getWidth() * inv.getHeight() - 1;
        }
        else {
            return emptyness + upgradeCount == inv.getWidth() * inv.getHeight();
        }
    }
    
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        if (isSplitting(inv)){
            ArrayList<ItemStack> stacks = new ArrayList<>();
            for (int i = 0; i < inv.getHeight(); ++i)
                for (int j = 0; j < inv.getWidth(); ++j) {
                    ItemStack itemstack = inv.getStackInRowAndColumn(j, i);
                    if (itemstack.isEmpty()) continue;
                    if (itemstack.getItem() == ItemsInit.upgradeItem && itemstack.hasTagCompound()){
                        stacks.add(itemstack);
                    }                    
                }
            ItemStack stack = new ItemStack(new UpgradeItem());
            for (ItemStack stack1 : stacks){
                stack = UpgradeItem.addTags(stack, stack1);
            }
            return stack;
        }
        int range = 0, power = 0, speed = 0;
        ItemStack stack = ItemStack.EMPTY;
    
        for (int i = 0; i < inv.getHeight(); ++i)
            for (int j = 0; j < inv.getWidth(); ++j) {
                ItemStack itemstack = inv.getStackInRowAndColumn(j, i);
                if (itemstack.isEmpty()) continue;
                
                range += getOrDefault(itemstack, rangeItems);
                power += getOrDefault(itemstack, powerItems);
                speed += getOrDefault(itemstack, speedItems);
                if (itemstack.getItem() ==ItemsInit.upgradeItem)
                    stack = itemstack.copy();
            }
        return UpgradeItem.customizeUpgrade(stack, speed, range, power); 
    }
    
    @Override
    public int getRecipeSize() {
        return 9;
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(ItemsInit.upgradeItem);
    }
    
    private boolean contains(ItemStack stack, Map<ItemStack, Integer> source){
        return source.entrySet().stream().anyMatch(x -> x.getKey().isItemEqual(stack));
    }
    
    private Integer getOrDefault(ItemStack stack, Map<ItemStack, Integer> source){
        Optional<Map.Entry<ItemStack, Integer>> stack1 = source.entrySet().stream().filter(x -> x.getKey().isItemEqual(stack)).findFirst();
        
        return stack1.isPresent() ? stack1.get().getValue() : 0;
    }
    
    //region Helping Methods
    private boolean isSplitting(InventoryCrafting inv) {
        boolean isUpgrade = false;
        
        //Find two or more 
        for (int i = 0; i < inv.getHeight(); ++i)
            for (int j = 0; j < inv.getWidth(); ++j) {
                ItemStack itemstack = inv.getStackInRowAndColumn(j, i);
                if (itemstack.isEmpty()) continue;
                if (ItemsInit.upgradeItem == itemstack.getItem()){
                    if (isUpgrade) return true;
                    isUpgrade = true;
                }
            }
            return false;    
    }
    //endregion

}
