package minigameLib.minigame.minerun;

import minigameLib.map.EntityDefaultNPC;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityMRFallingBlockCreator extends EntityMR {
    private static final DataParameter<Integer>  FALLING_DELAY  = EntityDataManager.createKey(EntityMRFallingBlockCreator.class, DataSerializers.VARINT);
    public EntityMRFallingBlockCreator(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.TNT);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(FALLING_DELAY, 0);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        dataManager.set(FALLING_DELAY, dataManager.get(FALLING_DELAY)+1);
        if(dataManager.get(FALLING_DELAY) > 60){
            dataManager.set(FALLING_DELAY, 0);
            EntityMRFallingBlock defaultNPC = new EntityMRFallingBlock(worldObj);
            defaultNPC.setBlockMode(Blocks.ANVIL);
            defaultNPC.setPosition(posX,posY,posZ);
            if(isServerWorld())
            worldObj.spawnEntityInWorld(defaultNPC);
        }
    }
}
