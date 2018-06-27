package ruo.map.lopre2.jump2;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruo.map.lopre2.CommandJB;
import ruo.map.lopre2.EntityPreBlock;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;

import java.util.List;

public class EntityJumpSpider extends EntitySpider {
    private static final DataParameter<Boolean> ISJUMP = EntityDataManager.createKey(EntityJumpSpider.class, DataSerializers.BOOLEAN);
    public EntityJumpSpider(World world)
    {
        super(world);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(ISJUMP, false);
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
        for(EntityPlayer player : list){
            if(entityIn.posY > posY+0.5){
                attackEntityFrom(new DamageSource("밟혀서 뎀지 받음"), 1.5F);
                entityIn.setVelocity(0,0.5,0);
                dataManager.set(ISJUMP, true);
                entityIn.fallDistance = 0;
                entityIn.onGround = true;
            }
        }

    }
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(CommandJB.isDebMode) {
            if (source.getEntity() != null && (source.getEntity().isSneaking() || WorldAPI.equalsHeldItem(Items.BONE) || WorldAPI.equalsHeldItem(Items.ARROW))) {
                if (WorldAPI.equalsHeldItem(Items.ARROW)) {
                    List<EntityPreBlock> list = EntityAPI.getEntity(worldObj, this.getEntityBoundingBox().expand(5, 5, 5), EntityPreBlock.class);
                    for (EntityPreBlock pre : list) {
                        pre.setDead();
                    }
                }
                this.setDead();
            }
        }
        return false;
    }

    public boolean isJump(){
        return dataManager.get(ISJUMP);
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
