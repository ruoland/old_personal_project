package map.escaperoom.nouse.base;

import olib.map.EntityDefaultBlock;
import olib.map.TypeModel;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityRoomWindBase extends EntityDefaultBlock {
    private static final DataParameter<Integer> DELAY = EntityDataManager.createKey(EntityRoomWindBase.class, DataSerializers.VARINT);

    public EntityRoomWindBase(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.PUMPKIN);
        this.typeModel = TypeModel.SHAPE_BLOCK;
        this.setSize(width, height + 1);
        this.setTra(0,2,0);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(DELAY, 0);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (addDelay(1, 20, 200)) {
            onEffect();
        }
    }

    public void onEffect(){

    }

    public int getDelay() {
        return dataManager.get(DELAY);
    }

    public boolean addDelay(int addDelay, int minDelay, int maxDelay) {
        dataManager.set(DELAY, getDelay() + addDelay);
        if(getDelay() >= minDelay && getDelay() <= maxDelay){
            if(getDelay() == maxDelay){
                setDelay(0);
            }
            return true;
        }
        return false;
    }

    public void setDelay(int delay) {
        dataManager.set(DELAY, delay);
    }
}
