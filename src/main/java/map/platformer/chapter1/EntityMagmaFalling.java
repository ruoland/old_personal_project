package map.platformer.chapter1;

import oneline.api.WorldAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityMagmaFalling extends EntityMob{
	public float randomY, randomX, roX, roY;
	public int random;
	private int count = 0;
	public EntityMagmaFalling(World worldIn) {
		super(worldIn);
		this.setSize(10, 1);
		this.noClip = !this.noClip;
		this.rotationYaw = 0;
		this.rotationPitch = 0;
		this.rotationYawHead = 0;
		random = rand.nextInt(5);
		float m = (float) (rand.nextBoolean() ? Math.random() : -Math.random());
		randomY = rand.nextBoolean() ? -rand.nextInt(3)+m : rand.nextInt(3)+m;
		randomX = rand.nextBoolean() ? -rand.nextInt(3)+m : rand.nextInt(3)+m;
		roX = rand.nextInt(180);
		roY = rand.nextInt(180);
		setPosition(posX+randomX, posY+randomY, posZ);
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
		super.fall(distance, damageMultiplier);
		WorldAPI.setBlock(posX, posY, posZ, Blocks.STONE);

	}
	
	int lifeTime = 200;
	@Override
	public void onLivingUpdate() {
        if (WorldAPI.getPlayer().isDead || (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL))
        {
            this.setDead();
        }
		if(getCustomNameTag().equals("-2")) {
	        for(Entity player : worldObj.getEntitiesWithinAABB(EntityPlayer.class, getEntityBoundingBox())) {
	        	player.attackEntityFrom(DamageSource.fallingBlock, 0.2F);
	        }
		}
		if(random == -1) {
	        for(Entity player : worldObj.getEntitiesWithinAABB(EntityPlayer.class, getEntityBoundingBox())) {
	        	player.attackEntityFrom(DamageSource.fallingBlock, 0.2F);
	        }
		}
	    this.rotationYaw = 0;
		this.rotationPitch = 0;
		this.rotationYawHead = 0;
		super.onLivingUpdate();
		if(getCustomNameTag().equals("-2") && posY < 194 && !worldObj.isAirBlock(getPosition())){
			for(Entity player : worldObj.getEntitiesWithinAABB(EntityPlayer.class, getEntityBoundingBox().addCoord(8, 0, 0))) {
				player.attackEntityFrom(DamageSource.fallingBlock, 3);
			}
			if(random != -1)
				random = rand.nextInt(5);
			float m = (float) (rand.nextBoolean() ? Math.random() : -Math.random());
			randomY = rand.nextBoolean() ? -rand.nextInt(5)+m : rand.nextInt(5)+m;
			randomX = rand.nextBoolean() ? -rand.nextInt(5)+m : rand.nextInt(5)+m;
			this.setDead();
		}
		if(!getCustomNameTag().equals("-2") && posY < 175){
			//getBlock(posX, posX, posY, posY, posZ+5, posZ-5);
	        for(Entity player : worldObj.getEntitiesWithinAABB(EntityPlayer.class, getEntityBoundingBox().addCoord(8, 0, 0))) {
	        	player.attackEntityFrom(DamageSource.fallingBlock, 3);
	        }
			if(random != -1)
				random = rand.nextInt(5);
			float m = (float) (rand.nextBoolean() ? Math.random() : -Math.random());
			randomY = rand.nextBoolean() ? -rand.nextInt(5)+m : rand.nextInt(5)+m;
			randomX = rand.nextBoolean() ? -rand.nextInt(5)+m : rand.nextInt(5)+m;
			this.motionY = 0;
			this.setPositionAndUpdate(posX, 205, posZ);
			count++;
	
		}
		if(count == 4 || lifeTime <= 0)
			this.setDead();
		
		if(getCustomNameTag().equals("-2")) {
			this.setVelocity(0, -0.7,0);
		}
		else
			this.setVelocity(0, -1.0, 0);
		lifeTime --;
	}
	@Override
	public void applyEntityCollision(Entity entityIn) {
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn) {
		// super.onCollideWithPlayer(entityIn);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}

	@Override
	protected void collideWithNearbyEntities() {
		// super.collideWithNearbyEntities();
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		// super.collideWithEntity(entityIn);
	}
}
