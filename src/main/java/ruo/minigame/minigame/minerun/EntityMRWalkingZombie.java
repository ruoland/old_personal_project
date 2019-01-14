package ruo.minigame.minigame.minerun;

import net.minecraft.world.World;
import ruo.minigame.MiniGame;
import ruo.minigame.api.Direction;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.TypeModel;

public class EntityMRWalkingZombie extends EntityMR {
    Direction[] directions = new Direction[]{Direction.BACK, Direction.FORWARD};
    Direction direction = directions[rand.nextInt(1)];
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
                setVelocity(getX(direction, 0.2, false), 0, getZ(direction, 0.2, false));
            } else
                setPosition(getSpawnPosVec());

        }else
            teleportSpawnPos();
    }
}
