package ruo.yout;

import net.minecraft.entity.ai.EntityAIBase;
import olib.api.WorldAPI;

public class EntityAIFlyingMove extends EntityAIBase {
    private EntityFlyingCreeperLab creeperLab;
    private int moveTime;
    public EntityAIFlyingMove(EntityFlyingCreeperLab creeperLab){
        this.creeperLab = creeperLab;
        this.setMutexBits(3);

    }
    @Override
    public boolean shouldExecute() {
        return (creeperLab.getAttackTarget() == null && creeperLab.noTarget() && creeperLab.arriveCooltime == 0 && creeperLab.worldObj.rand.nextInt(10) == 0);
    }

    @Override
    public void startExecuting() {
        creeperLab.setTarget(creeperLab.posX+WorldAPI.rand(5), creeperLab.posY+WorldAPI.rand(1), creeperLab.posZ+ WorldAPI.rand(5));
        moveTime = 0;
    }

    @Override
    public void updateTask() {
        super.updateTask();
        moveTime++;
    }
}
