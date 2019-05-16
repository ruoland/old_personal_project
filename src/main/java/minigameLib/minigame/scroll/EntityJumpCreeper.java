package minigameLib.minigame.scroll;

import olib.api.EntityAPI;
import olib.map.EntityDefaultNPC;
import olib.map.TypeModel;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import map.lopre2.CommandJB;

import java.util.List;

public class EntityJumpCreeper extends EntityDefaultNPC {
    public EntityJumpCreeper(World world)
    {
        super(world);
        setModel(TypeModel.CREEPER);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);
        List<EntityPlayer> list = EntityAPI.getEntity(worldObj, this.getEntityBoundingBox().expand(0,0.5,0), EntityPlayer.class);
        for(EntityPlayer player : list) {
            if (getEntityBoundingBox().intersectsWith(player.getEntityBoundingBox())) {
                worldObj.createExplosion(this, posX, posY, posZ, 3F, false);
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.isFireDamage())
            return false;
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
}
