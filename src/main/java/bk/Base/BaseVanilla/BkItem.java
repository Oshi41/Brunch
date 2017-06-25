package bk.Base.BaseVanilla;

import bk.BookCraft;
import net.minecraft.item.Item;

/**
 * Created by User on 23.06.2017.
 */
public class BkItem extends Item implements IBkBase {

    protected String name;
    public static BkItem instance = null;

    public BkItem(String name){
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
    }


    @Override
    public String getName() {
        return name;
    }

    public void registerItemModel() {
        BookCraft.proxy.registerItemRenderer(this, 0, name);
    }
}
