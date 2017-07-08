package bk.Initialize;

import bk.Base.BaseVanilla.IBkBase;
import bk.Blocks.SingularityChest;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by User on 22.06.2017.
 */
public class BlocksInit  {

    //region Fields
    //public static UltimateChest ultimateChest;
    public static SingularityChest singularityChest;
    //endregion

    public static void init(){
        //ultimateChest = register(new UltimateChest());
        singularityChest = register(new SingularityChest());
    }

    //region Helping Methods
    private static <T extends Block> T register(T block, ItemBlock itemBlock) {
        GameRegistry.register(block);
        GameRegistry.register(itemBlock);


        if (itemBlock.getHasSubtypes()){
            itemBlock = ItemsInit.registerWithSubtypes(itemBlock);
        }
        if (block instanceof IBkBase) {
            ((IBkBase)block).registerItemModel();
        }

        return block;
    }

    private static <T extends Block> T register(T block) {
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());
        return register(block, itemBlock);
    }

    //endregion
}
