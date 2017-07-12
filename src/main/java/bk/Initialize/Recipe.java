package bk.Initialize;

import bk.Recipes.CraftingRecipes.UpgradeRecipe;
import net.minecraft.item.crafting.CraftingManager;

/**
 * Created by User on 10.07.2017.
 */
public class Recipe {
    public static void init(){
        CraftingManager.getInstance().addRecipe(new UpgradeRecipe());
    }
}
