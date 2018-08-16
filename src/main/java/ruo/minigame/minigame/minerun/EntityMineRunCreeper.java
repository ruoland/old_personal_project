package ruo.minigame.minigame.minerun;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.TypeModel;

public class EntityMineRunCreeper extends EntityDefaultNPC {

    public EntityMineRunCreeper(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.CREEPER);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);
        EntityMineRunCreeper mrcreeper = this;
        if(!entityIn.isCreative() && getDistanceToEntity(entityIn) < 1) {
            worldObj.createExplosion(this, posX, posY, posZ, 2, false);
            this.setDead();
        }
        TickRegister.register(new AbstractTick(200, false) {
            @Override
            public void run(TickEvent.Type type) {
                EntityMineRunCreeper creeper = new EntityMineRunCreeper(worldObj);
                creeper.copyLocationAndAnglesFrom(mrcreeper);
                worldObj.spawnEntityInWorld(creeper);
            }
        });
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
