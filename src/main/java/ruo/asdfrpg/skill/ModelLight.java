package ruo.asdfrpg.skill;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * light - Undefined
 * Created using Tabula 6.0.0
 */
public class ModelLight extends ModelBase {
    public ModelRenderer shape1;
    public ModelRenderer shape2;
    public ModelRenderer shape3;
    public ModelRenderer shape4;

    public ModelLight() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.shape2 = new ModelRenderer(this, 0, 0);
        this.shape2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape2.addBox(-2.0F, -2.0F, -5.0F, 4, 4, 10, 0.0F);
        this.shape3 = new ModelRenderer(this, 0, 0);
        this.shape3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape3.addBox(-5.0F, -2.0F, -2.0F, 10, 4, 4, 0.0F);
        this.shape1 = new ModelRenderer(this, 0, 0);
        this.shape1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape1.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
        this.shape4 = new ModelRenderer(this, 0, 0);
        this.shape4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape4.addBox(-2.0F, -5.0F, -2.0F, 4, 10, 4, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.shape2.render(f5);
        this.shape3.render(f5);
        this.shape1.render(f5);
        this.shape4.render(f5);
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
