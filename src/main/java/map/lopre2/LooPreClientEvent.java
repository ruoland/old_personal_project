package map.lopre2;

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
        if(mc.thePlayer != null && mc.objectMouseOver != null) {
            if (mc.objectMouseOver.entityHit instanceof EntityPreBlock) {
                RenderAPI.drawTexture("looppre2:textures/text.png", 0.5F, 0, 100, mc.displayWidth / 2 - 300, mc.displayHeight / 2 - 200);
                mc.fontRendererObj.drawSplitString("아아 테스트 테테테테테테",10,110,100,0xFFFFFF);
            }
        }
    }

}
