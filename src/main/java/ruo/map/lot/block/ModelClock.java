package ruo.map.lot.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * clock - Undefined
 * Created using Tabula 6.0.0
 */
public class ModelClock extends ModelBase {
    public ModelRenderer shape1;
    public ModelRenderer shape2;

    public ModelClock() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.shape1 = new ModelRenderer(this, 0, 0);
        this.shape1.setRotationPoint(0.0F, -6.1F, 0.0F);
        this.shape1.addBox(0.0F, 0.0F, 0.0F, 31, 30, 1, 0.0F);
        this.shape2 = new ModelRenderer(this, 0, 0);
        this.shape2.setRotationPoint(15.0F, 9.2F, -1.0F);
        this.shape2.addBox(0.0F, 0.0F, 0.0F, 2, 14, 1, 0.0F);
        this.setRotateAngle(shape2, 3.141592653589793F, 0.0F, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.shape1.render(f5);
        this.shape2.render(f5);
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
