package ruo.asdfrpg.camp;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * campfire - Undefined
 * Created using Tabula 6.0.0
 */
public class ModelCampFire extends ModelBase {
    public ModelRenderer shape1;
    public ModelRenderer shape2;
    public ModelRenderer shape3;
    public ModelRenderer shape4;
    public ModelRenderer shape5;
    public ModelRenderer shape6;
    public ModelRenderer shape7;
    public ModelRenderer shape8;

    public ModelCampFire() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.shape5 = new ModelRenderer(this, 0, 0);
        this.shape5.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.shape5.addBox(0.0F, 0.0F, 0.0F, 3, 12, 3, 0.0F);
        this.setRotateAngle(shape5, 0.9560913642424937F, -2.6862362517444724F, 0.0F);
        this.shape8 = new ModelRenderer(this, 0, 0);
        this.shape8.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.shape8.addBox(0.0F, 0.0F, 0.0F, 3, 12, 3, 0.0F);
        this.setRotateAngle(shape8, 2.276432943376204F, 0.36425021489121656F, 1.5025539530419183F);
        this.shape1 = new ModelRenderer(this, 16, 16);
        this.shape1.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.shape1.addBox(0.0F, 0.0F, 0.0F, 3, 12, 3, 0.0F);
        this.setRotateAngle(shape1, 0.0F, 0.0F, 1.3203415791337103F);
        this.shape2 = new ModelRenderer(this, 0, 0);
        this.shape2.setRotationPoint(0.0F, 17.0F, 0.0F);
        this.shape2.addBox(0.0F, 0.0F, 0.0F, 3, 12, 3, 0.0F);
        this.setRotateAngle(shape2, -2.7317893452215247F, -0.136659280431156F, 2.0032889154390916F);
        this.shape6 = new ModelRenderer(this, 0, 0);
        this.shape6.setRotationPoint(0.0F, 21.8F, 0.0F);
        this.shape6.addBox(0.0F, 0.0F, 0.0F, 3, 12, 3, 0.0F);
        this.setRotateAngle(shape6, 1.5481070465189704F, 1.3203415791337103F, 0.31869712141416456F);
        this.shape4 = new ModelRenderer(this, 0, 0);
        this.shape4.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.shape4.addBox(0.0F, 0.0F, 0.0F, 3, 12, 3, 0.0F);
        this.setRotateAngle(shape4, -1.0471975511965976F, 0.0F, 0.0F);
        this.shape3 = new ModelRenderer(this, 0, 0);
        this.shape3.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.shape3.addBox(0.0F, 0.0F, 0.0F, 3, 12, 3, 0.0F);
        this.setRotateAngle(shape3, 1.0927506446736497F, 0.0F, 0.0F);
        this.shape7 = new ModelRenderer(this, 0, 0);
        this.shape7.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.shape7.addBox(0.0F, 0.0F, 0.0F, 3, 12, 3, 0.0F);
        this.setRotateAngle(shape7, 0.6829473363053812F, 0.0F, 1.1383037381507017F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.shape5.render(f5);
        this.shape8.render(f5);
        this.shape1.render(f5);
        this.shape2.render(f5);
        this.shape6.render(f5);
        this.shape4.render(f5);
        this.shape3.render(f5);
        this.shape7.render(f5);
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
