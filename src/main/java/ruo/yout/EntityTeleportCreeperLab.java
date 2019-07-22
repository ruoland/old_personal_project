package ruo.yout;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import olib.api.Direction;
import olib.api.PosHelper;
import olib.api.WorldAPI;

import javax.annotation.Nullable;

public class EntityTeleportCreeperLab extends EntityCreeper {
    public EntityTeleportCreeperLab(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (getCreeperState() == -1) {//크리퍼가 폭발하기 전 상태에서만 텔레포트함
            for (int i = 0; i < 16; i++)
                if (this.attemptTeleport(posX + WorldAPI.rand(5), posY + WorldAPI.rand(5), posZ + WorldAPI.rand(5))) {
                    this.setCreeperState(-1);
                    return super.attackEntityFrom(source, amount);
                }
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
    }

    private int teleportDelay, explosionDelay;

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        startRiding(player, true);
        return super.processInteract(player, hand, stack);
    }

    @Override
    public double getMountedYOffset() {
        return super.getMountedYOffset() + 2;
    }

    @Override
    public double getYOffset() {
        return super.getYOffset() + 2;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (explosionDelay > 0)
            explosionDelay--;

        if (getAttackTarget() != null) {
            if (this.worldObj.isRemote) {
                for (int i = 0; i < 2; ++i) {
                    this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
                }
            }
            EntityLivingBase target = getAttackTarget();
            if (getDistanceToEntity(target) > 7 && teleportDelay == 0 && getCreeperState() == -1) {
                for (int i = 0; i < 16; i++)
                    if (this.attemptTeleport(target.posX + WorldAPI.rand(5), target.posY + WorldAPI.rand(5), target.posZ + WorldAPI.rand(5))) {
                        this.setCreeperState(-1);
                        teleportDelay = 500;
                    }
            }
            if (teleportDelay > 0)
                teleportDelay--;
        }
    }

    public void teleport() {
        EntityLivingBase target = getAttackTarget();
        PosHelper posHelper = new PosHelper(target);
        if (getDistanceToEntity(target) > 7 && teleportDelay == 0 && getCreeperState() == -1) {
            for (int i = 0; i < 16; i++) {
                if (rand.nextInt(20) == 0) {
                    if (this.attemptTeleport(posHelper.getX(Direction.BACK, 1, true), target.posY + WorldAPI.rand(5), posHelper.getX(Direction.BACK, 1, true))) {
                        break;
                    }
                }

                if (this.attemptTeleport(target.posX + WorldAPI.rand(5), target.posY + WorldAPI.rand(5), target.posZ + WorldAPI.rand(5))) {
                    this.setCreeperState(-1);
                    teleportDelay = 500;
                }
            }
        }
    }

    @Override
    public void setCreeperState(int state) {
        if (explosionDelay == 0) {
            explosionDelay = 100;
            super.setCreeperState(state);
        }
        else
            return;
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {

        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public void setDead() {
        super.setDead();
    }
}
