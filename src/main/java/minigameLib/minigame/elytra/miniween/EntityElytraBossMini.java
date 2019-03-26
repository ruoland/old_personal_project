package minigameLib.minigame.elytra.miniween;

import oneline.api.WorldAPI;
import oneline.fakeplayer.EntityFakePlayer;
import oneline.fakeplayer.FakePlayerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

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
    private boolean phase360, threePhase;

    @Override
    public void targetArrive() {
        super.targetArrive();
        targetCount++;
        if (targetCount == 1) {
            EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
            if(threePhase){
                setTarget(posX, posY+10, posZ);
            }
            else if (phase360) {
                double[] vec3d = WorldAPI.getVecXZ(rotationPitch, rotationYaw + (absRunCount * 35), 15);
                setTarget(posX + vec3d[0], posY, posZ + vec3d[1]);
            } else
                setTarget(fakePlayer.posX, fakePlayer.posY, fakePlayer.posZ);
        }
        if (targetCount == 2)
            setDead();
    }

    public void setPhase360(boolean phase360, int absRunCount) {
        this.phase360 = phase360;
        this.absRunCount = absRunCount;
    }
    public void setThreePhase(boolean secondPhase, int absRunCount) {
        this.threePhase = secondPhase;
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
