package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.world.World;

public class EntityElytraPumpkinAttack extends EntityElytraPumpkin {

    public EntityElytraPumpkinAttack(World worldIn) {
        super(worldIn);
        this.setAttack(true);
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
