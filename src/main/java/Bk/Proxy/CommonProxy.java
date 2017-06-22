package Bk.Proxy;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.*;

/**
 * Created by User on 22.06.2017.
 */
public class CommonProxy {

    //region Events
    public void onServerStarting(FMLServerStartingEvent event) {
    }

    public void onPreInit(FMLPreInitializationEvent event) {
    }

    public void onInit(FMLInitializationEvent event) {
    }

    public void onPostInit(FMLPostInitializationEvent event) {
    }

    public void onServerStopping(FMLServerStoppingEvent event) {
    }
    //endregion

    public void registerItemRenderer(Item item, int meta, String id) {
    }
}
