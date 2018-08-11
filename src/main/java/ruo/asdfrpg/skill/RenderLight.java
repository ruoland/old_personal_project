package ruo.asdfrpg.skill;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderLight extends RenderLiving {
    public RenderLight(float shadowsizeIn) {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelLight(), shadowsizeIn);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
