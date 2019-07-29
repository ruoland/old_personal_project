package ruo.creeperworld;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelSpecialCreeper extends ModelCreeper {

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.pushMatrix();
        if(entityIn instanceof EntityMiniCreeper) {
            GlStateManager.translate(0,0.7,0);
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
        }
        else if(entityIn instanceof EntityBigCreeper) {
            GlStateManager.translate(0,-3,0);
            GlStateManager.scale(3, 3, 3);
        }
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.popMatrix();
    }
}
