package ruo.minigame.minigame.minerun;

import net.minecraft.world.World;
import ruo.minigame.api.SpawnDirection;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.TypeModel;

public class EntityMRPigZombie extends EntityMR {

    public EntityMRPigZombie(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.PIG_ZOMBIE);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        getLookHelper().setLookPosition(posX+(MineRun.zCoord()*2), posY+getEyeHeight(), posZ+ (MineRun.xCoord() * 2), 360,360);
        if(getDistanceToEntity(WorldAPI.getPlayer()) < 7){
            setVelocity(getX(SpawnDirection.FORWARD, 0.01, false), 0, getZ(SpawnDirection.FORWARD, 0.01, false));
        }else
            setPosition(getSpawnPosVec());

    }
}
