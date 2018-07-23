package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;

public class EntityElytraPumpkinFire extends EntityElytraPumpkin {
    public EntityElytraPumpkinFire(World worldIn) {
        super(worldIn);
        setBlockMode(Blocks.PUMPKIN);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.setFire(10);
        if(getSpawnDirection() != null)
        this.setVelocity(getXZ(getSpawnDirection().reverse(), 0.03, false));
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        entityIn.setFire(2);
    }
}
