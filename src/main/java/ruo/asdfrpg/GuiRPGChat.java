package ruo.asdfrpg;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;

public class GuiRPGChat extends GuiChat {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0,-1,0);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.popMatrix();
    }
}
