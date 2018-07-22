package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ruo.minigame.api.SpawnDirection;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.minigame.elytra.Elytra;


public class EntityElytraWeenUP extends EntityElytraWeenCore {
    public boolean arrival;
    public boolean falling = false;//두번째 패턴용, 플레이어에게 공격 받고 떨어지는 상태인 경우
    private static final DataParameter<Boolean> FIVE_PATTERN = EntityDataManager.createKey(EntityElytraWeenUP.class,
            DataSerializers.BOOLEAN);//다섯번째 패턴용, 플레이어 Y 까지 올라갔을 때 타겟을 재설정하는데 이 값을 false로 하면 타겟이 재설정 되지 않음

    public EntityElytraWeenUP(World worldIn) {
        super(worldIn);
        setBlockMode(Blocks.PUMPKIN);
        setCollision(false);
        this.setDropItem(false);
        this.setTargetMove(false);
    }

    public EntityElytraWeenUP(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setTarget(x + getX(SpawnDirection.BACK, 15, false), y, z +getZ(SpawnDirection.BACK, 15, false));
        this.setDeathTimer(500);
        String index = WorldAPI.getPlayer().getHorizontalFacing().getName();
        if (index.equalsIgnoreCase("NORTH") || index.equalsIgnoreCase("SOUTH")) {
            this.setPosition(posX + WorldAPI.rand(5), posY, FakePlayerHelper.fakePlayer.posZ);
        }
        if (index.equalsIgnoreCase("WEST") || index.equalsIgnoreCase("EAST")) {
            this.setPosition(FakePlayerHelper.fakePlayer.posX, posY, posZ + WorldAPI.rand(5));
        }
    }

    public void entityInit() {
        super.entityInit();
        this.dataManager.register(FIVE_PATTERN, false);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        setFalling(true);
        setTargetMove(false);
        return super.attackEntityFrom(source, 2);
    }

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean f) {
        falling = f;
        this.setVelocity(0,0,0);
    }

    public boolean isFivePattern() {
        return dataManager.get(FIVE_PATTERN);
    }

    public void setFivePattern(boolean f) {
        dataManager.set(FIVE_PATTERN, f);
    }

    private Vec3d posy;
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (WorldAPI.getPlayer() == null || FakePlayerHelper.fakePlayer == null) {//플레이어가 없는 경우
            this.setDead();
            return;
        }
        if (isFalling()) {
            if ((int) (Elytra.flyingWeen.posY) == (int) posY || posY < Elytra.flyingWeen.posY) {
                worldObj.createExplosion(this, posX, posY, posZ, (float) 2.5F, false);
                System.out.println("폭발함");
                this.setDead();
            }
        } else if (!arrival) {
            if ((int) (FakePlayerHelper.fakePlayer.posY) != (int) posY) {
                if(posy == null)
                    posy = new Vec3d(0,(FakePlayerHelper.fakePlayer.posY - posY),0).normalize();
                this.setVelocity(0, posy.yCoord * 0.5, 0);
                if (!isFivePattern()) {
                    this.setTarget(FakePlayerHelper.fakePlayer.posX, FakePlayerHelper.fakePlayer.posY, FakePlayerHelper.fakePlayer.posZ);
                }
                else{
                    setTarget(targetX, FakePlayerHelper.fakePlayer.posY, targetZ);
                }
            }else {
                this.setPosition(posX,FakePlayerHelper.fakePlayer.posY,posZ);
                arrival = true;
                setTargetMove(true);
                System.out.println("Y 같음");

            }
        }
    }

}
