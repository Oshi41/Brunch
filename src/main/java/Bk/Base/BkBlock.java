package Bk.Base;

import Bk.BookCraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

/**
 * Created by User on 22.06.2017.
 */
public class BkBlock extends Block {

    protected String name;
    public static BkBlock instance = null;

    public BkBlock(Material blockMaterialIn, String name) {

        super(blockMaterialIn);
        this.name = name;

        setUnlocalizedName(name);
        setRegistryName(name);
    }


    public void registerItemModel(ItemBlock itemBlock){
        BookCraft.proxy.registerItemRenderer(itemBlock, 0, name);
    }
}
