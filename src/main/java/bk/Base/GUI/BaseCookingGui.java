package bk.Base.GUI;

import bk.Base.Entity.TileEntity.BaseMachineUpgradeTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * Created by User on 16.07.2017.
 */
public abstract class BaseCookingGui extends GuiContainer {
    
    private final EntityPlayer player;
    private final BaseMachineUpgradeTileEntity tileEntity;    
    //public BaseMachineUpgradeTileEntity getTileEntity() {return tileEntity;}    
    public EntityPlayer getPlayer() {return player;}
    
    //region Abstract
    public abstract ResourceLocation getTexture();  
    public abstract int getWidth();
    public abstract int getHeight();
    
    public abstract int getFireWidth();    
    public abstract int getFireHeight();
    
    public abstract int getCookWidth();    
    public abstract int getCookHeight();
    //endregion
    
    /**
     * TileEntity must implement IInteractionObject!!!
     *
     * @param tileEntity
     * @param player
     */
    public BaseCookingGui(BaseMachineUpgradeTileEntity tileEntity, EntityPlayer player) {
        super(tileEntity.createContainer(player.inventory, player));
        this.player = player;
        this.tileEntity = tileEntity;
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        //BackGround
        Minecraft.getMinecraft().getTextureManager().bindTexture(getTexture());
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, getWidth(), getHeight());
    
        if (tileEntity.isBurning()) {
            int k = this.getBurnLeftScaled(13);
            this.drawTexturedModalRect(guiLeft + getFireWidth(), guiTop + getFireHeight() + 12 - k,
                    176, 12 - k, 14, k + 1);
        }
    
        int l = this.getCookProgressScaled(24);
        this.drawTexturedModalRect(guiLeft + getCookWidth(), guiTop + getCookHeight(), 176, 14, l + 1, 16);
    }
    
    private int getCookProgressScaled(int pixels) {
        int i = tileEntity.getField(1);
        int j = tileEntity.getField(0);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }
    
    private int getBurnLeftScaled(int pixels) {
        int i = tileEntity.getField(3);
        if (i == 0) {
            i = 200;
        }
        
        return tileEntity.getField(2) * pixels / i;
    }
}
