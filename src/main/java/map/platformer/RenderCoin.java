package map.platformer;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;

public class RenderCoin extends RenderEntityItem {

    public RenderCoin(RenderManager renderManagerIn, RenderItem p_i46167_2_)
    {
        super(renderManagerIn, p_i46167_2_);
    }

    @Override
    public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
