package Bk.Proxy;

import Bk.BookCraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
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
        //MinecraftForge.EVENT_BUS.register(new SwordEvents());


//        BlocksInit.register();
//        ItemsInit.register();
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        super.onInit(event);

//        BlocksInit.registerRenders();
//        ItemsInit.registerRenders();
    }

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
        ModelLoader.setCustomModelResourceLocation(item, meta,
                new ModelResourceLocation(BookCraft.MOD_ID + ":" + id, "inventory"));
    }
}
