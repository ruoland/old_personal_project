package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ruo.cmplus.util.Sky;
import ruo.helloween.WeenEffect;
import ruo.map.lopre2.LooPre2Event;
import ruo.minigame.MiniGame;
import ruo.minigame.api.Direction;
import ruo.minigame.api.PosHelper;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.minigame.elytra.Elytra;
import static ruo.minigame.fakeplayer.FakePlayerHelper.fakePlayer;
import static ruo.minigame.MiniGame.elytra;

public class EntityElytraBossWeen extends EntityElytraPumpkin {
    private static final DataParameter<Integer> PATTERN = EntityDataManager.createKey(EntityElytraBossWeen.class, DataSerializers.VARINT);

    public EntityElytraBossWeen(World world) {
        super(world);
        addScale(14, 14, 14);
        setSize(15, 15);
        this.setRotate(0, 90, 0);

    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(PATTERN, 1);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(2000000000);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }

    @Override
    public void targetArrive() {
        super.targetArrive();
        if (getPattern() == 1) {
            first();
            System.out.println("타겟에 도착함 첫 패턴 실행함");
        }
        dataManager.set(PATTERN, getPattern() + 1);
    }


    public int getPattern() {
        return dataManager.get(PATTERN);
    }

    public void first() {
        elytra.spawnPumpkinAttack(Direction.FORWARD, 5, 0);
        TickRegister.register(new AbstractTick(20, true) {
            @Override
            public void run(TickEvent.Type type) {
                boolean isSkip = false;
                for (int i = 0; i < 5; i++) {
                    if (!isSkip && (i == 4 || worldObj.rand.nextInt(5) == 0)) {
                        isSkip = true;

                        continue;
                    }
                    elytra.spawnPumpkinAttack(Direction.FORWARD_RIGHT, 5, i);
                    elytra.spawnPumpkinAttack(Direction.FORWARD_LEFT, 5, i);
                }
                if (absRunCount >= 5) {
                    absLoop = false;
                    second();
                }
            }
        });
    }

    public void second() {
        setTarget(posX, posY - 10, posZ, 1);
        WeenEffect.entityRotateX(this, -90, new AbstractTick() {
            @Override
            public void run(TickEvent.Type type) {
                second2();
            }
        });
    }

    public void second2() {
        TickRegister.register(new AbstractTick(20, true) {
            @Override
            public void run(TickEvent.Type type) {
                EntityElytraBossMini entityElytraPumpkin = new EntityElytraBossMini(worldObj);
                entityElytraPumpkin.setPosition(posX, posY, posZ);
                entityElytraPumpkin.setTarget(posX, posY + 10, posZ);
                entityElytraPumpkin.updateSpawnPosition();
                worldObj.spawnEntityInWorld(entityElytraPumpkin);
                if (absRunCount > 10) {
                    //360도로 발사함
                    entityElytraPumpkin.setPhase360(true, absRunCount);
                    if (absRunCount > 30) {
                        absLoop = false;
                        three();
                    }
                }
            }
        });
    }

    public void three() {
        TickRegister.register(new AbstractTick(20, true) {
            @Override
            public void run(TickEvent.Type type) {
                EntityElytraBossMini entityElytraPumpkin = new EntityElytraBossMini(worldObj);
                entityElytraPumpkin.setPosition(posX, posY, posZ);
                entityElytraPumpkin.setTarget(fakePlayer.posX, fakePlayer.posY - 10, fakePlayer.posZ);
                entityElytraPumpkin.setThreePhase(true, absRunCount);
                entityElytraPumpkin.updateSpawnPosition();
                worldObj.spawnEntityInWorld(entityElytraPumpkin);
                if (absRunCount > 10) {
                    //빠르게 올려보냄
                    entityElytraPumpkin.setTargetSpeed(1.5);
                    absDefTick = 10;
                }
                if (absRunCount > 30) {
                    four();
                }
            }
        });
    }

    /**
     * 0.5초마다  순서대로 나감
     */
    public void four() {
        elytra.spawnPumpkinAttack(Direction.FORWARD, 5, 0);
        TickRegister.register(new AbstractTick(10, true) {
            boolean isSkip = false;

            @Override
            public void run(TickEvent.Type type) {
                if (!isSkip && (absRunCount == 4 || worldObj.rand.nextInt(5) == 0)) {
                    isSkip = true;
                    return;
                }
                elytra.spawnPumpkinAttack(Direction.FORWARD_RIGHT, 5, absRunCount);
                elytra.spawnPumpkinAttack(Direction.FORWARD_LEFT, 5, absRunCount);
                if (absRunCount > 30) {
                    five();
                }
            }
        });
    }

    //안개 스테이지
    public void five() {
        setTarget(getSpawnX(), getSpawnY(), getSpawnZ(), 1);
        WeenEffect.entityRotateX(this, 0, new AbstractTick() {
            @Override
            public void run(TickEvent.Type type) {
                WeenEffect.fog(7, new AbstractTick() {
                    @Override
                    public void run(TickEvent.Type type) {
                        five2();
                    }
                });
            }
        });
    }

    public void five2(){
        EntityElytraPumpkin pumpkin = elytra.spawnPumpkinAttack(Direction.random(), 5, 10);
        pumpkin.setTarget(fakePlayer.posX, fakePlayer.posY, fakePlayer.posZ);
        pumpkin.setTargetArriveStop(false);
        pumpkin.setPosition(WorldAPI.x() + WorldAPI.minRand(5, 10), WorldAPI.y(), WorldAPI.z()+ WorldAPI.minRand(5, 10));
        pumpkin.setDeathTimer(200);
    }

}
