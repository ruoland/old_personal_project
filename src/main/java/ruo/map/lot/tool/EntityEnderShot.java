package ruo.map.lot.tool;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruo.map.lot.LOT;

public class EntityEnderShot extends EntityThrowable {
    private EntityLivingBase thrower;

    public EntityEnderShot(World worldIn) {
        super(worldIn);
    }

    public EntityEnderShot(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
        this.thrower = throwerIn;
    }

    @SideOnly(Side.CLIENT)
    public EntityEnderShot(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(RayTraceResult result) {
        EntityPlayer player = (EntityPlayer) this.getThrower();

        for (int i = 0; i < 32; ++i) {
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian(), new int[0]);
        }
        System.out.println("onImpact");
        if (!this.worldObj.isRemote) {
            if(getEntityWorld().getBlockState(result.getBlockPos()).getBlock() == LOT.enderShotBlock) {
                if (player instanceof EntityPlayerMP) {
                    EntityPlayerMP entityplayermp = (EntityPlayerMP) player;
                    if (entityplayermp.connection.getNetworkManager().isChannelOpen() && entityplayermp.worldObj == this.worldObj && !entityplayermp.isPlayerSleeping()) {
                        player.setPositionAndUpdate(posX, posY, posZ);
                        player.fallDistance = 0.0F;
                    }
                } else if (player != null) {
                    player.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                    player.fallDistance = 0.0F;
                }
            }
            this.setDead();

        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        EntityLivingBase player = this.getThrower();

        if (player != null && player instanceof EntityPlayer && !player.isEntityAlive()) {
            this.setDead();
        } else {
            super.onUpdate();
        }
    }
}