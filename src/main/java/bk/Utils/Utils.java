package bk.Utils;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

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
                    if (results.get(j).isItemEqual(stacks.get(i)) && results.get(j).getMetadata() == stacks.get(i).getMetadata()){
                        results.get(j).grow(stacks.get(i).getCount());
                        break;
                    }
                }
            }
            else results.add(stacks.get(i));
        }
        
        return results;
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
}
