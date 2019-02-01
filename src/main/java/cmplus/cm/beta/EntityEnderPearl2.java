package cmplus.cm.beta;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityEnderPearl2 extends EntityEnderPearl
{
    private EntityLivingBase thrower;
    private boolean isXYZ;
    private double x,y,z;
    private final EntityEnderPearl2 pearlInstance;
    public EntityEnderPearl2(World worldIn)
    {
        super(worldIn);
        pearlInstance = this;
    }
    public EntityEnderPearl2(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
        this.thrower = throwerIn;
        pearlInstance = this;
    }

    public EntityEnderPearl2(World worldIn, EntityLivingBase throwerIn, double x, double y, double z, float pitch, float yaw)
    {
        super(worldIn, throwerIn);
        pearlInstance = this;
        this.thrower = throwerIn;
        this.setPosition(thrower.posX, thrower.posY, thrower.posZ);
        this.x = x;
        this.y = y;
        this.z = z;
        isXYZ = true;
    }

    @SideOnly(Side.CLIENT)
    public EntityEnderPearl2(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
        pearlInstance = this;
    }

    
    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(RayTraceResult result)
    {
        final EntityLivingBase entitylivingbase = this.getThrower();

        if (result.entityHit != null)
        {
            if (result.entityHit == this.thrower)
            {
                return;
            }

            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, entitylivingbase), 0.0F);
        }

        for (int i = 0; i < 32; ++i)
        {
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian(), new int[0]);
        }

        if (!this.worldObj.isRemote)
        {
            if (entitylivingbase instanceof EntityPlayerMP)
            {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)entitylivingbase;

                if (entityplayermp.connection.getNetworkManager().isChannelOpen() && entityplayermp.worldObj == this.worldObj && !entityplayermp.isPlayerSleeping())
                {
                    entitylivingbase.setPositionAndUpdate(x,y,z);
                    entitylivingbase.fallDistance = 0.0F;
                }
            }
            else if (entitylivingbase != null)
            {
            	if(isXYZ)
            		entitylivingbase.setPositionAndUpdate(x,y,z);
            	else
            		entitylivingbase.setPosition(posX, posY, posZ);
            	
            	entitylivingbase.fallDistance = 0.0F;
            }

            this.setDead();
        }
    }
    
    @Override
    public void onEntityUpdate() {
    	
    	super.onEntityUpdate();
    }
    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        EntityLivingBase entitylivingbase = this.getThrower();

        if (entitylivingbase != null && entitylivingbase instanceof EntityPlayer && !entitylivingbase.isEntityAlive())
        {
            this.setDead();
        }
        else
        {
            super.onUpdate();
        }
    }
}