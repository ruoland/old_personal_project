package minigameLib.minigame.elytra.miniween;

import minigameLib.MiniGame;
import oneline.api.Direction;
import oneline.fakeplayer.EntityFakePlayer;
import oneline.fakeplayer.FakePlayerHelper;
import net.minecraft.world.World;

public class EntityElytraPumpkinAttack extends EntityElytraPumpkin {


    public EntityElytraPumpkinAttack(World worldIn) {
        super(worldIn);
        this.setAttackMode(true);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (isAttackMode()) {
            if (attackCooldown > 0) {
                attackCooldown--;
            }
            if (attackCooldown == 0) {
                Direction direction = MiniGame.elytra.spawnPosHelper.getDirection(this);
                if (!direction.isBack())//페이크 플레이어가 뒤에 있을 떄만 공격하게
                    spawnBullet();
                attackCooldown = defaultCooldown;
            }
        }
    }

    public EntityElytraBullet spawnBullet() {
        EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
        EntityElytraBullet elytraBullet = new EntityElytraBullet(worldObj);
        elytraBullet.setPosition(posX, posY + 1.7, posZ);
        elytraBullet.setTarget(fakePlayer.posX, posY + 1.7, fakePlayer.posZ);
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
