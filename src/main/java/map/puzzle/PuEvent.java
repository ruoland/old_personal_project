package map.puzzle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PuEvent {
    public static TextureAtlasSprite text, grim;
    @SubscribeEvent
    public void event(TextureStitchEvent event){
        text = event.getMap().registerSprite(new ResourceLocation("minigame:text"));
        grim = event.getMap().registerSprite(new ResourceLocation("minigame:grim"));
    }
    @SubscribeEvent
    public void event(PlayerInteractEvent.RightClickEmpty event){
        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleLarge(event.getWorld(), event.getEntityLiving(), 0,0,0));
        event.getWorld().spawnParticle(EnumParticleTypes.FLAME, 0,0,0,0,0,0);
    }
}
