package ruo.minigame.map;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import ruo.cmplus.deb.DebAPI;

/**
 * NewProject - Undefined
 * Created using Tabula 6.0.0
 */
public class ModelTest extends ModelBase {
    public ModelRenderer reye;
    public ModelRenderer leye;
    public ModelRenderer rbliend;
    public ModelRenderer lbliend;

    public ModelTest() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.rbliend = new ModelRenderer(this, 0, 11);
        this.rbliend.setRotationPoint(1.0F, -4.0F, -5.2F);
        this.rbliend.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.lbliend = new ModelRenderer(this, 0, 11);
        this.lbliend.setRotationPoint(-2.0F, -4.0F, -5.2F);
        this.lbliend.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.reye = new ModelRenderer(this, 0, 0);
        this.reye.setRotationPoint(1.0F, -4.0F, -5.0F);
        this.reye.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.setRotateAngle(reye, 0.0F, -3.875409442231813E-18F, 0.0F);
        this.leye = new ModelRenderer(this, 0, 0);
        this.leye.setRotationPoint(-2.0F, -4.0F, -5.0F);
        this.leye.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
    }

    DebAPI bliend = DebAPI.createDebAPI("bliend", 0, 0, 3.5);//블라인드 XYZ
    DebAPI eye = DebAPI.createDebAPI("eye", 0, 0, 3);//눈 좌표
    DebAPI scale = DebAPI.createDebAPI("scale", 1,1,0.02);//눈 크기
    DebAPI bscale = DebAPI.createDebAPI("bscale", 1,1,0.02);//블라인드 크기

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        EntityDefaultNPC defaultNPC = (EntityDefaultNPC) entity;
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.rbliend.offsetX, this.rbliend.offsetY, this.rbliend.offsetZ);
        GlStateManager.translate(this.rbliend.rotationPointX * f5, this.rbliend.rotationPointY * f5, this.rbliend.rotationPointZ * f5);
        if (defaultNPC.random == 0)
            GlStateManager.scale(1.0D, 1D, bscale.z);
        else
            GlStateManager.scale(1.0D, 0.0D, bscale.z);
        GlStateManager.translate(-this.rbliend.offsetX, -this.rbliend.offsetY, -this.rbliend.offsetZ);
        GlStateManager.translate(-this.rbliend.rotationPointX * f5, -this.rbliend.rotationPointY * f5, -this.rbliend.rotationPointZ * f5);
        GlStateManager.translate(bliend.x, bliend.y, bliend.z);
        this.rbliend.render(f5);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(this.lbliend.offsetX, this.lbliend.offsetY, this.lbliend.offsetZ);
        GlStateManager.translate(this.lbliend.rotationPointX * f5, this.lbliend.rotationPointY * f5, this.lbliend.rotationPointZ * f5);
        if (defaultNPC.random == 0)
            GlStateManager.scale(1.0D, 1D, bscale.z);
        else
            GlStateManager.scale(1.0D, 0.1D, bscale.z);
        GlStateManager.translate(-this.lbliend.offsetX, -this.lbliend.offsetY, -this.lbliend.offsetZ);
        GlStateManager.translate(-this.lbliend.rotationPointX * f5, -this.lbliend.rotationPointY * f5, -this.lbliend.rotationPointZ * f5);
        GlStateManager.translate(bliend.x, bliend.y, bliend.z);
        this.lbliend.render(f5);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(this.reye.offsetX, this.reye.offsetY, this.reye.offsetZ);
        GlStateManager.translate(this.reye.rotationPointX * f5, this.reye.rotationPointY * f5, this.reye.rotationPointZ * f5);
        GlStateManager.scale(1.0D, 1.0D, scale.z);
        GlStateManager.translate(-this.reye.offsetX, -this.reye.offsetY, -this.reye.offsetZ);
        GlStateManager.translate(-this.reye.rotationPointX * f5, -this.reye.rotationPointY * f5, -this.reye.rotationPointZ * f5);
        GlStateManager.translate(eye.x, eye.y, eye.z);
        this.reye.render(f5);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(this.leye.offsetX, this.leye.offsetY, this.leye.offsetZ);
        GlStateManager.translate(this.leye.rotationPointX * f5, this.leye.rotationPointY * f5, this.leye.rotationPointZ * f5);
        GlStateManager.scale(1.0D, 1.0D, scale.z);
        GlStateManager.translate(-this.leye.offsetX, -this.leye.offsetY, -this.leye.offsetZ);
        GlStateManager.translate(-this.leye.rotationPointX * f5, -this.leye.rotationPointY * f5, -this.leye.rotationPointZ * f5);
        GlStateManager.translate(eye.x, eye.y, eye.z);

        this.leye.render(f5);
        GlStateManager.popMatrix();
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
