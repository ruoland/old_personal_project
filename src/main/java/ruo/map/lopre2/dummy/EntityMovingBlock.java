package ruo.map.lopre2.dummy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ruo.map.lopre2.EntityPreBlock;
import ruo.minigame.api.WorldAPI;

import javax.annotation.Nullable;

public class EntityMovingBlock extends EntityPreBlock {
	public double aX, aY, aZ;
	public boolean isRiding = true;

	public EntityMovingBlock(World worldIn) {
		super(worldIn);
		this.setBlockMode(Blocks.DIRT);
		this.setCollision(false);
	}

	public EntityMovingBlock(World worldIn, double spawnY, double x, double y, double z) {
		super(worldIn);
		this.setBlockMode(Blocks.DIRT);
		this.setCollision(true);
		this.setPosition(x, spawnY, z);
		aX = x;
		aY = y;
		aZ = z;
		noClip = false;
	}

	public EntityMovingBlock(World worldIn, double x, double y, double z) {
		super(worldIn);
		this.setBlockMode(Blocks.DIRT);
		this.setCollision(true);
		aX = x;
		aY = y;
		aZ = z;
		noClip = false;
	}

	public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
		if (!super.processInteract(player, hand, stack)) {
			if (!this.worldObj.isRemote && !this.isBeingRidden() && isRiding) {
				player.startRiding(this);
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
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
		if(WorldAPI.getPlayer().getDistance(posX, posY, posZ) < 20) {

			if (isRiding) {
				if (isBeingRidden()) {
					if (posY < aY)
						this.setVelocity(0, 0.1, 0);
				} else {

				}
			} else {
				if (posY < aY)
					this.setVelocity(0, 0.1, 0);
				else {
					isFly = true;
					noClip = true;
				}
			}
		}
	}
}
