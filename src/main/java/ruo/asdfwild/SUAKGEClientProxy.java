package ruo.asdfwild;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import olib.api.RenderAPI;
import ruo.awild.EntityWildZombie;
import ruo.creeperworld.EntityBigCreeper;
import ruo.creeperworld.EntityMiniCreeper;
import ruo.creeperworld.RenderSpecialCreeper;

public class SUAKGEClientProxy extends SUAKGECommonProxy {

    public void init(){
        RenderAPI.registerRender(EntityTeleportCreeper.class, new RenderCreeper(Minecraft.getMinecraft().getRenderManager()));
        RenderAPI.registerRender(EntitySpiderJockey.class, new RenderSpider(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityMiniCreeper.class, new RenderSpecialCreeper(0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityBigCreeper.class, new RenderSpecialCreeper(0.5F));
        RenderAPI.registerRender(EntityWildZombie.class, new RenderZombie(Minecraft.getMinecraft().getRenderManager()));

    }
}
