package Bk.Initialize;

import Bk.Base.BkBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by User on 22.06.2017.
 */
public class BlocksInit  {

    //region Fields
    //endregion

    public static void init(){

    }

    //region Helping Methods
    private static <T extends Block> T register(T block, ItemBlock itemBlock) {
        GameRegistry.register(block);
        GameRegistry.register(itemBlock);

        if (block instanceof BkBlock) {
            ((BkBlock)block).registerItemModel(itemBlock);
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
