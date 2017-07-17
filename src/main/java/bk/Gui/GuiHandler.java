package bk.Gui;

import bk.Gui.GuiContainer.PotionCompressorGui;
import bk.Gui.GuiContainer.SingularityGUI;
import bk.Gui.TileEntity.PotionCompressorTileEntity;
import bk.Gui.TileEntity.SingularityTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * Created by User on 02.07.2017.
 */
public class GuiHandler implements IGuiHandler {
    public static final int SingularityGUIId = 1002;
    public static final int PotionGuiID = 1001;
    
    public static IGuiHandler getInstance() {
        return instance;
    }
    //Singleton
    public static IGuiHandler instance = new GuiHandler();
    
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        if (tileEntity == null || !(tileEntity instanceof IInteractionObject)) return null;
        Container container = ((IInteractionObject) tileEntity).createContainer(player.inventory, player);
    
    
        switch (ID){                
            case SingularityGUIId:
                if (tileEntity instanceof SingularityTileEntity){
                    return container;
                }
            case PotionGuiID:
                if (tileEntity instanceof PotionCompressorTileEntity)
                    return container;
        }
        
        return null;
    }
    
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        if (tileEntity == null || !(tileEntity instanceof IInteractionObject)) return null;
        Container container = ((IInteractionObject) tileEntity).createContainer(player.inventory, player);
    
        switch (ID){
            case SingularityGUIId:
                if (tileEntity instanceof SingularityTileEntity){
                return new SingularityGUI(container);
                }
            case PotionGuiID:
                if (tileEntity instanceof PotionCompressorTileEntity){
                return new PotionCompressorGui((PotionCompressorTileEntity) tileEntity, player);
                }
        }
        
        return null;
    }
}
