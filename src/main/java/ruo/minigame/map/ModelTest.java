package ruo.minigame.map;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import ruo.cmplus.deb.DebAPI;
import ruo.minigame.api.RenderAPI;

/**
 * NewProject - Undefined
 * Created using Tabula 6.0.0
 */
public class ModelTest extends ModelBase {
    public ModelRenderer reye;
    public ModelRenderer leye;
    public ModelRenderer rbliend;
    public ModelRenderer lbliend;
    public ModelRenderer mouth;
    public ModelRenderer leyebrow;
    public ModelRenderer reyebrow;
    public ModelTest() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.leyebrow = new ModelRenderer(this, 0, 0);
        this.leyebrow.setRotationPoint(0.0F, -5.0F, -4.3F);
        this.leyebrow.addBox(0.0F, 0.0F, 0.0F, 3, 1, 1, 0.0F);
        this.reyebrow = new ModelRenderer(this, 0, 0);
        this.reyebrow.setRotationPoint(-3.5F, -5.0F, -4.3F);
        this.reyebrow.addBox(0.0F, 0.0F, 0.0F, 3, 1, 1, 0.0F);
        this.mouth = new ModelRenderer(this, 0, 0);
        this.mouth.setRotationPoint(-1.0F, -1.2F, -5.0F);
        this.mouth.addBox(0.0F, 0.0F, 0.0F, 2, 1, 4, 0.0F);
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

    DebAPI bliendl = DebAPI.createDebAPI("bliendl", -0.032, 0, 3.5);//블라인드 XYZ
    DebAPI bliendr = DebAPI.createDebAPI("bliendr", 0, 0, 3.5);//블라인드 XYZ

    DebAPI eyel = DebAPI.createDebAPI("eyel", -0.03, 0.01, 3);//눈 좌표
    DebAPI eyer = DebAPI.createDebAPI("eyer", 0.03, 0.01, 3);//눈 좌표
    DebAPI eyelb = DebAPI.createDebAPI("eyelb", 0, 0, 0);//눈썹좌표
    DebAPI eyerb = DebAPI.createDebAPI("eyerb", 0, 0, 0);//눈썹 좌표
    DebAPI mouths = DebAPI.createDebAPI("mouths", 1, 1, 1);//눈썹 좌표
    DebAPI moutht = DebAPI.createDebAPI("moutht", 0,0,0);//눈썹 좌표

    DebAPI scale = DebAPI.createDebAPI("scale", 0.8,0.8, 0.02);//눈 크기
    DebAPI bscale = DebAPI.createDebAPI("bscale", 2, 1, 0.02);//블라인드 크기
    public DebAPI eyero = DebAPI.createDebAPI("eyero", 0, 0, 3);//눈 좌표

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        EntityDefaultNPC defaultNPC = (EntityDefaultNPC) entity;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1,1,1, 0);
        GlStateManager.scale(mouths.x, mouths.y, mouths.z);
        GlStateManager.translate(moutht.x, moutht.y, moutht.z);
        this.mouth.render(f5);
        GlStateManager.disableBlend();

        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(this.leyebrow.offsetX, this.leyebrow.offsetY, this.leyebrow.offsetZ);
        GlStateManager.translate(this.leyebrow.rotationPointX * f5, this.leyebrow.rotationPointY * f5, this.leyebrow.rotationPointZ * f5);
        GlStateManager.scale(1.0D, 0.4D, 0.3D);
        GlStateManager.translate(-this.leyebrow.offsetX, -this.leyebrow.offsetY, -this.leyebrow.offsetZ);
        GlStateManager.translate(-this.leyebrow.rotationPointX * f5, -this.leyebrow.rotationPointY * f5, -this.leyebrow.rotationPointZ * f5);
        GlStateManager.translate(eyelb.x, eyelb.y, eyelb.z);
        this.leyebrow.render(f5);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(this.reyebrow.offsetX, this.reyebrow.offsetY, this.reyebrow.offsetZ);
        GlStateManager.translate(this.reyebrow.rotationPointX * f5, this.reyebrow.rotationPointY * f5, this.reyebrow.rotationPointZ * f5);
        GlStateManager.scale(1.0D, 0.4D, 0.3D);
        GlStateManager.translate(-this.reyebrow.offsetX, -this.reyebrow.offsetY, -this.reyebrow.offsetZ);
        GlStateManager.translate(-this.reyebrow.rotationPointX * f5, -this.reyebrow.rotationPointY * f5, -this.reyebrow.rotationPointZ * f5);
        GlStateManager.translate(eyerb.x, eyerb.y, eyerb.z);
        this.reyebrow.render(f5);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(this.rbliend.offsetX, this.rbliend.offsetY, this.rbliend.offsetZ);
        GlStateManager.translate(this.rbliend.rotationPointX * f5, this.rbliend.rotationPointY * f5, this.rbliend.rotationPointZ * f5);
        if (defaultNPC.eyeCloseReverse) {
            if (defaultNPC.eyeCloseScaleY <= 0) {
                defaultNPC.random = defaultNPC.worldObj.rand.nextInt(100);
                defaultNPC.eyeCloseReverse = false;
                defaultNPC.eyeCloseScaleY = 0;
            } else
                defaultNPC.eyeCloseScaleY -= 0.1;
        }
        else if (defaultNPC.random == 0) {
            if (defaultNPC.eyeCloseScaleY >= 1) {
                defaultNPC.random = defaultNPC.worldObj.rand.nextInt(100);
                defaultNPC.eyeCloseReverse = true;
                defaultNPC.eyeCloseScaleY = 1;
            } else
                defaultNPC.eyeCloseScaleY += 0.1;

        }
        GlStateManager.scale(bscale.x, defaultNPC.eyeCloseScaleY, bscale.z);
        GlStateManager.translate(-this.rbliend.offsetX, -this.rbliend.offsetY, -this.rbliend.offsetZ);
        GlStateManager.translate(-this.rbliend.rotationPointX * f5, -this.rbliend.rotationPointY * f5, -this.rbliend.rotationPointZ * f5);
        GlStateManager.translate(bliendr.x, bliendr.y, bliendr.z);
        this.rbliend.render(f5);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(this.lbliend.offsetX, this.lbliend.offsetY, this.lbliend.offsetZ);
        GlStateManager.translate(this.lbliend.rotationPointX * f5, this.lbliend.rotationPointY * f5, this.lbliend.rotationPointZ * f5);
        GlStateManager.scale(bscale.x, defaultNPC.eyeCloseScaleY, bscale.z);

        GlStateManager.translate(-this.lbliend.offsetX, -this.lbliend.offsetY, -this.lbliend.offsetZ);
        GlStateManager.translate(-this.lbliend.rotationPointX * f5, -this.lbliend.rotationPointY * f5, -this.lbliend.rotationPointZ * f5);
        GlStateManager.translate(bliendl.x, bliendl.y, bliendl.z);
        this.lbliend.render(f5);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(this.reye.offsetX, this.reye.offsetY, this.reye.offsetZ);
        GlStateManager.translate(this.reye.rotationPointX * f5, this.reye.rotationPointY * f5, this.reye.rotationPointZ * f5);
        GlStateManager.scale( scale.x, scale.y, scale.z);
        GlStateManager.translate(-this.reye.offsetX, -this.reye.offsetY, -this.reye.offsetZ);
        GlStateManager.translate(-this.reye.rotationPointX * f5, -this.reye.rotationPointY * f5, -this.reye.rotationPointZ * f5);
        GlStateManager.translate(eyer.x, eyer.y, eyer.z);
        this.reye.render(f5);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(this.leye.offsetX, this.leye.offsetY, this.leye.offsetZ);
        GlStateManager.translate(this.leye.rotationPointX * f5, this.leye.rotationPointY * f5, this.leye.rotationPointZ * f5);
        GlStateManager.scale( scale.x, scale.y, scale.z);
        GlStateManager.translate(-this.leye.offsetX, -this.leye.offsetY, -this.leye.offsetZ);
        GlStateManager.translate(-this.leye.rotationPointX * f5, -this.leye.rotationPointY * f5, -this.leye.rotationPointZ * f5);
        GlStateManager.translate(eyel.x, eyel.y, eyel.z);

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
