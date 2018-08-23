package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.world.World;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;

public class EntityElytraPumpkinAttack extends EntityElytraPumpkin {
    public int attackCooldown = 40, defaultCooldown = 40;
    public EntityElytraPumpkinAttack(World worldIn) {
        super(worldIn);
        this.setAttack(true);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(isAttackMode()) {
            if (attackCooldown > 0) {
                attackCooldown--;
            }
            if (attackCooldown == 0) {
                spawnBullet();
                attackCooldown = defaultCooldown;
            }
        }
    }
    public EntityElytraBullet spawnBullet() {
        EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
        EntityElytraBullet elytraBullet = new EntityElytraBullet(worldObj);
        elytraBullet.setPosition(posX, posY, posZ);
        elytraBullet.setTarget(fakePlayer.posX, posY, fakePlayer.posZ);
        if(isServerWorld())
        worldObj.spawnEntityInWorld(elytraBullet);
        elytraBullet.setDamage(5);
        if (isFireAttack())
            elytraBullet.setFire(100);
        return elytraBullet;
    }
    @Override
    public void targetArrive() {
        super.targetArrive();
    }
}
