package bk.Gui.GuiContainer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

/**
 * Created by User on 07.07.2017.
 */
public class UniversalGuiContainer extends GuiContainer {
    
    
    private static ResourceLocation texture;
    private int width;
    private int height;
    
    public UniversalGuiContainer(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }
    
    public UniversalGuiContainer customize(GUITypes type){
        texture = new ResourceLocation(type.resource);
        width = type.width;
        height = type.height;
        return this;
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        // Bind the image texture of our custom container
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        // Draw the image
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, width, height);
    }
    
    public enum GUITypes{
        SINGULARITY("bk:textures/gui/ultimatechest.png", 183,183);
        
        public final String resource;
        public final int width;
        public final int height;
        
        GUITypes(String resource, int width, int height){
            this.resource = resource;
            this.width = width;
            this.height = height;
        } 
    }
}
