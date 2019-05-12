package gooditem;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class GIClientProxy extends GIServerProxy{

    @Override
    public void preInit() {
        ModelLoader.setCustomModelResourceLocation(GoodItem.itemForbidden, 0, new ModelResourceLocation(GoodItem.itemForbidden.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(GoodItem.itemKill, 0, new ModelResourceLocation(GoodItem.itemKill.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(GoodItem.itemMagnet, 0, new ModelResourceLocation(GoodItem.itemMagnet.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(GoodItem.itemWarning, 0, new ModelResourceLocation(GoodItem.itemWarning.getRegistryName(), "inventory"));

    }

    @Override
    public void init() {
    }
}
