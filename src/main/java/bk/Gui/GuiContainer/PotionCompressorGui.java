package bk.Gui.GuiContainer;

import bk.Base.Entity.TileEntity.BaseMachineUpgradeTileEntity;
import bk.Base.GUI.BaseCookingGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * Created by User on 16.07.2017.
 */
public class PotionCompressorGui extends BaseCookingGui {
    
    /**
     * TileEntity must implement IInteractionObject!!!
     *
     * @param tileEntity
     * @param player
     */
    public PotionCompressorGui(BaseMachineUpgradeTileEntity tileEntity, EntityPlayer player) {
        super(tileEntity, player);
    }
    
    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation("bk:textures/gui/potioncompressor.png");
    }
    
    @Override
    public int getWidth() {
        return 175;
    }
    
    @Override
    public int getHeight() {
        return 208;
    }
    
    @Override
    public int getFireWidth() {
        return 80;
    }
    
    @Override
    public int getFireHeight() {
        return 79;
    }
    
    @Override
    public int getCookWidth() {
        return 115;
    }
    
    @Override
    public int getCookHeight() {
        return 34;
    }
}
