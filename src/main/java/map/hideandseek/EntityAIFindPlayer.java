package map.hideandseek;

import minigameLib.api.EntityAPI;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class EntityAIFindPlayer extends EntityAIBase {
    private EntityGuardLoop guard;
    private World worldObj;
    private int rotationDelay, findDelay;
    private float prevYaw;
    private AxisAlignedBB aabb = null;
    public EntityAIFindPlayer(EntityGuardLoop guard){
        this.guard = guard;
        worldObj = guard.worldObj;
    }

    @Override
    public boolean shouldExecute() {
        return guard.getTraceEntity() == null;
    }

    @Override
    public void updateTask() {
        super.updateTask();
        rotationDelay++;
        findDelay++;
        this.guard.getNavigator().tryMoveToXYZ(guard.getSpawnX(), guard.getSpawnY(), guard.getSpawnZ(), 1);
        if(rotationDelay > 60){
            rotationDelay = 0;
            prevYaw = guard.rotationYawHead + 90;
            aabb = new AxisAlignedBB(EntityAPI.lookX(guard, -30, 5), guard.posY- 3, EntityAPI.lookZ(guard, -30, 5)
                    , EntityAPI.lookX(guard, 30, 5), guard.posY+3, EntityAPI.lookZ(guard, 30, 5));
        }
        guard.rotationYawHead = prevYaw;
        guard.renderYawOffset = prevYaw;

        if(findDelay > 20) {
            findDelay = 0;
            List<EntityPlayer> playerList = EntityAPI.getEntity(worldObj, aabb, EntityPlayer.class);
            for (EntityPlayer player : playerList) {
                if (guard.getEntitySenses().canSee(player)) {
                    guard.setTraceEntity(player);
                }
            }
        }
    }
}
