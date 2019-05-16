package map.lopre2.jump2;

import olib.api.WorldAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.init.Items;

public class RenderJumpSpider extends RenderSpider<EntityJumpSpider> {
    public RenderJumpSpider(){
        super(Minecraft.getMinecraft().getRenderManager());
        this.removeLayer(this.layerRenderers.get(0));
    }
    @Override
    protected void renderModel(EntityJumpSpider entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        GlStateManager.pushMatrix();
        if(entitylivingbaseIn.isJump()) {
            if(WorldAPI.equalsHeldItem(Items.APPLE)){
                GlStateManager.scale(1.2, 0.7, 1.2);
                GlStateManager.translate(0,0.8,0);
            }
            if(WorldAPI.equalsHeldItem(Items.BREAD)){
                GlStateManager.scale(1.3, 0.7, 1.3);
                GlStateManager.translate(0,0.7,0);

            }
            if(WorldAPI.equalsHeldItem(Items.BEEF)){
                GlStateManager.scale(1.5, 0.7, 1.4);
                GlStateManager.translate(0,0.6,0);
            }
            else {
                GlStateManager.scale(1.5, 0.7, 1.5);
                GlStateManager.translate(0,0.8,0);

            }
        }
        super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        GlStateManager.popMatrix();
    }
}
