package Bk.Initialize;

import Bk.Base.BkItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by User on 22.06.2017.
 */
public class ItemsInit  {

    //region Fields
    //endregion

    public static void init(){

    }

    //region Helping Method
    private static <T extends Item> T register(T item) {
        GameRegistry.register(item);

        if (item instanceof BkItem) {
            ((BkItem)item).registerItemModel();
        }

        return item;
    }
    //endregiuon
}
