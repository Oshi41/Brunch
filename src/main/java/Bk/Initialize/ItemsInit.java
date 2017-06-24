package Bk.Initialize;

import Bk.Base.BaseVanilla.BkAxe;
import Bk.Base.BaseVanilla.BkItem;
import Bk.Base.Tools.Boers;
import Bk.Base.Tools.Hammer;
import Bk.BookCraft;
import com.sun.glass.ui.Size;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by User on 22.06.2017.
 */
public class ItemsInit  {

    //region Fields
    public static Hammer hammes;
    public static Boers boer;
    public static BkAxe axe;
    //endregion

    public static void init(){

        hammes = registerWithSubtypes(new Hammer(Hammer.Hammers.LEGENDARY_HAMMER));
        boer = register(new Boers("boer", new Size(5,3), Item.ToolMaterial.DIAMOND));
        axe = register(new BkAxe("axe", Item.ToolMaterial.DIAMOND, 7, 7,1000,20));
    }

    //region Helping Method
    private static <T extends Item> T register(T item) {
        GameRegistry.register(item);

        if (item instanceof BkItem) {
            ((BkItem)item).registerItemModel();
        }

        return item;
    }

    private static <T extends Item> T registerWithSubtypes(T item){
        NonNullList<ItemStack> items = NonNullList.create();

        item.getSubItems(item, item.getCreativeTab(), items);
        int i = 0;
        for(ItemStack stack : items){
            BookCraft.proxy.registerItemRenderer(stack.getItem(), i,
                    stack.getItem().getRegistryName().getResourcePath());
        }
        return register(item);
    }
    //endregion
}
