package com.ruoland.customclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import ruo.minigame.api.RenderAPI;

import java.io.File;

public class GuiCusButton extends GuiButton {
    public boolean canEdit = true;
    public ResourceLocation dynamicLocation;
    public ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");

    public GuiCusButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiCusButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        this(buttonId, x, y, widthIn, heightIn, buttonText, true);
    }
    public GuiCusButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, boolean canEdit) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.canEdit = canEdit;

    }
    @Override
    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        super.drawTexturedModalRect(x, y, textureX, textureY, width, height);
    }

    /**
     * Draws this button to the screen.
     */
    private Minecraft mc = Minecraft.getMinecraft();
    float updateCounter;

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (buttonTextures.toString().startsWith("https://") || buttonTextures.toString().startsWith("http://")) {
            File f = new File("resourcepacks/CustomClient/assets/customclient/textures/gui/");
            f.mkdirs();
            buttonTextures = new ResourceLocation("customclient:textures/gui/" + URLDownload.download("CustomClient", buttonTextures.toString(),
                    "resourcepacks/CustomClient/assets/customclient/textures/gui/"));
        }
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            if (!buttonTextures.toString().equals("minecraft:textures/gui/widgets.png")) {
                if (dynamicLocation != null)
                    RenderAPI.drawTexture(dynamicLocation.toString(), 1.0F, this.xPosition, this.yPosition, this.width, this.height);
                else
                    RenderAPI.drawTexture(buttonTextures.toString(), 1.0F, this.xPosition, this.yPosition, this.width, this.height);
            } else {
                this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
                this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            }
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (packedFGColour != 0) {
                j = packedFGColour;
            } else if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
        }
    }


}
