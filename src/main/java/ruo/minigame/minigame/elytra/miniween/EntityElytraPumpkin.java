package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import ruo.minigame.MiniGame;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.map.EntityDefaultNPC;

public class EntityElytraPumpkin extends EntityDefaultNPC {
    public EntityElytraPumpkin(World world) {
        super(world);
        this.setBlockMode(Blocks.PUMPKIN);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
    }

    public void setting() {
        getLookHelper().setLookPosition(FakePlayerHelper.fakePlayer.posX, FakePlayerHelper.fakePlayer.posY, FakePlayerHelper.fakePlayer.posZ
                , getHorizontalFaceSpeed(), getVerticalFaceSpeed());
        faceEntity(FakePlayerHelper.fakePlayer, getHorizontalFaceSpeed(), getVerticalFaceSpeed());
    }

    int cooldown = 40;

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
        if (fakePlayer != null) {
            if (fakePlayer.getDistanceToEntity(this) > 15) {
                rotationYaw = -fakePlayer.rotationYaw;
                rotationPitch = -fakePlayer.rotationPitch;
                this.setVelocity(EntityAPI.forwardX(this, 5, true) - posX, 0, EntityAPI.forwardZ(this, 5, true) - posZ);
            }
            if (cooldown > 0)
                cooldown--;
            this.motionY = 0;
            if (cooldown == 0) {
                spawnBullet();
                cooldown = 40;
            }
        }
    }

    public EntityElytraBullet spawnBullet(float pitch, float yaw) {
        double[] lookXZ = WorldAPI.getVecXZ(pitch, yaw, 5);
        EntityElytraBullet elytraBullet = new EntityElytraBullet(worldObj);
        elytraBullet.setPosition(posX, posY, posZ);
        worldObj.spawnEntityInWorld(elytraBullet);
        elytraBullet.setTarget(posX + lookXZ[0], posY, posZ + lookXZ[1]);
        return elytraBullet;
    }

    public EntityElytraBullet spawnBullet() {
        return spawnBullet(rotationPitch, rotationYaw);
    }
}
