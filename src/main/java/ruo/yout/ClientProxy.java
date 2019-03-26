package ruo.yout;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {

    @Override
    public void preinit() {
        ModelLoader.setCustomModelResourceLocation(Mojae.itemLock, 0, new ModelResourceLocation(Mojae.itemLock.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Mojae.itemRiding, 0, new ModelResourceLocation(Mojae.itemRiding.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Mojae.itemGod, 0, new ModelResourceLocation(Mojae.itemGod.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Mojae.itemOneShot, 0, new ModelResourceLocation(Mojae.itemOneShot.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Mojae.itemExplosion, 0, new ModelResourceLocation(Mojae.itemOneShot.getRegistryName(), "inventory"));

    }

    @Override
    public void init() {
    }
}
