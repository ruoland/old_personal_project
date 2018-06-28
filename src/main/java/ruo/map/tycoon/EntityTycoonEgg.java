package ruo.map.tycoon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import ruo.map.tycoon.block.bread.TileBreadWorkbench;

public class EntityTycoonEgg extends EntityEgg {
    public EntityTycoonEgg(World worldIn) {
        super(worldIn);
    }

    public EntityTycoonEgg(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    public EntityTycoonEgg(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        TileBreadWorkbench workbench = (TileBreadWorkbench) worldObj.getTileEntity(result.getBlockPos());
        workbench.addEggCount(1);

    }
}
