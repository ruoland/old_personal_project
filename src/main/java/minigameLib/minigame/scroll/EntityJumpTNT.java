package minigameLib.minigame.scroll;

import minigameLib.map.EntityDefaultNPC;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import rmap.lopre2.CommandJB;

public class EntityJumpTNT extends EntityDefaultNPC {
    public EntityJumpTNT(World world)
    {
        super(world);
        this.setBlockMode(Blocks.TNT);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);

    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);
        if(getEntityBoundingBox().intersectsWith(entityIn.getEntityBoundingBox())){
            if(entityIn.posY > posY+0.5){
                worldObj.createExplosion(this, posX, posY, posZ, 1.5F, false);
                entityIn.setHealth(0);
            }
        }
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

    @Override
    public boolean isAIDisabled() {
        return true;
    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        //super.moveEntityWithHeading(strafe, forward);
    }
}
