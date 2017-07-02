package bk.Initialize;

import bk.Base.BaseVanilla.BkAxe;
import bk.Base.BaseVanilla.IBkBase;
import bk.Base.Tools.Boer;
import bk.Base.Tools.Hammer;
import bk.BookCraft;
import bk.Items.Essense;
import bk.Items.Magnet;
import bk.Items.Tools.LavaHammer;
import bk.Items.Weapons.AngelSword;
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

    //Hammers
    public static Hammer BuilderHammer;
    public static Hammer LightHammer;
    public static Hammer Mjöllnir;
    public static Hammer WarHammer;
    public static Hammer LegendaryHammer;
    public static bk.Items.Tools.LavaHammer LavaHammer;

    //Swords
    public static AngelSword angelSword;

    //Boer
    public static Boer basedrill;
    public static Boer deepdrill;

    //Axes
    public static BkAxe axe;
    
    //Essenses
    public static Essense knowledge;
    public static Essense forgotten;
    public static Essense wisdom;
    public static Essense cosmic;
    
    public static Magnet magnet;
    //endregion

    //
    public static void init(){

        BuilderHammer = register(new Hammer(Hammer.Hammers.BUILDER_HAMMER));
        LightHammer = register(new Hammer(Hammer.Hammers.LIGHT_HAMMER));
        Mjöllnir = register(new Hammer(Hammer.Hammers.MJÖLLNIR));
        WarHammer = register(new Hammer(Hammer.Hammers.WAR_HAMMER));
        LegendaryHammer = register(new Hammer(Hammer.Hammers.LEGENDARY_HAMMER));
        LavaHammer = register(new LavaHammer());
        angelSword = register(new AngelSword());
        knowledge = register(new Essense(Essense.Essenses.knowledge));
        forgotten = register(new Essense(Essense.Essenses.forgotten));
        wisdom = register(new Essense(Essense.Essenses.wisdom));
        cosmic = register(new Essense(Essense.Essenses.cosmic));
        basedrill = register(new Boer(Boer.Boers.BASE_DRILL));
        deepdrill = register(new Boer(Boer.Boers.DEEP_DRILL));
        magnet = register(new Magnet(5, true));
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
