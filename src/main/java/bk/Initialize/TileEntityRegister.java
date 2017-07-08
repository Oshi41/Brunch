package bk.Initialize;

import bk.Gui.TileEntity.SingularityTileEntity;
import bk.Gui.TileEntity.UnlimitedTileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by User on 07.07.2017.
 */
public class TileEntityRegister {
    
    public static void init(){
        GameRegistry.registerTileEntity(UnlimitedTileEntity.class, "UnlimitedTileEntity");
        GameRegistry.registerTileEntity(SingularityTileEntity.class,"bk:SingularityTileEntity");
    }
}
