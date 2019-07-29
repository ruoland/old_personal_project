package ruo.creeperworld;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import olib.api.RenderAPI;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
    }

    @Override
    public void init() {
        RenderingRegistry.registerEntityRenderingHandler(EntityMiniCreeper.class, new RenderSpecialCreeper(0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityBigCreeper.class, new RenderSpecialCreeper(0.5F));

    }
}
