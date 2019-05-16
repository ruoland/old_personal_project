package ruo.awild;


import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderZombie;
import olib.api.RenderAPI;

public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        RenderAPI.registerRender(EntityWildZombie.class, new RenderZombie(Minecraft.getMinecraft().getRenderManager()));
        RenderAPI.registerRender(EntityWildHorse.class, new RenderHorse(Minecraft.getMinecraft().getRenderManager(), new ModelHorse(), 0.5F));
        RenderAPI.registerRender(EntityWildEnderman.class, new RenderEnderman(Minecraft.getMinecraft().getRenderManager()));
    }
}
