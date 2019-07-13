package map.lopre2;

import net.minecraft.client.renderer.GlStateManager;
import olib.api.BlockAPI;
import olib.api.RenderAPI;
import olib.api.WorldAPI;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;


public class LooPreClientEvent {
    @SubscribeEvent
    public void client(TickEvent.RenderTickEvent event){
        Minecraft mc= Minecraft.getMinecraft();
        if(mc.thePlayer != null && mc.objectMouseOver != null && mc.currentScreen == null && WorldAPI.equalsHeldItem(mc.thePlayer, LoPre2.itemSpanner) ) {
            if (mc.objectMouseOver.entityHit instanceof EntityPreBlock) {
                EntityPreBlock preBlock = (EntityPreBlock) mc.objectMouseOver.entityHit;
                int x = CommandJB.x;
                int y = CommandJB.y;
                int w = CommandJB.width;
                int h = CommandJB.height;
                RenderAPI.drawTextureZ("looppre2:textures/text.png", 0.5F, x, y, 0,mc.displayWidth / 2 -300, mc.displayHeight / 2 - 180);
                GlStateManager.pushMatrix();;
                GlStateManager.translate(0,0,10);
                mc.fontRendererObj.drawSplitString(preBlock.getJumpName(),0,80,mc.displayWidth / 2 -300,0xFFFFFF);
                mc.fontRendererObj.drawSplitString(preBlock.getText(),0,95,mc.displayWidth / 2 -300,0xFFFFFF);

                GlStateManager.popMatrix();
            }
        }
    }

}
