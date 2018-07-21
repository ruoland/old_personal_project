package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
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
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        //super.onCollideWithPlayer(entityIn);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        //super.collideWithEntity(entityIn);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(2000000000);
    }

    int cooldown = 40;

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
        if (fakePlayer != null) {
            faceEntity(fakePlayer, 360,360);
            if (cooldown > 0)
                cooldown--;
            this.motionY = 0;
            if (cooldown == 0) {
                if(isServerWorld())
                spawnBullet();
                cooldown = 40;
            }
        }
    }

    public EntityElytraBullet spawnBullet() {
        EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;;
        EntityElytraBullet elytraBullet = new EntityElytraBullet(worldObj);
        elytraBullet.setPosition(posX, posY, posZ);
        elytraBullet.setTarget( fakePlayer.posX, posY, fakePlayer.posZ);
        worldObj.spawnEntityInWorld(elytraBullet);
        elytraBullet.setDamage(5);
        return elytraBullet;
    }

}
