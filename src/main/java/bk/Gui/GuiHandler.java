package bk.Gui;

import bk.Gui.GuiContainer.UniversalGuiContainer;
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
    public static final int UltimateGUIId = 1001;
    public static final int SingularityGUIId = 1002;
    
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
//            case UltimateGUIId:
//                if (tileEntity instanceof UnlimitedTileEntity) {
//                    return container;
//                }
                
            case SingularityGUIId:
                if (tileEntity instanceof SingularityTileEntity){
                    return container;
                }
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
//            case UltimateGUIId:
//                if (tileEntity instanceof UnlimitedTileEntity) {
//                    return new UniversalGuiContainer(container).
//                            customize(UniversalGuiContainer.GUITypes.SINGULARITY);
//                }
            case SingularityGUIId:
                if (tileEntity instanceof SingularityTileEntity){
                    return new UniversalGuiContainer(container).
                            customize(UniversalGuiContainer.GUITypes.SINGULARITY);
                }
        }
        
        return null;
    }
}
