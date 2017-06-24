package Bk.Initialize;

import Bk.Base.BaseVanilla.BkItem;
import Bk.Base.Tools.Hammer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by User on 22.06.2017.
 */
public class ItemsInit  {

    ArrayList<Class> classes = new ArrayList<Class>(Arrays.asList(
            BkItem.class
    ));

    //region Fields
    public static Hammer hammes;
    //endregion

    public static void init(){
        hammes = register(new Hammer(Hammer.Hammers.BUILDER_HAMMER));
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
        for(ItemStack stack : items){

        }
        return register(item);
    }
    //endregiuon
}
