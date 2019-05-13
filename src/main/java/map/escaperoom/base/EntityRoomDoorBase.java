package map.escaperoom.base;

import oneline.map.EntityDefaultNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityRoomDoorBase extends EntityDefaultNPC {
    //defcount와 count 존재 이유는 스위치를 여러개 활성화 해야만 문이 열리게 하는 기능을 위해서 있음
    private int openMinCount;//count 가 defcount보다 높은 경우에만 문이 열림
    private int openCount;//문이 열리라고 명령받은 횟수
    private boolean isOpen;
    private static final DataParameter<String> DOOR_NAME = EntityDataManager.createKey(EntityRoomDoorBase.class, DataSerializers.STRING);

    public EntityRoomDoorBase(World worldIn) {
        super(worldIn);
        this.setSize(3, 5);
        this.setBlockMode(Blocks.STONE);
        this.setScale(3, 5, 1);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(DOOR_NAME, "");
    }


    public void close() {
        System.out.println("문 닫음");
        isOpen = false;
    }

    public void open() {
        System.out.println("문 열음" + openMinCount + " - " + openCount);
        if (openMinCount == 0 || (openMinCount > 0 && openCount == openMinCount)) {
            isOpen = true;
            openCount = 0;
        }
        openCount++;
    }

    public void setOpenMinCount(int openMinCount) {
        this.openMinCount = openMinCount;
    }

    public int getOpenMinCount() {
        return openMinCount;
    }

    public void setDoorName(String doorName) {
        this.dataManager.set(DOOR_NAME, doorName);
    }

    public String getDoorName() {
        return dataManager.get(DOOR_NAME);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("count", openCount);
        compound.setInteger("openMinCount", openMinCount);
        compound.setString("DoorName", getDoorName());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        openCount = compound.getInteger("count");
        openMinCount = compound.getInteger("openMinCount");
        setDoorName(compound.getString("DoorName"));
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (hand == EnumHand.MAIN_HAND) {
            if (player.isSneaking()) {
                open();
            } else
                close();
        }
        return super.processInteract(player, hand, stack);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.rotationYaw = -90;
        this.renderYawOffset = -90;
        openAnimation();
    }

    public void openAnimation(){
        float y = 1F / 20F;
        if (isOpen) {
            if (getTraY() >= 3) {
                setSize(1, 1);
                return;
            }
            setTra(0, getTraY() + y, 0);
            worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ, 0D, 0.0D, 0.0D);
            worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ + 1, 0.0D, 0.0D, 0.0D);
            worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ - 1, 0D, 0.0D, 0.0D);
        }
        if (!isOpen) {
            if (getTraY() <= -2) {
                return;
            }
            setTra(0, getTraY() - y, 0);
            worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ, 0D, 0.0D, 0.0D);
            worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ + 1, 0.0D, 0.0D, 0.0D);
            worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ - 1, 0D, 0.0D, 0.0D);

        }
        System.out.println("문 "+isOpen + " - "+getTraY());
    }
}
