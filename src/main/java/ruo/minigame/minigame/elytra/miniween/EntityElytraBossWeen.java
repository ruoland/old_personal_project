package ruo.minigame.minigame.elytra.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ruo.helloween.WeenEffect;
import ruo.minigame.MiniGame;
import ruo.minigame.api.Direction;
import ruo.minigame.api.PosHelper;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.minigame.elytra.Elytra;

public class EntityElytraBossWeen extends EntityElytraPumpkin {
    public EntityElytraBossWeen(World world) {
        super(world);
        addScale(14,14,14);
        setSize(15,15);
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
        first();
        System.out.println("타겟에 도착함 첫 패턴 실행함");
    }

    public void first(){
        Elytra elytra = MiniGame.elytra;
        elytra.spawnPumpkinAttack(Direction.FORWARD, 5,0);
        TickRegister.register(new AbstractTick(20, true) {
            @Override
            public void run(TickEvent.Type type) {
                boolean isSkip = false;
                for(int i = 0; i < 5;i++){
                    if(!isSkip && (i == 4 || worldObj.rand.nextInt(5) == 0))
                    {
                        isSkip = true;
                        continue;
                    }
                    elytra.spawnPumpkinAttack(Direction.FORWARD_RIGHT, 5, i);
                    elytra.spawnPumpkinAttack(Direction.FORWARD_LEFT, 5, i);
                }
                if(absRunCount == 5) {
                    absLoop = false;
                    second();
                }
            }
        });
    }

    public void second(){
        setTarget(posX, posY-10, posZ, 1);
        WeenEffect.entityRotateX(this, -90, new AbstractTick() {
            @Override
            public void run(TickEvent.Type type) {
                second2();
            }
        });
    }

    public void second2(){
        TickRegister.register(new AbstractTick(20, true) {
            @Override
            public void run(TickEvent.Type type) {
                EntityElytraBossMini entityElytraPumpkin = new EntityElytraBossMini(worldObj);
                entityElytraPumpkin.setPosition(posX, posY, posZ);
                entityElytraPumpkin.setTarget(posX, posY+10, posZ);
                entityElytraPumpkin.updateSpawnPosition();
                worldObj.spawnEntityInWorld(entityElytraPumpkin);
                if(absRunCount >10)
                {
                    entityElytraPumpkin.setSecondPhase(true, absRunCount);
                }

            }
        });
    }


}
