package bk.Proxy;

import bk.BookCraft;
import bk.Events.EventHook;
import bk.Initialize.BlocksInit;
import bk.Initialize.ItemsInit;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.*;

/**
 * Created by User on 22.06.2017.
 */
public class ClientProxy extends CommonProxy {

    //region Events
    @Override
    public void onServerStarting(FMLServerStartingEvent event) {
        super.onServerStarting(event);
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        super.onPreInit(event);
        MinecraftForge.EVENT_BUS.register(new EventHook());       

        ItemsInit.init();
        BlocksInit.init();
    }

    @Override
    public void onInit(FMLInitializationEvent event) { super.onInit(event);   }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) { 
        super.onPostInit(event);
    }

    @Override
    public void onServerStopping(FMLServerStoppingEvent event) {
        super.onServerStopping(event);
    }
    //endregion
    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelResourceLocation location = new ModelResourceLocation(BookCraft.MOD_ID + ":" + id, "inventory");
        ModelLoader.setCustomModelResourceLocation(item, meta, location);
    }
}
