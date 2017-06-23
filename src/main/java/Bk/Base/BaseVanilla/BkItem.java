package Bk.Base.BaseVanilla;

import Bk.BookCraft;
import net.minecraft.item.Item;

/**
 * Created by User on 23.06.2017.
 */
public class BkItem extends Item {

    protected String name;
    public static BkItem instance = null;

    public BkItem(String name){
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
    }


    public void registerItemModel() {
        BookCraft.proxy.registerItemRenderer(this, 0, name);
    }
}
