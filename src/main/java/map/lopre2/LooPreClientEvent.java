package map.lopre2;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.gameevent.InputEvent;
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

import java.security.Key;


public class LooPreClientEvent {

    @SubscribeEvent
    public void client(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null && mc.objectMouseOver != null && mc.currentScreen == null) {
            if (mc.objectMouseOver.entityHit instanceof EntityPreBlock) {
                if (WorldAPI.equalsHeldItem(mc.thePlayer, LoPre2.itemCopy)) {
                    System.out.println(mc.gameSettings.keyBindSprint.getKeyCode());

                    if(ItemCopy.getDistance() > 0 && LoPre2.KEY_MINUS.isPressed()) {//-키
                        ItemCopy.setDistance((int) (ItemCopy.getDistance()-1));
                    }
                    if(LoPre2.KEY_ADD.isPressed() || LoPre2.KEY_ADD_2.isPressed()) {//+키
                        ItemCopy.setDistance((int) (ItemCopy.getDistance()+1));
                        System.out.println("snfma");
                    }

                }
            }
        }
    }

    @SubscribeEvent
    public void client(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null && mc.objectMouseOver != null && mc.currentScreen == null) {
            if (mc.objectMouseOver.entityHit instanceof EntityPreBlock && (WorldAPI.equalsHeldItem(mc.thePlayer, LoPre2.itemSpanner) || WorldAPI.equalsHeldItem(mc.thePlayer, LoPre2.itemCopy))) {
                EntityPreBlock preBlock = (EntityPreBlock) mc.objectMouseOver.entityHit;
                RenderAPI.drawTextureZ("looppre2:textures/text.png", 0.5F, 0,80, 0, mc.displayWidth / 2 - 300, mc.displayHeight / 2 - 180);
                GlStateManager.pushMatrix();
                ;
                GlStateManager.translate(0, 0, 10);
                if (WorldAPI.equalsHeldItem(mc.thePlayer, LoPre2.itemSpanner)) {
                    mc.fontRendererObj.drawSplitString(preBlock.getJumpName(), 0, 80, mc.displayWidth / 2 - 300, 0xFFFFFF);
                    mc.fontRendererObj.drawSplitString(preBlock.getText(), 0, 95, mc.displayWidth / 2 - 300, 0xFFFFFF);
                } else if (WorldAPI.equalsHeldItem(mc.thePlayer, LoPre2.itemCopy)) {
                    mc.fontRendererObj.drawSplitString("복사 거리:" + ItemCopy.getDistance() + " 복사 간격:" + ItemCopy.getInterval(), 0, 80, mc.displayWidth / 2 - 300, 0xFFFFFF);
                    mc.fontRendererObj.drawSplitString("+키나 -키로 복사 거리를 변경할 수 있습니다.      채팅에 /jb bu 입력하면 복사된 블럭을 삭제합니다.", 0, 95, mc.displayWidth / 2 - 320, 0xFFFFFF);
                }
                GlStateManager.popMatrix();
            }
        }
    }

}
