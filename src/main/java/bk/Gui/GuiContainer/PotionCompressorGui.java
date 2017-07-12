package bk.Gui.GuiContainer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

/**
 * Created by User on 09.07.2017.
 */
public class PotionCompressorGui extends GuiContainer {
    
    private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation("bk:textures/gui/potioncompressor.png");
    /**
     * The player inventory bound to this GUI.
     */
//    private final InventoryPlayer playerInventory;
//    private final IInventory tileFurnace;
    
    public PotionCompressorGui(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

//    public PotionCompressorGui(InventoryPlayer playerInv, IInventory furnaceInv) {
//        super(new SingularityContainer(playerInv, furnaceInv));
//        this.playerInventory = playerInv;
//        this.tileFurnace = furnaceInv;
//    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        
    }
}
