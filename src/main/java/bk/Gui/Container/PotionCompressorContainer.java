package bk.Gui.Container;

import bk.Base.GUI.Container.BaseContainer;
import bk.Gui.TileEntity.PotionCompressorTileEntity;
import bk.Utils.SlotFactory;
import bk.Utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;

import java.awt.*;

/**
 * Created by User on 13.07.2017.
 */
public class PotionCompressorContainer extends BaseContainer {

    
    //region Drawind
    private final int size = 16; 
    //endregion
    
    public PotionCompressorContainer(PotionCompressorTileEntity tileEntity, EntityPlayer player) {
        super(tileEntity, player);
        
        //0 - upgrade
        addSlotToContainer(SlotFactory.getUpgradeSlot(tileEntity, 0, 8, 9).setTakePredicat(x -> tileEntity.isCooking()));
        
        //1,2 - buckets
        addSlotToContainer(SlotFactory.getWaterBucketSlot(tileEntity, 1, 8, 53).setTakePredicat(x -> tileEntity.isCooking()));
        addSlotToContainer(SlotFactory.getWaterBucketSlot(tileEntity, 2, 26, 53).setTakePredicat(x -> tileEntity.isCooking()));
        
        //3 - fuel
        addSlotToContainer(new SlotFurnaceFuel(tileEntity, 3, 81, 97));
        
        int index = 4;
        // 4 - 12 crafting slots
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++){                
                int x = 60 + j * 16;
                int y = 16 + i * 16;
                addSlotToContainer(SlotFactory.getPotionSlot(tileEntity, index, x, y).
                        setTakePredicat(temp -> tileEntity.isCooking()));
                index++;
            }
            
        //13 - output
        addSlotToContainer(SlotFactory.getOutupSlot(tileEntity, 13, 146, 34));
        
        //player inventory
        Slot[] slots = Utils.getPlayerSlots(player.inventory, new Point(7,183), new Point(7,125));
        for (int i = 0; i < slots.length; i++)
            addSlotToContainer(slots[i]);
    }
    
    @Override
    public int getTileSlotAmount() {
        return 14;
    }
    
    @Override
    public int getSlotAmout() {
        return 9 * 4 + getTileSlotAmount();
    }
}
