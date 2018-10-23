package ruo.asdf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.client.renderer.entity.RenderZombie;
import ruo.awild.EntityWildZombie;
import ruo.minigame.api.RenderAPI;

public class SUAKGEClientProxy extends SUAKGECommonProxy {

    public void init(){
        RenderAPI.registerRender(EntityTeleportCreeper.class, new RenderCreeper(Minecraft.getMinecraft().getRenderManager()));
        RenderAPI.registerRender(EntitySpiderJockey.class, new RenderSpider(Minecraft.getMinecraft().getRenderManager()));
        RenderAPI.registerRender(EntityMissileCreeper.class, new RenderSkeleton(Minecraft.getMinecraft().getRenderManager()));
        RenderAPI.registerRender(EntityWildZombie.class, new RenderZombie(Minecraft.getMinecraft().getRenderManager()));

    }
}
