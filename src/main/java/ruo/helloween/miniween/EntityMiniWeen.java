package ruo.helloween.miniween;


import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import olib.map.EntityDefaultNPC;
import ruo.helloween.EntityBigWeen;
import ruo.helloween.EntityWeen;

public class EntityMiniWeen extends EntityDefaultNPC {
    private static final DataParameter<Float> PATTEN_LEVEL = EntityDataManager.createKey(EntityMiniWeen.class, DataSerializers.FLOAT);
	public EntityMiniWeen(World worldIn) {
		super(worldIn);
		setBlockMode(Blocks.PUMPKIN);
		setCollision(true);
		this.setDeathTimer(400);
		this.setSize(0.8F,0.8F);
		this.isFly = true;
	}

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(PATTEN_LEVEL, 1F);
    }

    @Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(200000);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if(source.isExplosion() || !isDefenceMiniWeen() && !(this instanceof EntityAttackMiniWeen))
			return false;
		return super.attackEntityFrom(source, amount);
	}
	@Override
	protected void collideWithEntity(Entity entityIn) {
		if (entityIn instanceof EntityWeen ||  entityIn instanceof EntityBigWeen || entityIn instanceof EntityMiniWeen)
			return;
		super.collideWithEntity(entityIn);
	}
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
	}

	public void setPattern(float i){
	    dataManager.set(PATTEN_LEVEL, i);
    }

    public float getPattern(){
	    return dataManager.get(PATTEN_LEVEL);
    }
	public boolean isDefenceMiniWeen() {
		return this instanceof EntityDefenceMiniWeen;
	}
	/**
	 * double maxX = Math.max(posX, player.posX); double maxY = Math.max(posY,
	 * player.posY); double maxZ = Math.max(posZ, player.posZ); double minX =
	 * Math.min(posX, player.posX); double minY = Math.min(posY, player.posY);
	 * double minZ = Math.min(posZ, player.posZ); if(mode == 0)
	 * this.setVelocity((posX- player.posX) / 100, 0, (posZ - player.posZ) / 100);
	 * 
	 * if(mode == 1) this.setVelocity((player.posX- posX) / 10, player.posY - posY,
	 * (player.posZ - posZ) / 10);
	 * 
	 * if(mode == 2) this.setVelocity((maxX - minX) / 100, 0, (maxZ - minZ) / 100);
	 * 
	 * if(mode == 3) this.setVelocity((minX - maxX) / 100, 0, (minZ - maxZ) / 100);
	 */

}
