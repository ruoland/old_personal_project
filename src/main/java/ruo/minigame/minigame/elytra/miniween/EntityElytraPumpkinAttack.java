package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;

public class EntityElytraPumpkinAttack extends EntityElytraPumpkin {

    public EntityElytraPumpkinAttack(World worldIn) {
        super(worldIn);
        this.setDeathTimer(600);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(isAttackMode()) {
            if (attackCooldown > 0)
                attackCooldown--;
            if (attackCooldown == 0) {
                if (isServerWorld())
                    spawnBullet();
                attackCooldown = 40;
            }
        }
    }
}
