package ruo.map.platformer;


import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.entity.RenderHorse;
import ruo.cmplus.deb.DebAPI;
import ruo.map.platformer.chapter1.EntityBossHorse;
import ruo.minigame.api.RenderAPI;

public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        RenderAPI.registerRender(EntityBossHorse.class, new RenderHorse(Minecraft.getMinecraft().getRenderManager(), new ModelHorse(), 0.5F));
        RenderAPI.registerRender(EntityCoin.class, new RenderCoin(DebAPI.getRenderManager(), Minecraft.getMinecraft().getRenderItem()));

    }
}
