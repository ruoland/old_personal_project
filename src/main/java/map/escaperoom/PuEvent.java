package map.escaperoom;

import cmplus.deb.DebAPI;
import map.lopre2.CommandJB;
import map.lopre2.jump1.EntityLavaBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class PuEvent {
    public static TextureAtlasSprite text, grim;
    @SubscribeEvent
    public void event(TextureStitchEvent event){
        text = event.getMap().registerSprite(new ResourceLocation("minigame:text"));
        grim = event.getMap().registerSprite(new ResourceLocation("minigame:grim"));
    }
    @SubscribeEvent
    public void event(PlayerInteractEvent.RightClickEmpty event){
//        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleLarge(event.getWorld(), event.getEntityLiving(), 0,0,0));
//        event.getWorld().spawnParticle(EnumParticleTypes.FLAME, 0,0,0,0,0,0);
    }


    @SubscribeEvent
    public void event(MouseEvent event) {
        if (EntityRoomBlockJumpMap.isTeleport) {
            if (event.getDwheel() == 120) {
                if (DebAPI.isKeyDown(Keyboard.KEY_LCONTROL)) {
                    EntityLavaBlock.ax -= 0.3;
                } else
                    EntityLavaBlock.ax -= 0.05;
            }
            if (event.getDwheel() == -120) {
                if (DebAPI.isKeyDown(Keyboard.KEY_LCONTROL)) {
                    EntityLavaBlock.ax += 0.3;
                } else
                    EntityLavaBlock.ax += 0.05;
            }
        }
    }
}
