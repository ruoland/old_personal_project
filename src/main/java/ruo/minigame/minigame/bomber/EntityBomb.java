package ruo.minigame.minigame.bomber;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityBomb extends Entity
{
    private static final DataParameter<Integer> FUSE = EntityDataManager.<Integer>createKey(EntityBomb.class, DataSerializers.VARINT);
    private EntityLivingBase tntPlacedBy;
    /** How long the fuse is */
    private int fuse;

    public EntityBomb(World worldIn)
    {
        super(worldIn);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.setSize(1F, 1F);
    }

    public EntityBomb(World worldIn, double x, double y, double z, EntityLivingBase igniter)
    {
        this(worldIn);
        this.setPosition(x, y, z);
        float f = (float)(Math.random() * (Math.PI * 2D));
        this.setFuse(80);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.tntPlacedBy = igniter;
    }

    protected void entityInit()
    {
        this.dataManager.register(FUSE, Integer.valueOf(80));
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }


    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (!this.hasNoGravity())
        {
            this.motionY -= 0.03999999910593033D;
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        --this.fuse;

        if (this.fuse <= 0)
        {
            this.setDead();

            if (!this.worldObj.isRemote)
            {
                this.explode();
            }
        }
        else
        {
            this.handleWaterMovement();
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    int exp = 0;
    private void explode()
    {
    	float strength = 1.0F;
    	for(int z = 0; z < 3;z++){ 
    		this.worldObj.createExplosion(tntPlacedBy, this.posX, this.posY + (double)(this.height / 16.0F), this.posZ+z, strength, true);
    		this.worldObj.createExplosion(tntPlacedBy, this.posX, this.posY + (double)(this.height / 16.0F), this.posZ-z, strength, true);
    		this.worldObj.createExplosion(tntPlacedBy, this.posX, this.posY + 1, this.posZ+z, strength, true);
    		this.worldObj.createExplosion(tntPlacedBy, this.posX, this.posY + 1, this.posZ-z, strength, true);
    		this.worldObj.createExplosion(tntPlacedBy, this.posX, this.posY + 2, this.posZ-z, strength, true);
    		this.worldObj.createExplosion(tntPlacedBy, this.posX, this.posY + 2, this.posZ+z, strength, true);
    	}
    	for(int x = 0; x < 3;x++){ 
    		this.worldObj.createExplosion(tntPlacedBy, this.posX+x, this.posY + (double)(this.height / 16.0F), this.posZ, strength, true);
    		this.worldObj.createExplosion(tntPlacedBy, this.posX-x, this.posY + (double)(this.height / 16.0F), this.posZ, strength, true);
    		this.worldObj.createExplosion(tntPlacedBy, this.posX+x, this.posY + 1, this.posZ, strength, true);
    		this.worldObj.createExplosion(tntPlacedBy, this.posX-x, this.posY + 1, this.posZ, strength, true);
    		this.worldObj.createExplosion(tntPlacedBy, this.posX+x, this.posY + 2, this.posZ, strength, true);
    		this.worldObj.createExplosion(tntPlacedBy, this.posX-x, this.posY + 2, this.posZ, strength, true);
    	}
        this.worldObj.createExplosion(tntPlacedBy, this.posX, this.posY, this.posZ, strength, true);
        this.worldObj.createExplosion(tntPlacedBy, this.posX, this.posY + 1, this.posZ, strength, true);
        this.worldObj.createExplosion(tntPlacedBy, this.posX, this.posY + 2, this.posZ, strength, true);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setShort("Fuse", (short)this.getFuse());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        this.setFuse(compound.getShort("Fuse"));
    }

    /**
     * returns null or the entityliving it was placed or ignited by
     */
    public EntityLivingBase getTntPlacedBy()
    {
        return this.tntPlacedBy;
    }

    public float getEyeHeight()
    {
        return 0.0F;
    }

    public void setFuse(int fuseIn)
    {
        this.dataManager.set(FUSE, Integer.valueOf(fuseIn));
        this.fuse = fuseIn;
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (FUSE.equals(key))
        {
            this.fuse = this.getFuseDataManager();
        }
    }

    /**
     * Gets the fuse from the data manager
     */
    public int getFuseDataManager()
    {
        return ((Integer)this.dataManager.get(FUSE)).intValue();
    }

    public int getFuse()
    {
        return this.fuse;
    }
}