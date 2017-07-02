package bk.Gui.GuiContainer;

import bk.Gui.Container.UnlimitedContainer;
import bk.Gui.TileEntity.UnlimitedTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * Created by User on 02.07.2017.
 */
public class UltimateGuiContainer extends GuiContainer {
    
    private static final ResourceLocation texture = new ResourceLocation("bk:textures/gui/ultimatechest.png");
    
    public UltimateGuiContainer(InventoryPlayer invPlayer, UnlimitedTileEntity tile) {
        super(new UnlimitedContainer(invPlayer, tile));
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        // Bind the image texture of our custom container
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        // Draw the image
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 183, 183);
    }
}
