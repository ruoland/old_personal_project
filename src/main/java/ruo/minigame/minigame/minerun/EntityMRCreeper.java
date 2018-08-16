package ruo.minigame.minigame.minerun;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.TypeModel;

public class EntityMRCreeper extends EntityDefaultNPC {

    public EntityMRCreeper(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.CREEPER);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        if(entityIn instanceof EntityFakePlayer){
            EntityMRCreeper mrcreeper = this;
            if(getDistanceToEntity(entityIn) < 1) {
                worldObj.createExplosion(this, posX, posY, posZ, 1.5F, false);
                this.setDead();
                TickRegister.register(new AbstractTick(200, false) {
                    @Override
                    public void run(TickEvent.Type type) {
                        EntityMRCreeper creeper = new EntityMRCreeper(worldObj);
                        creeper.copyLocationAndAnglesFrom(mrcreeper);
                        worldObj.spawnEntityInWorld(creeper);
                    }
                });
            }
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getEntity() instanceof EntityPlayer){
            if(source.getEntity().isSneaking()){
                this.setDead();
            }
        }
        return super.attackEntityFrom(source, amount);
    }
}
