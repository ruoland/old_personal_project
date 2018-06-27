package ruo.map.lopre2.dummy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;

public class EntityShipBlock extends EntityDefaultNPC {
	public EntityShipBlockPart[] partArray = new EntityShipBlockPart[11];
	private double spawnY;
	public EntityShipBlock(World worldIn) {
		super(worldIn);
		this.setBlockMode(Blocks.STONE);
		this.setCollision(true);
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		
		setPosition(posX, posY+0.2, posZ);
		this.spawnY = posY+0.2;
		return super.onInitialSpawn(difficulty, livingdata);
	}
	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
		
		return super.processInteract(player, hand, stack);
	}
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if(!source.isCreativePlayer() || source == DamageSource.lava) {
			return false;
		}
		if (isServerWorld() && partArray[0] != null) {
			for (int i = 0; i < 8; i++) {
				partArray[i].setDead();
			}
			this.setDead();
		}
		return super.attackEntityFrom(source, amount);
	}
	@Override
	public boolean canRenderOnFire() {
		
		return false;
	}
	@Override
	public void setFire(int seconds) {
		//super.setFire(seconds);
	}
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		setBlock();
		if(spawnY == 0)
			this.spawnY = posY;
	}

	public void setBlock() {
		if (partArray[0] == null) {
			for (int i = 0; i < 8; i++) {
				partArray[i] = new EntityShipBlockPart(worldObj, this, i);// OOO
			}
			for (int i = 0; i < 8; i++) {
				if (isServerWorld())
					worldObj.spawnEntityInWorld(partArray[i]);
			}
		}
	}

	@Override
	public void applyEntityCollision(Entity entityIn) {
		if (entityIn instanceof EntityShipBlockPart || entityIn instanceof EntityShipBlock) {
			return;
		}
		super.applyEntityCollision(entityIn);
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		if (entityIn instanceof EntityShipBlockPart || entityIn instanceof EntityShipBlock) {
			return;
		}
		super.collideWithEntity(entityIn);
	}
}
