package bk.Utils;

import net.minecraft.item.ItemStack;

import java.util.LinkedList;

/**
 * Created by User on 24.06.2017.
 */
public class Utils {

    public static LinkedList<ItemStack> mergeItems(LinkedList<ItemStack> stacksOrigin){
        LinkedList<ItemStack> results = new LinkedList<>();

        LinkedList<ItemStack> stacks = (LinkedList<ItemStack>) stacksOrigin.clone();

        for (int i = 0; i < stacks.size(); i++){
            int finalI = i;
            //Find the same item
            if (results.stream().anyMatch(x -> stacks.get(finalI).isItemEqual(x))){
                for (int j = 0; j < results.size(); j++){
                    if (results.get(j).isItemEqual(stacks.get(i))){
                        results.get(j).grow(stacks.get(i).getCount());
                        break;
                    }
                }
            }
            else results.add(stacks.get(i));
        }

        LinkedList<ItemStack> toReturn = new LinkedList<>();
        for (ItemStack stack : results){
            while (!stack.isEmpty()){
                toReturn.add(stack.splitStack(64));
            }
        }

        //toReturn.addAll(results);
        return toReturn;
    }
}
