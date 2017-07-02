package bk.Proxy;

import bk.BookCraft;
import bk.Gui.GuiHandler;
import bk.Gui.TileEntity.UnlimitedTileEntity;
import bk.Initialize.PacketRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by User on 22.06.2017.
 */
public class CommonProxy {
    
    public static SimpleNetworkWrapper simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("BookCraftChannel");
    
    
    //region Events
    public void onServerStarting(FMLServerStartingEvent event) {
        
    }
    
    public void onPreInit(FMLPreInitializationEvent event) {        
        PacketRegistry.init(simpleNetworkWrapper);
        GameRegistry.registerTileEntity(UnlimitedTileEntity.class, "UnlimitedTileEntity");
        NetworkRegistry.INSTANCE.registerGuiHandler(BookCraft.instance, GuiHandler.getInstance());
    }
    
    public void onInit(FMLInitializationEvent event) {
        
    }
    
    public void onPostInit(FMLPostInitializationEvent event) {
        
    }
    
    public void onServerStopping(FMLServerStoppingEvent event) {
        
    }
    //endregion
    
    @SideOnly(Side.CLIENT)
    public void registerItemRenderer(Item item, int meta, String id) {
        
    }
}
