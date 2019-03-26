package map.lot;

import oneline.map.EntityDefaultNPC;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

//바닥을 떨어트리는 연출에 사용함
//렌덤 딜레이는 블럭이 다 다르게 떨어트리게 만들기 위해서
//MAXY는 뭔지 모르겠음
public class EntityFallBlock extends EntityDefaultNPC {
    private static final DataParameter<Integer> RANDOM_DELAY = EntityDataManager.<Integer>createKey(EntityFallBlock.class,
            DataSerializers.VARINT);
    private static final DataParameter<Float> MAX_Y = EntityDataManager.<Float>createKey(EntityFallBlock.class,
            DataSerializers.FLOAT);
    public EntityFallBlock(World worldIn) {
        super(worldIn);
        this.setCollision(true);
        if (getCurrentBlock() == Blocks.PUMPKIN) {
            setBlockMode(Blocks.STONE);
        }
        isFly = true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(RANDOM_DELAY, worldObj.rand.nextInt(100));
        dataManager.register(MAX_Y, (float) posY);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getEntity() != null && source.getEntity().isSneaking()) {//디버그용
            this.setDead();
        }
        return false;
    }

    public void setRandomDelay(int delay) {
        this.dataManager.set(RANDOM_DELAY, delay);
    }
    public int getRandomDelay() {
        return dataManager.get(RANDOM_DELAY);
    }

    public void setMaxY(float y) {
        this.dataManager.set(MAX_Y, y);
    }
    public float getMaxY() {
        return dataManager.get(MAX_Y);
    }


    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(getRandomDelay() > -10) {
            setRandomDelay(getRandomDelay() - 1);
        }
        if(getRandomDelay() == 0)//딜레이가 0이 됐으므로 떨어트림
            isFly = false;
        if (getRandomDelay() == -10) {//딜레이가 -10이 됐으므로 없앰
            this.setDead();
        }

        if(getRandomDelay() == -190) {
            if(getMaxY() > posY)
                motionY+=0.02;
            else
                isFly = true;
        }
    }
}
