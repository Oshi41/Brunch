package bk.Initialize;

import bk.Base.BaseVanilla.BkAxe;
import bk.Base.BaseVanilla.IBkBase;
import bk.Base.Tools.Boers;
import bk.Base.Tools.Hammer;
import bk.BookCraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by User on 22.06.2017.
 */
public class ItemsInit  {

    //region Fields
    public static Hammer BuilderHammer;
    public static Hammer LightHammer;
    public static Hammer Mjöllnir;
    public static Hammer WarHammer;
    public static Hammer LegendaryHammer;
    public static Boers boer;
    public static BkAxe axe;
    //endregion

    //
    public static void init(){

        BuilderHammer = register(new Hammer(Hammer.Hammers.BUILDER_HAMMER));
        LightHammer = register(new Hammer(Hammer.Hammers.LIGHT_HAMMER));
        Mjöllnir = register(new Hammer(Hammer.Hammers.MJÖLLNIR));
        WarHammer = register(new Hammer(Hammer.Hammers.WAR_HAMMER));
        LegendaryHammer = register(new Hammer(Hammer.Hammers.LEGENDARY_HAMMER));
    }

    //region Helping Method
    private static <T extends Item> T register(T item) {
        GameRegistry.register(item);

        if (item instanceof IBkBase) {
            if (item.getHasSubtypes()){
                item = registerWithSubtypes(item);
            }
            else
                ((IBkBase) item).registerItemModel();
        }

        return item;
    }

    public static <T extends Item> T registerWithSubtypes(T item){

        NonNullList<ItemStack> items = NonNullList.create();
        item.getSubItems(item, item.getCreativeTab(), items);

        for (int i = 0; i < items.size(); i++){
            Item tempItem = items.get(i).getItem();
                String name = tempItem.getUnlocalizedName().substring(5);
                ModelLoader.setCustomModelResourceLocation(tempItem, i,
                        new ModelResourceLocation(BookCraft.MOD_ID + ":" + name, "inventory"));
            }
        return item;
    }
    //endregion
}
