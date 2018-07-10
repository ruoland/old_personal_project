package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruo.minigame.MiniGame;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.minigame.elytra.ElytraEvent;


public class EntityElytraWeenTNT extends EntityElytraWeenCore {
    public double targetX, targetY, targetZ;

    public EntityElytraWeenTNT(World worldIn) {
        super(worldIn);
        setCollision(false);
        this.setBlockMode(Blocks.TNT);
    }

    public EntityElytraWeenTNT(World worldIn, double x, double y, double z) {
        this(worldIn);
        targetX = x;
        targetY = y;
        targetZ = z;
        this.setDeathTimer(400);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return source.isExplosion() ? false : super.attackEntityFrom(source, amount);
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        ElytraEvent a = MiniGame.elytraEvent;
        if (!MiniGame.elytra.isStart() || a == null)
            return;
        a.killCount++;

        String index = WorldAPI.getPlayer().getHorizontalFacing().getName();
        if (index.equalsIgnoreCase("WEST") || index.equalsIgnoreCase("EAST")) {
            for (int i = 1; i < 4; i++) {
                worldObj.createExplosion(this, posX + i, posY, posZ, 1.5F, true);
                worldObj.createExplosion(this, posX - i, posY, posZ, 1.5F, true);
            }
        }
        if (index.equalsIgnoreCase("NORTH") || index.equalsIgnoreCase("SOUTH")) {
            for (int i = 1; i < 4; i++) {
                worldObj.createExplosion(this, posX, posY, posZ + i, 1.5F, true);
                worldObj.createExplosion(this, posX, posY, posZ - i, 1.5F, true);
            }
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (WorldAPI.getPlayer() == null || FakePlayerHelper.fakePlayer == null) {//플레이어가 없는 경우
            this.setDead();
            return;
        }
        if (targetX != 0 && targetY != 0 && targetZ != 0) {
            if (isEntityAlive()) {
                this.setVelocity((targetX - posX) / 90, (targetY - posY) / 90, (targetZ - posZ) / 90);
                if (this.getDistance(targetX, targetY, targetZ) < 0.5) {
                    this.worldObj.createExplosion(this, posX, posY, posZ, 1.5F, false);
                    this.setDead();
                }
            }
        }
    }

}
