package ruo.map.lopre2;

import net.minecraft.block.Block;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

//반복해서 떨어지는 블럭 - 6월 21일
//1탄에서 용암에 떨어지는 블럭으로 쓰임
//용암에 들어서는 경우 천천히 용암속으로 사라짐

public class EntityFallingBlock extends EntityPreBlock {
	public int lavaY;
	private boolean debug;
	private static int debugCount = 0;

	public EntityFallingBlock(World worldIn) {
		super(worldIn);
		this.setBlockMode(Blocks.STONE);
		this.setCollision(true);
	}

	@Override
	public EntityFallingBlock spawn(double x, double y, double z) {
		EntityFallingBlock lavaBlock = new EntityFallingBlock(worldObj);
		lavaBlock.setCanFalling(canFalling());
		lavaBlock.setSpawnXYZ(x, y, z);
		lavaBlock.setTeleport(false);
		lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY() + 0.3, lavaBlock.getSpawnZ());
		lavaBlock.setPositionAndRotationDirect(lavaBlock.getSpawnX(), lavaBlock.getSpawnY() + 0.3,
				lavaBlock.getSpawnZ(), 90, 90, 1, false);
		this.copyModel(lavaBlock);
		lavaBlock.prevBlock = prevBlock;
		lavaBlock.onInitialSpawn(null, null);
		lavaBlock.setBlockMode(getCurrentBlock());
		worldObj.spawnEntityInWorld(lavaBlock);
		return lavaBlock;
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		if (lavaY == 0)
			for (int i = 0; i < 70; i++) {
				Block block = worldObj.getBlockState(new BlockPos(getSpawnX(), getSpawnY() - i, getSpawnZ()))
						.getBlock();
				if (block == Blocks.LAVA) {
					lavaY = (int) getSpawnY() - i;
					break;
				}
			}
		return super.onInitialSpawn(difficulty, livingdata);
	}

	boolean firstReset = false;// 이건 너무 높은 곳에서 떨어지는 경우 용암 깊은 곳에 들어가서 원래장소로 되돌아가는 버그를 막기 위해 있음
	int delay = 0, delayCount = 0;// 용암에 떨어졌을 때 너무 짧은 시간에 원래 장소로 돌아가는 경우를 대비해서

	@Override
	public String getCustomNameTag() {
		return super.getCustomNameTag()+" - "+lavaY;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
			delay++;
			if (isInLava()) {
				if (!firstReset) {
					firstReset = true;
					this.setPosition(posX, posY + 0.7, posZ);
				}
				if (lavaY == 0)
					lavaY = MathHelper.floor_double(posY);
				this.setVelocity(0, -0.004, 0);
			}
			if (lavaY - 0.2 > posY && firstReset) {
				this.setPositionAndUpdate(posX, getSpawnY(), posZ);

				if (delay < 15) {
					delayCount++;
					if(delayCount == 3) {
						lavaY -= 0.5;
						delayCount = 0;
						delay = 0;
					}
				}
				firstReset = false;
				delay = 0;

			}
	}

	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, ItemStack stack, EnumHand hand) {
		if(CommandJB.isDebMode) {

			if (hand == EnumHand.MAIN_HAND && stack != null && stack.getItem() == Items.APPLE) {
				debug = !debug;
				if (debug) {
					debugCount++;
					this.setCustomNameTag(lavaY + " - DEBUG" + debugCount);
					this.setAlwaysRenderNameTag(true);
				} else {
					debugCount--;
					this.setCustomNameTag("");
					this.setAlwaysRenderNameTag(false);
				}
				System.out.println("디버그 " + debug);
			}
		}
		return super.applyPlayerInteraction(player, vec, stack, hand);
	}

	@Override
	public void teleport() {
		super.teleport();
	}

	@Override
	public void teleportEnd() {
		super.teleportEnd();
		if (dataManager != null && !dataManager.isEmpty()) {
			for (int i = 0; i < 70; i++) {
				Block block = worldObj.getBlockState(new BlockPos(getSpawnX(), getSpawnY() - i, getSpawnZ()))
						.getBlock();
				if (block == Blocks.LAVA) {
					lavaY = (int) getSpawnY() - i;
					break;
				}
			}
		}
	}
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("lavaY", lavaY);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		lavaY = compound.getInteger("lavaY");
	}

}
