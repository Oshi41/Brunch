package bk.Gui.GuiContainer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

/**
 * Created by User on 10.07.2017.
 */
public class SingularityGUI extends GuiContainer {
    
    private final static ResourceLocation texture = new ResourceLocation("bk:textures/gui/ultimatechest.png");
    private final int width = 183;
    private final int height = 183;
    
    public SingularityGUI(Container inventorySlotsIn) {
        super(inventorySlotsIn);        
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        // Bind the image texture of our custom container
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        // Draw the image
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, width, height);
    }
}
