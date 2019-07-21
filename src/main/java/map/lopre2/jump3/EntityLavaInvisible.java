package map.lopre2.jump3;

import map.lopre2.CommandJB;
import map.lopre2.EntityPreBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

//일정 시간마다 용암 속에 가라앉는 블럭

public class EntityLavaInvisible extends EntityPreBlock {
    private static final DataParameter<Integer> LAVA_NUMBER = EntityDataManager.createKey(EntityLavaInvisible.class, DataSerializers.VARINT);

    public EntityLavaInvisible(World worldObj) {
        super(worldObj);
        setBlockMode(Blocks.STONE);
        setCollision(true);
        isFly = true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(LAVA_NUMBER, 1);
    }

    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityLavaInvisible lavaInvisible = new EntityLavaInvisible(worldObj);
        dataCopy(lavaInvisible, x, y, z);
        if (isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(lavaInvisible);
        }
        return lavaInvisible;
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (isServerWorld()) {
            int lava = dataManager.get(LAVA_NUMBER);
            dataManager.set(LAVA_NUMBER, lava + 10);
            System.out.println(dataManager.get(LAVA_NUMBER) + " - " + lava);
            if (lava > 100) {
                dataManager.set(LAVA_NUMBER, 0);
            }
        }
        return super.processInteract(player, hand, stack);
    }

    public int lavaNumber(){
        return dataManager.get(LAVA_NUMBER);
    }
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!isTeleport()) {
            if (CommandJB.lavaTick <= lavaNumber() || CommandJB.lavaTick <= lavaNumber() + 40) {//시간이  되서 블럭 내려감
                if (getTraY() < 1) {
                    float a = (float) (((getSpawnY() - 1) - posY) / 20);
                    this.setTra(0, getTraY() - a, 0);
                    System.out.println(getTraY()+"내려가는 중" + a);
                } else
                    setTra(0, 1, 0);
                if(CommandJB.lavaTick > lavaNumber() + 10 ){
                    setCollision(false);
                }
            } else {
                setCollision(true);
                if (getTraY() > 0) {
                    float a = (float) ((getSpawnY() + 0.3) - posY) / 10;
                    setTra(0, getTraY() + -a, 0);
                    System.out.println(getTraY()+"올라가는 중" + -a);

                } else
                    setTra(0, 0, 0);
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("lavaNumber", lavaNumber());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        dataManager.set(LAVA_NUMBER, compound.getInteger("lavaNumber"));
    }

    @Override
    public String getName() {
        return "용암 블럭" + getDataManager().get(LAVA_NUMBER);
    }

    @Override
    public String getText() {
        return "일정시간마다 용암에 사라지는 블럭입니다. 우클릭으로 번호를 지정할 수 있습니다.";
    }
}
