package bk;

import bk.Proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

/**
 * Created by User on 22.06.2017.
 */
@Mod(modid = BookCraft.MOD_ID)
public class BookCraft {

    //region Fields
    @SidedProxy(clientSide = BookCraft.MOD_ID + ".Proxy.ClientProxy",
            serverSide = BookCraft.MOD_ID + ".Proxy.CommonProxy")
    public static CommonProxy proxy;

    public final static String MOD_ID = "bk";

    @Mod.Instance(BookCraft.MOD_ID)
    public static BookCraft instance;

    //region Creative tabs
    public static final CreativeTabs toolTab = new CreativeTabs("Tools") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Items.IRON_PICKAXE);
        }
    };
    public static final CreativeTabs swordsTab = new CreativeTabs("Melee weapon") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Items.IRON_SWORD);
        }
    };
    public static final CreativeTabs rangedTabs = new CreativeTabs("Ranged weapon") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Items.BOW);
        }
    };
    //endregion

    //endregion

    //region Loading Events

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        proxy.onServerStarting(event);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        proxy.onPreInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.onInit(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.onPostInit(event);
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        proxy.onServerStopping(event);
    }

    //endregion
}
