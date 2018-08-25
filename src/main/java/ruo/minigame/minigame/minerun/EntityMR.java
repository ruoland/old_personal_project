package ruo.minigame.minigame.minerun;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;
import ruo.minigame.map.EntityDefaultNPC;
import scala.xml.dtd.EntityDef;

public class EntityMR extends EntityDefaultNPC {

    public EntityMR(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        if (getDistanceToEntity(entityIn) < 1 && !isSturn()) {
            this.swingArm();
            this.setLastAttacker(entityIn);
            collideAttack(entityIn);
            this.setDead();
        }
    }

    public void collideAttack(Entity entityIn){
        entityIn.attackEntityFrom(DamageSource.cactus, 3);
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
    @Override
    public void onSturn() {
        if(!isSturn())
            setInvisible(false);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(WorldAPI.getPlayer() != null)
        this.faceEntity(WorldAPI.getPlayer(), 360, 360);
    }
}
