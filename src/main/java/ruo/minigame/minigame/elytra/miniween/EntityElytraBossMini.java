package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;

public class EntityElytraBossMini extends EntityElytraPumpkin {

    public EntityElytraBossMini(World world) {
        super(world);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        setTarget(0, 0, 0, 0);
        isFly = false;
        return super.attackEntityFrom(source, amount);
    }

    private int targetCount = 0, absRunCount;
    private boolean secondPhase;

    @Override
    public void targetArrive() {
        super.targetArrive();
        targetCount++;
        if (targetCount == 1) {
            EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
            if (secondPhase) {
                double[] vec3d = WorldAPI.getVecXZ(rotationPitch, rotationYaw + (absRunCount * 35), 15);
                setTarget(posX + vec3d[0], posY, posZ + vec3d[1]);
            } else
                setTarget(fakePlayer.posX, fakePlayer.posY, fakePlayer.posZ);
        }
        if (targetCount == 2)
            setDead();
    }

    public void setSecondPhase(boolean secondPhase, int absRunCount) {
        this.secondPhase = secondPhase;
        this.absRunCount = absRunCount;
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        System.out.println(entityIn + "와 충돌함");
        if (!isFly && entityIn instanceof EntityElytraBossWeen) {
            entityIn.attackEntityFrom(DamageSource.magic, 15);
            worldObj.createExplosion(this, posX, posY, posZ, 3F, true);
            this.setDead();
        }
    }
}
