package minigameLib.minigame.scroll;

import net.minecraft.entity.Entity;
import olib.api.EntityAPI;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import map.lopre2.CommandJB;

import java.util.List;

public class EntityJumpSpider extends EntitySpider {
    private static final DataParameter<Boolean> IS_JUMP = EntityDataManager.createKey(EntityJumpSpider.class, DataSerializers.BOOLEAN);
    public EntityJumpSpider(World world)
    {
        super(world);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(IS_JUMP, false);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);

    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);
        List<EntityPlayer> list = EntityAPI.getEntity(worldObj, this.getEntityBoundingBox().expand(0,0.5,0), EntityPlayer.class);
        for(EntityPlayer player : list) {
            if (getEntityBoundingBox().intersectsWith(player.getEntityBoundingBox())) {
                if (entityIn.posY > posY + 0.5) {
                    attackEntityFrom(new DamageSource("밟혀서 뎀지 받음"), 1.5F);
                    entityIn.setVelocity(0, 0.5, 0);
                    dataManager.set(IS_JUMP, true);
                    entityIn.fallDistance = 0;
                    entityIn.onGround = true;
                }
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(CommandJB.isDebMode){
            if(source.getEntity() instanceof EntityPlayer){
                EntityPlayer player = (EntityPlayer) source.getEntity();
                if(player.isSneaking()){
                    setDead();
                }
            }
        }
        return super.attackEntityFrom(source, amount);
    }

    public boolean isJump(){
        return dataManager.get(IS_JUMP);
    }

    @Override
    public boolean isAIDisabled() {
        return true;
    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        //super.moveEntityWithHeading(strafe, forward);
    }
}
