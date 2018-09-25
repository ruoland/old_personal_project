package ruo.minigame.minigame.minerun;

import net.minecraft.world.World;
import ruo.minigame.MiniGame;
import ruo.minigame.api.Direction;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.TypeModel;

public class EntityMRWalkingZombie extends EntityMR {

    public EntityMRWalkingZombie(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.PIG_ZOMBIE);
        this.isLookPlayer = false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (MiniGame.minerun.isStart() && WorldAPI.getPlayer() != null) {

            getLookHelper().setLookPosition(posX + (MineRun.zCoord() * 2), posY + getEyeHeight(), posZ + (MineRun.xCoord() * 2), 360, 360);
            if (getDistanceToEntity(WorldAPI.getPlayer()) < 7) {
                setVelocity(getX(Direction.FORWARD, 0.08, false), 0, getZ(Direction.FORWARD, 0.08, false));
            } else
                setPosition(getSpawnPosVec());

        }
    }
}
