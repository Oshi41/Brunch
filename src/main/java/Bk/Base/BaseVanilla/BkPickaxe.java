package Bk.Base.BaseVanilla;

import Bk.BookCraft;
import net.minecraft.item.ItemPickaxe;

/**
 * Created by User on 23.06.2017.
 */
public class BkPickaxe extends ItemPickaxe implements IBkBase {

    protected String name;
    public static BkPickaxe instance = null;

    public BkPickaxe(String name, ToolMaterial material){
        super(material);
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
    }


    public void registerItemModel() {
        BookCraft.proxy.registerItemRenderer(this, 0, name);
    }
}
