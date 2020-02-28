package ruo.yout;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import olib.api.WorldAPI;
import olib.effect.TickRegister;
import olib.effect.TickTask;

public class MojaeTest {

    public static void removeWall(){
        WorldAPI.setBlock(WorldAPI.getWorld(), 491, 5, 516, 469, 4, 516, Blocks.AIR);
        WorldAPI.setBlock(WorldAPI.getWorld(), 491, 5, 526, 469, 4, 526, Blocks.AIR);
    }

    public static void attackDelay(boolean setZero){
        if(setZero)
            MoJaeEvent.attackDelay = 0;
        else
            MoJaeEvent.attackDelay = -1;
    }

    public static void attack(String name, String name2){
        Mojae.monterAttack.put(name, name2);
        Mojae.monterAttack.put(name2, name);
    }

    public static void attack(Class<EntityLivingBase> cla1, Class <EntityLivingBase> cla2){
        Mojae.monterAttack.put(EntityList.getEntityStringFromClass(cla1), EntityList.getEntityStringFromClass(cla2));
        Mojae.monterAttack.put(EntityList.getEntityStringFromClass(cla2), EntityList.getEntityStringFromClass(cla1));
    }

    public static void teamKill(boolean noTeamKill){
        Mojae.noTeamKill = noTeamKill;
    }

    public static void monsterSpawn(String entity, String entity2, int spawnCount1, int spawnCount2, boolean lockEntity){

        TickRegister.register(new TickTask(TickEvent.Type.SERVER, 100, false) {
            @Override
            public void run(TickEvent.Type type) {
                TickRegister.register(new TickTask(TickEvent.Type.SERVER, 1, true) {
                    int posMin = 0;

                    @Override
                    public boolean stopCondition() {
                        return absRunCount >= spawnCount2;
                    }

                    @Override
                    public void run(TickEvent.Type type) {
                        EntityLiving living = (EntityLiving) EntityList.createEntityByName(entity2, WorldAPI.getWorld());
                        double posX = 3 + ((absRunCount - posMin) * 2);
                        if (posX > 10) {
                            posMin = absRunCount;
                        }
                        living.setPosition(posX, 4, 2);
                        WorldAPI.getWorld().spawnEntityInWorld(living);
                        living.onInitialSpawn(WorldAPI.getWorld().getDifficultyForLocation(living.getPosition()), null);
                        if (lockEntity)
                            Mojae.lockEntity(living);

                    }
                });

                TickRegister.register(new TickTask(TickEvent.Type.SERVER, 1, true) {
                    int posMin = 0;

                    @Override
                    public boolean stopCondition() {
                        return absRunCount >= spawnCount1;
                    }
                    @Override
                    public void run(TickEvent.Type type) {
                        EntityLiving living = (EntityLiving) EntityList.createEntityByName(entity, WorldAPI.getWorld());
                        double posX = 3 + ((absRunCount - posMin) * 2);
                        if (posX > 10) {
                            posMin = absRunCount;
                        }
                        living.setPosition(posX, 4, 19);
                        WorldAPI.getWorld().spawnEntityInWorld(living);
                        living.onInitialSpawn(WorldAPI.getWorld().getDifficultyForLocation(living.getPosition()), null);
                        if (lockEntity)
                            Mojae.lockEntity(living);
                    }
                });
            }
        });

    }
}
