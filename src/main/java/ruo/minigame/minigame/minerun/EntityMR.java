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
        this.setCollision(true);
        isFly = true;
    }

    @Override
    public void knockBack(Entity entityIn, float strenght, double xRatio, double zRatio) {
        //super.knockBack(entityIn, strenght, xRatio, zRatio);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        //super.collideWithEntity(entityIn);
        if (entityIn instanceof EntityPlayer && !((EntityPlayer) entityIn).isCreative() && getDistanceToEntity(entityIn) < 1 && !isSturn()) {
            this.swingArm();
            this.setLastAttacker(entityIn);
            collideAttack(entityIn);
            this.setSturn(100);
        }
    }

    public void collideAttack(Entity entityIn) {
        entityIn.attackEntityFrom(DamageSource.cactus, 3);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getEntity() instanceof EntityPlayer) {
            if (source.getEntity().isSneaking()) {
                this.setDead();
            }
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void onSturn() {
        setInvisible(!isSturn());
        System.out.println("스턴"+isSturn());
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (WorldAPI.getPlayer() != null)
            this.faceEntity(WorldAPI.getPlayer(), 360, 360);
        this.setVelocity(0,0,0);

    }
}
