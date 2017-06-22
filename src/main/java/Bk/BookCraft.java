package Bk;

import Bk.Proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

/**
 * Created by User on 22.06.2017.
 */
@Mod(modid = "Bk",
name = "BookCraft",
version = "0.1")
public class BookCraft {

    //region Fields
    @SidedProxy(clientSide = "Bk.Proxy.ClientProxy",
                serverSide = "Bk.Proxy.ServerProxy")
    public static CommonProxy proxy;

    public static String MOD_ID = "Bk";

    //region Creative tabs
    public static final CreativeTabs toolTab = new CreativeTabs("Tools") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Items.IRON_PICKAXE);
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
