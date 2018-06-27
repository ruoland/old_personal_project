package ruo.map.lot;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;

public class EntityWallBlock extends EntityDefaultNPC{

	public EntityWallBlock(World worldIn) {
		super(worldIn);
		this.setSize(5, 5);
		this.setBlockMode(Blocks.STONE);
		this.setScale(5,5,5);
		this.setTra(0,-1,0);
		this.isFly = true;
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn) {
		super.onCollideWithPlayer(entityIn);
		((EntityLivingBase) this).knockBack(entityIn, 1F, entityIn.posX - this.posX, entityIn.posZ - this.posZ);
	}
	
}
