package ruo.map.lopre2.dummy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;

import java.util.List;

public class EntityRidingBlock extends EntityDefaultNPC {
	public double aX, aY, aZ;

	public EntityRidingBlock(World worldIn) {
		super(worldIn);
		this.setBlockMode(Blocks.DIRT);
		this.setCollision(false);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source == source.inWall || source == DamageSource.fall)
			return false;
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		 List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(3, 3, 3));
         if (!list.isEmpty())
         {
             for (Entity entity : list)
             {
                 if ((entity instanceof EntityPlayer) && !entity.noClip)
                 {
                	 if(!entity.onGround)
                		 entity.moveEntity(0.1, entity.motionY, entity.motionZ);
                	 this.moveEntity(0.1, this.motionY, this.motionZ);
                 }
             }
         }
	}
}
