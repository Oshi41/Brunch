package bk.Gui;

import bk.Gui.Container.UnlimitedContainer;
import bk.Gui.GuiContainer.UltimateGuiContainer;
import bk.Gui.TileEntity.UnlimitedTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * Created by User on 02.07.2017.
 */
public class GuiHandler implements IGuiHandler {
    public static final int UltimateGUIId = 1001;
    
    public static IGuiHandler getInstance() {
        return instance;
    }
    //Singleton
    public static IGuiHandler instance = new GuiHandler();
    
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        
        switch (ID){
            case UltimateGUIId:
                BlockPos xyz = new BlockPos(x, y, z);
                TileEntity tileEntity = world.getTileEntity(xyz);
                if (tileEntity instanceof UnlimitedTileEntity) {
                    UnlimitedTileEntity tileEntityInventoryBasic = (UnlimitedTileEntity) tileEntity;
                    return new UnlimitedContainer(player.inventory, tileEntityInventoryBasic);
                }
        }
        
        
        return null;
    }
    
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        
        switch (ID){
            case UltimateGUIId:
                BlockPos xyz = new BlockPos(x, y, z);
                TileEntity tileEntity = world.getTileEntity(xyz);
                if (tileEntity instanceof UnlimitedTileEntity) {
                    UnlimitedTileEntity tileEntityInventoryBasic = (UnlimitedTileEntity) tileEntity;
                    return new UltimateGuiContainer(player.inventory, tileEntityInventoryBasic);
                }
        }
        
        return null;
    }
}
