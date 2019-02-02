package map.mafence;

import net.minecraft.entity.ai.EntityAIBase;

public class EntityAILookMonster extends EntityAIBase {
    private EntityTower tower;
    private EntityMonster target;
    private int attackDelay;

    public EntityAILookMonster(EntityTower tower) {
        this.tower = tower;
    }

    @Override
    public boolean shouldExecute() {
        return tower.getAttackTarget() != null && (tower.isDead || Mafence.MAFENCE_START);
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        target = (EntityMonster) tower.getAttackTarget();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        tower.getLookHelper().setLookPositionWithEntity(target, 360, 360);
    }
}
