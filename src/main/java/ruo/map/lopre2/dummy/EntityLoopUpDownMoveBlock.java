package ruo.map.lopre2.dummy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.map.lopre2.CommandJB;
import ruo.map.lopre2.EntityPreBlock;
import ruo.minigame.api.WorldAPI;

import java.util.List;

/*
엔티티 충돌 바운딩 박스를 높게 만들기
-이방법은 블럭 위에서 잘 움직일 수도 있고 점프도 되는데 접근할 수 없음

엔티티 충돌 바운딩 박스에 조건 만들기
1번 플레이어 Y가 높을 때만 충돌 바운딩 박스 활성화 - 안 됨(왜 안되는지는 확인 안해봄)
2번 WorldAPI checkPos를 쓴다 - 잘 올라가지고 점프도 잘 되는데 좌우 이동이  끼인 것처럼 움직임ㄴ(이게 체크포스 문제인지 뭐 때문인지 모르겠음)
3번
 */
public class EntityLoopUpDownMoveBlock extends EntityPreBlock {
	public static double ax = 0.161;

	public EntityLoopUpDownMoveBlock(World worldIn) {
		super(worldIn);
		this.setCollision(true);
		setBlockMode(Blocks.STONE);
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		setSpawnXYZ(posX, posY, posZ);
		this.setTeleport(true);
		return super.onInitialSpawn(difficulty, livingdata);
	}
	@Override
	public EntityLoopUpDownMoveBlock spawn(double x, double y, double z) {
		EntityLoopUpDownMoveBlock lavaBlock = new EntityLoopUpDownMoveBlock(worldObj);
		lavaBlock.setCanFalling(canFalling());
		lavaBlock.setSpawnXYZ(x, y, z);
		lavaBlock.setTeleport(false);
		lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY() + 0.3, lavaBlock.getSpawnZ());
		lavaBlock.setPositionAndRotationDirect(lavaBlock.getSpawnX(), lavaBlock.getSpawnY() + 0.3, lavaBlock.getSpawnZ(), 90, 90, 1, false);
		this.copyModel(lavaBlock);
		lavaBlock.prevBlock = prevBlock;
		lavaBlock.setBlockMode(getCurrentBlock());
		worldObj.spawnEntityInWorld(lavaBlock);
		return lavaBlock;

	}
	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
		if(CommandJB.isDebMode) {
			if (hand == EnumHand.MAIN_HAND && isServerWorld()) {
				if (stack.getItem() == Items.APPLE) {
					if (player.isSneaking()) {
						ax -= 0.001;
					} else
						ax += 0.001;
					System.out.println(ax);

					return true;

				}
				if (stack.getItem() == Items.GOLDEN_APPLE) {
					if (player.isSneaking()) {
						ax -= 0.003;
					} else
						ax += 0.003;
					System.out.println(ax);
					return true;

				}
			}
		}
		return super.processInteract(player, hand, stack);
	}

	private int delay = 20;

	@Override
	public void onLivingUpdate() {
		if (isServerWorld() && delay > 0) {
			delay--;
		}
		if (WorldAPI.getPlayer() != null && WorldAPI.getPlayer().getDistance(posX, posY, posZ) < 40) {
			if (delay == 0 && posY < getSpawnY() + 2.5) {
				this.setVelocity(0, 0.03, 0);
				this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D,
						this.posX + 0.5D, this.posY + 1, this.posZ + 0.5D));

			} else if (posY >= getSpawnY()) {
				this.setVelocity(0, -0.02, 0);
				this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D,
						this.posX + 0.5D, this.posY + 1, this.posZ + 0.5D));
				delay = 5;
			}
			List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox());
			if (!list.isEmpty()) {
				for (Entity entity : list) {
					if ((entity instanceof EntityPlayer) && !entity.noClip) {
						this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D,
								this.posX + 0.5D, this.posY + 1 + 2.5D, this.posZ + 0.5D));
						entity.onGround = true;
						entity.moveEntity(0, ax, 0);
					}
				}
			}
		} else
			motionY = 0;

		super.onLivingUpdate();
	}

	@Override
	public boolean canTeleportLock() {
		return false;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setDouble("spawnX", getSpawnX());
		compound.setDouble("spawnY", getSpawnY());
		compound.setDouble("spawnZ", getSpawnZ());

	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		setSpawnXYZ(compound.getDouble("spawnX"), compound.getDouble("spawnY"), compound.getDouble("spawnZ"));

	}

}
