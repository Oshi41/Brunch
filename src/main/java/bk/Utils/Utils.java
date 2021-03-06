package bk.Utils;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by User on 24.06.2017.
 */
public class Utils {
    
    /**
     * Returns distinkt itemStacks with amount of items
     * @param stacksOrigin
     * @return
     */
    public static LinkedList<ItemStack> mergeItems(LinkedList<ItemStack> stacksOrigin){
        LinkedList<ItemStack> results = new LinkedList<>();

        LinkedList<ItemStack> stacks = (LinkedList<ItemStack>) stacksOrigin.clone();

        for (int i = 0; i < stacks.size(); i++){
            int finalI = i;
            //Find the same item
            if (results.stream().anyMatch(x -> stacks.get(finalI).isItemEqual(x))){
                for (int j = 0; j < results.size(); j++){
                    if (results.get(j).isItemEqual(stacks.get(i)) && 
                            ItemStack.areItemStackTagsEqual(results.get(j), stacks.get(i))){
                        results.get(j).grow(stacks.get(i).getCount());
                        break;
                    }
                }
            }
            else results.add(stacks.get(i));
        }
        
        return results;
    }
    
    public static NonNullList<ItemStack> mergeItems(NonNullList<ItemStack> source){
        LinkedList<ItemStack> items = mergeItems(new LinkedList<>(source));
        NonNullList<ItemStack> toReturn = NonNullList.withSize(source.size(), ItemStack.EMPTY);
        int min = Math.min(items.size(), toReturn.size());
        for (int i = 0; i < min; i++){
            toReturn.set(i, items.get(i));
        }
        
        return toReturn;
    }
    
    /**
     * Destroy whole area with given positions and return unsorted array of items
     * For sorting use {@link Utils#mergeItems(LinkedList)}
     * @param world
     * @param poses
     * @param stack
     * @param player
     * @return
     */
    public static ArrayList<ItemStack> destroyArea(World world, ArrayList<BlockPos> poses, ItemStack stack, EntityPlayer player){
        ArrayList<ItemStack> result = new ArrayList<>();
    
        for (BlockPos blockPos : poses) {
            if (ForgeHooks.canToolHarvestBlock(world, blockPos, stack)) {
                Block block = world.getBlockState(blockPos).getBlock();
                List<ItemStack> tempDrop = block.getDrops(world, blockPos,
                        world.getBlockState(blockPos),
                        EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand()));
                if (block.removedByPlayer(world.getBlockState(blockPos), world, blockPos, player, true)) {
                    if (tempDrop.size() > 0)
                        result.addAll(tempDrop);
                }
            }
        }
        
        return result;
    }    
    
    //region NBT HELPER
    
    /**
     * Override it to write correct stack size
     *
     * @param tag
     * @param list
     * @return
     */
    public static NBTTagCompound saveAllItems(NBTTagCompound tag, NonNullList<ItemStack> list) {
        NBTTagList nbttaglist = new NBTTagList();
        
        for (int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = list.get(i);
            
            if (!itemstack.isEmpty()) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) i);
                itemstack.writeToNBT(nbttagcompound);
                writeStackCount(nbttagcompound, itemstack);
                nbttaglist.appendTag(nbttagcompound);
            }
        }
        
        if (!nbttaglist.hasNoTags()) {
            tag.setTag("Items", nbttaglist);
        }
        
        return tag;
    }
    
    /**
     * Replace old tag with integer value to write correct stack size.
     */
    public static void writeStackCount(NBTTagCompound tagCompound, ItemStack stack) {
        if (tagCompound.hasNoTags() || !tagCompound.hasKey("Count")) return;
        
        tagCompound.removeTag("Count");
        tagCompound.setInteger("Count", stack.getCount());
    }
    
    public static void loadAllItems(NBTTagCompound tag, NonNullList<ItemStack> list) {
        NBTTagList nbttaglist = tag.getTagList("Items", 10);
        
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;
            
            if (j >= 0 && j < list.size()) {
                ItemStack stack = new ItemStack(nbttagcompound);
                stack.setCount(nbttagcompound.getInteger("Count"));
                list.set(j, stack);
            }
        }
    }
    
    //endregion
    
    public static Slot[] getPlayerSlots(IInventory playerInv, Point hotbarPos, Point mainPos){
        ArrayList<Slot> slots = new ArrayList<>();
        
        for (int i = 0; i < 9; i++){
            slots.add(new Slot(playerInv, i, hotbarPos.x + i*16, hotbarPos.y));
        }
        for (int j = 0; j < 3; j++)
            for (int i = 0; i < 9; i++){
                int index = 9 + i + (j * 9);
                int x = mainPos.x + i*16;
                int y = mainPos.y + j*16;
                slots.add(new Slot(playerInv, index, x,y));
            }
        
        Slot[] result = new Slot[slots.size()];
        return slots.toArray(result);
    }
}
