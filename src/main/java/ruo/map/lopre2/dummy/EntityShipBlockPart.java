package ruo.map.lopre2.dummy;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.EntityDefaultNPC;

import java.util.List;

public class EntityShipBlockPart extends EntityDefaultNPC {
	EntityShipBlock shipblock;
	int count;
	private static final DataParameter<Integer> COUNT = EntityDataManager.<Integer>createKey(EntityShipBlockPart.class, DataSerializers.VARINT);
	public EntityShipBlockPart(World worldIn) {
		super(worldIn);
		this.setBlockMode(Blocks.STONE);
		this.setCollision(true);
		this.isFly = true;
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(COUNT, 0);
	}

	public EntityShipBlockPart(World worldIn, EntityShipBlock shipblock, int count) {
		this(worldIn);
		this.shipblock = shipblock;
		this.count = count;
		if (count == 0)
			setPosition(shipblock.posX, shipblock.posY, shipblock.posZ - 1);//바로 뒤에 설치함
		if (count == 1)
			setPosition(shipblock.posX - 1, shipblock.posY, shipblock.posZ);//왼쪽에 설치함
		if (count == 2)
			setPosition(shipblock.posX - 1, shipblock.posY, shipblock.posZ - 1);//윈쪽 뒤에 설치
		if (count == 3)
			setPosition(shipblock.posX+1, shipblock.posY, shipblock.posZ);//오른쪽에 설치함
		if (count == 4)
			setPosition(shipblock.posX+1, shipblock.posY, shipblock.posZ - 1);//오른쪽 뒤에 설치
		if (count == 5)
			setPosition(shipblock.posX - 1, shipblock.posY, shipblock.posZ - 2);//맨오른쪽 뒤
		if (count == 6)
			setPosition(shipblock.posX + 1, shipblock.posY, shipblock.posZ - 2);//맨 왼쪽 뒤
		if (count == 7)
			setPosition(shipblock.posX, shipblock.posY, shipblock.posZ - 2);//맨 뒤
		this.dataManager.set(COUNT, count);
	}
	public void onLivingUpdate() {
		super.onLivingUpdate();
		
		List<Entity> list;
		if(isServerWorld()) {
			if (shipblock == null) {
				System.out.println("shipblock이 없어 삭제됨");
				this.setDead();
				return;
			}	
		}
		if (!isServerWorld() && shipblock == null) {
			list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(
					this.posX - 5.5D, this.posY, this.posZ - 5.5D, this.posX + 5.5D, this.posY + 5.5, this.posZ + 5.5D));
			for (Entity entity : list) {
				if ((entity instanceof EntityShipBlock)) {
					shipblock = (EntityShipBlock) entity;
					System.out.println("shipblock 가져옴");

				}
			}
		}
		if(shipblock == null) {
			this.setDead();
			return;
		}
		if (dataManager.get(COUNT).intValue() == 1) {
			list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(
					this.posX - 0D, this.posY, this.posZ - 2.5D, this.posX + 2.5D, this.posY + 1.5, this.posZ + 0.5D));
			if (!list.isEmpty()) {
				for (Entity entity : list) {
					if ((entity instanceof EntityPlayer)) {
						boolean isLava = false;

						for(EntityShipBlockPart part : shipblock.partArray) {
							if(part != null && isMaterialLava(part)) {
								isLava = true;
								break;
							}
						}
						if(!isLava) {
							isLava = isMaterialLava(shipblock);
						}
						if(Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown()) {
							continue;
						}else {
							if(WorldAPI.getPlayerSP() == null)
								return;
							WorldAPI.getPlayerSP().moveEntity(0, (isLava ? 0.07 : 0) , (isLava ? 0 : 0.04));
						}
						shipblock.moveEntity(0, (isLava ? 0.06 : 0), (isLava ? 0 : 0.06));
						if(shipblock.motionX == 0 && shipblock.motionY == 0 && shipblock.motionZ == 0)
							WorldAPI.getPlayerSP().moveEntity(0,0,0);
						//System.out.println(shipblock+" - "+isServerWorld()+" - "+isLava+" - "+entity);
					}
				}
			}
		}
			if (dataManager.get(COUNT).intValue() == 0)
				setPosition(shipblock.posX, shipblock.posY, shipblock.posZ - 1);//바로 뒤에 설치함
			if (dataManager.get(COUNT).intValue() == 1)
				setPosition(shipblock.posX - 1, shipblock.posY, shipblock.posZ);//오른쪽에 설치함
			if (dataManager.get(COUNT).intValue() == 2)
				setPosition(shipblock.posX - 1, shipblock.posY, shipblock.posZ - 1);//오른쪽 뒤에 설치
			if (dataManager.get(COUNT).intValue() == 3)
				setPosition(shipblock.posX+1, shipblock.posY, shipblock.posZ);//왼쪽에 설치함
			if (dataManager.get(COUNT).intValue() == 4)
				setPosition(shipblock.posX+1, shipblock.posY, shipblock.posZ - 1);//왼쪽 뒤에 설치
			if (dataManager.get(COUNT).intValue() == 5)
				setPosition(shipblock.posX +1, shipblock.posY, shipblock.posZ - 2);//왼쪽 맨뒤
			if (dataManager.get(COUNT).intValue() == 6)
				setPosition(shipblock.posX - 1, shipblock.posY, shipblock.posZ - 2);//오른쪽 맨뒤
			if (dataManager.get(COUNT).intValue() == 7)
				setPosition(shipblock.posX, shipblock.posY, shipblock.posZ - 2);//맨 뒤
		
	}

	public boolean isMaterialLava(Entity shipblock) {
		return shipblock.worldObj.isMaterialInBB(new AxisAlignedBB(shipblock.posX-0.3, shipblock.posY+1, shipblock.posZ-0.3, shipblock.posX+0.3, shipblock.posY+2, shipblock.posZ+0.3), Material.LAVA);
	}
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("COUNT", dataManager.get(COUNT));
	}
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		dataManager.set(COUNT, compound.getInteger("COUNT"));

	}
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if(!source.isCreativePlayer() || source == DamageSource.lava) {
			return false;
		}
		return super.attackEntityFrom(source, amount);
	}
	@Override
	public boolean canRenderOnFire() {
		return !super.canRenderOnFire();
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

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
		System.out.println(dataManager.get(COUNT).intValue());
		return super.processInteract(player, hand, stack);
	}
}
