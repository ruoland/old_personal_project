package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;

public class EntityElytraPumpkinFire extends EntityDefaultNPC {
    public EntityElytraPumpkinFire(World worldIn) {
        super(worldIn);
        setBlockMode(Blocks.PUMPKIN);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.setFire(10);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(2000000000);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        //super.onCollideWithPlayer(entityIn);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        //super.collideWithEntity(entityIn);
    }

    @Override
    protected void collideWithNearbyEntities() {
        //super.collideWithNearbyEntities();
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

}
