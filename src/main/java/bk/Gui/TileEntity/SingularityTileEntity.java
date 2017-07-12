package bk.Gui.TileEntity;

import bk.Base.Entity.TileEntity.BaseUnlimitedTileEntity;
import bk.Gui.Container.SingularityContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

/**
 * Created by User on 07.07.2017.
 */
public class SingularityTileEntity extends BaseUnlimitedTileEntity {
    
    //Just need, do not delete!!!
    public SingularityTileEntity(){
        super(81, "bk:singularitychest");
    }
    
    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new SingularityContainer(this, playerIn);
    }
    
    @Override
    public String getGuiID() {
        return "bk:" + getName();
    }
}
