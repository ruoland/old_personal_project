package ruo.asdf;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.map.TypeModel;

import javax.annotation.Nullable;

public class EntityFlyingCreeper extends EntityDefaultNPC {
    private int delay = 0, attackDelay = 0;
    private Vec3d targetVelocity, targetXYZ;
    public EntityFlyingCreeper(World worldIn) {
        super(worldIn);
        this.setModel(TypeModel.CREEPER);
        this.setElytra(true);
        experienceValue = 30;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        this.setPosition(getPosition().add(0, 10, 0));
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        attackDelay++;
        if (!noTarget() && getDistance(targetXYZ.xCoord, posY, targetXYZ.zCoord) > 1) {
            setVelocity(targetVelocity);
        }else{
            setTarget(null);
        }
        if (attackDelay > 50 && getAttackTarget() instanceof EntityPlayer) {
            EntityTNTPrimed tntPrimed = new EntityTNTPrimed(worldObj, posX, posY, posZ, this);
            tntPrimed.setFuse(worldObj.rand.nextInt(tntPrimed.getFuse() / 4 + tntPrimed.getFuse() / 8));
            worldObj.spawnEntityInWorld(tntPrimed);
            tntPrimed.setVelocity(targetVelocity.xCoord, targetVelocity.yCoord, targetVelocity.zCoord);
            attackDelay = 0;
        }
        randomPosition();
        motionY = 0;
    }

    public void randomPosition() {
        delay++;
        if (isAttackTargetPlayer()) {
            setTarget(getAttackTarget());
            return;
        }
        if (delay >= 200 && noTarget()) {
            setTarget(posX+ WorldAPI.rand(10), posY, posZ+ WorldAPI.rand(10));
            delay = 0;
        }
    }

}
