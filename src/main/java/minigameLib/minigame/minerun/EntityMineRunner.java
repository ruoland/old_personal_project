package minigameLib.minigame.minerun;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import olib.map.EntityDefaultNPC;
import olib.map.TypeModel;

public class EntityMineRunner extends EntityDefaultNPC {
    private static final DataParameter<Boolean> isCollision = EntityDataManager.createKey(EntityMineRunner.class, DataSerializers.BOOLEAN);
    public EntityMineRunner(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.NPC);
        this.setCollision(true);
    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    public void jump() {
        super.jump();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(isCollision, false);
    }

    @Override
    protected void doBlockCollisions() {
        super.doBlockCollisions();
        dataManager.set(isCollision, true);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        extinguish();
    }

    @Override
    public void knockBack(Entity entityIn, float strenght, double xRatio, double zRatio) {
        //super.knockBack(entityIn, strenght, xRatio, zRatio);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean isOnLadder() {
        return super.isOnLadder();
    }

    public void updatePos(){
        dataManager.set(isCollision, false);
    }
}
