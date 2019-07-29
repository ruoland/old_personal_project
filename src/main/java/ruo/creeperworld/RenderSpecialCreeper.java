package ruo.creeperworld;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.util.ResourceLocation;

public class RenderSpecialCreeper extends RenderCreeper{
    private static final ResourceLocation CREEPER_TEXTURES = new ResourceLocation("textures/entity/creeper/creeper.png");

    public RenderSpecialCreeper(float shadowsizeIn) {
        super(Minecraft.getMinecraft().getRenderManager());
        this.mainModel = new ModelSpecialCreeper();
    }

}
