package map.lot;

import minigameLib.map.EntityDefaultNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityDoorBlock extends EntityDefaultNPC{
	//defcount와 count 존재 이유는 스위치를 여러개 활성화 해야만 문이 열리게 하는 기능을 위해서 있음
	private int defcount;//count 가 defcount보다 높은 경우에만 문이 열림
	private int count;//문이 열리라고 명령받은 횟수
	private boolean isClose, isOpen;
	private int closeTick;
	public EntityDoorBlock(World worldIn) {
		super(worldIn);
		this.setSize(3, 5);
		this.setBlockMode(Blocks.STONE);
		this.setScale(3,5,1);
	}

	public void close() {
	    System.out.println("문 닫음");
	    isClose = true;
	}

	public void open() {
        System.out.println("문 열음"+defcount+" - "+count);
        if(getCustomNameTag().indexOf("c:") != -1){
			defcount = Integer.valueOf(getCustomNameTag().split("c:")[1]);
		}
		if(defcount == 0 || (defcount > 0 && count == defcount)) {
			isOpen = true;
        }

        if(getCustomNameTag().indexOf("c:") != -1){
            count++;
        }
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("count", count);
		compound.setInteger("defcount", defcount);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		count = compound.getInteger("count");
		defcount = compound.getInteger("defcount");
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
		if(hand == EnumHand.MAIN_HAND) {
			if(player.isSneaking()) {
				open();
			}else
				close();
		}
		return super.processInteract(player, hand, stack);
	}
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.rotationYaw = -90;
		this.renderYawOffset = -90;
		float y = 1F / 20F;

		if(isOpen){
			setTra(0, getTraY() + y, 0);
			if (getTraY() == 3) {
				setSize(0,0);
				isOpen = false;
			}
			worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ, 0D, 0.0D, 0.0D, new int[0]);
			worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ + 1, 0.0D, 0.0D, 0.0D, new int[0]);
			worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ - 1, 0D, 0.0D, 0.0D, new int[0]);
		}
		if(isClose) {
			setTra(0, getTraY() - y, 0);

			if (getTraY() == -3) {
				isClose = false;
			}
			worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ, 0D, 0.0D, 0.0D, new int[0]);
			worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ + 1, 0.0D, 0.0D, 0.0D, new int[0]);
			worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ - 1, 0D, 0.0D, 0.0D, new int[0]);
		}
	}
}
