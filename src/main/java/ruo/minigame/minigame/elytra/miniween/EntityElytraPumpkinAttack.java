package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ruo.minigame.api.SpawnDirection;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;

public class EntityElytraPumpkinAttack extends EntityElytraPumpkin {

    public int attackCooldown = 200, defaultCooldown = 40;

    public EntityElytraPumpkinAttack(World worldIn) {
        super(worldIn);
        this.setAttackMode(true);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (isAttackMode()) {
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
        if (isServerWorld())
            worldObj.spawnEntityInWorld(elytraBullet);
        elytraBullet.setDamage(5);
        if (isFireAttack())
            elytraBullet.setFire(100);
        elytraBullet.setTNTMode(isTNTMode());
        elytraBullet.setWaterMode(isWaterMode());
        return elytraBullet;
    }

    @Override
    public void targetArrive() {
        super.targetArrive();
        this.attackCooldown = 40;
    }


}
