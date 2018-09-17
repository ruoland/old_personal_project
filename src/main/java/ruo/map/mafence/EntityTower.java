package ruo.map.mafence;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.TypeModel;

public class EntityTower extends EntityDefaultNPC {
    private static final DataParameter<Integer> TOWER_LEVEL = EntityDataManager.createKey(EntityTower.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TOWER_RANGE = EntityDataManager.createKey(EntityTower.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TOWER_DAMAGE = EntityDataManager.createKey(EntityTower.class, DataSerializers.VARINT);
    protected int attackDelay, maxAttackDelay = 20;

    public EntityTower(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.NPC);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(getTowerRange());
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(TOWER_LEVEL, 1);
        dataManager.register(TOWER_RANGE, 5);
        dataManager.register(TOWER_DAMAGE, 5);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (getAttackTarget() != null) {
            attackDelay++;
            if (attackDelay > maxAttackDelay) {
                towerAttack(getAttackTarget());
            }
        }
    }

    public void towerUpgrade() {
        dataManager.set(TOWER_LEVEL, dataManager.get(TOWER_LEVEL) + 1);
    }

    public void towerAttack(EntityLivingBase target) {
    }

    public void setTowerRange(int i) {
        dataManager.set(TOWER_RANGE, i);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(getTowerRange());
    }

    public void setTowerLevel(int i) {
        dataManager.set(TOWER_LEVEL, i);
    }

    public void setTowerDamage(int i) {
        dataManager.set(TOWER_DAMAGE, i);
    }

    public int getTowerLevel() {
        return dataManager.get(TOWER_LEVEL);
    }

    public int getTowerRange() {
        return dataManager.get(TOWER_RANGE);
    }

    public int getTowerDamage() {
        return dataManager.get(TOWER_DAMAGE);
    }


}
