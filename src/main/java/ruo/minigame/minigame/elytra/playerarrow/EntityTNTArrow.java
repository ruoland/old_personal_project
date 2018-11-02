package ruo.minigame.minigame.elytra.playerarrow;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.minigame.elytra.miniween.EntityElytraPumpkin;

public class EntityTNTArrow extends EntityDefaultNPC {
    public EntityTNTArrow(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.TNT);
        this.setSize(0.3F,0.3F);
        this.setScale(0.3F,0.3F,0.3F);
        this.isFly = true;
        this.setDeathTimer(200);
    }

    @Override
    public void targetArrive() {
        super.targetArrive();
        this.worldObj.createExplosion(this, posX, posY, posZ, 1F, false);
        this.setDead();
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        if(entityIn instanceof EntityElytraPumpkin){
            this.worldObj.createExplosion(this, posX, posY, posZ, 1F, false);
            entityIn.attackEntityFrom(DamageSource.generic, 3F);
            this.setDead();

            System.out.println(entityIn+"엔티티와 부딪혀 죽음");
        }
    }
}