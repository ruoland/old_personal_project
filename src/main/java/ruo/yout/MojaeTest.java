package ruo.yout;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import olib.api.WorldAPI;

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

        int posMin = 0;
        for (int i = 0; i < Integer.valueOf(spawnCount2); i++) {
            EntityLiving living = (EntityLiving) EntityList.createEntityByName(entity2, WorldAPI.getWorld());
            double posX = 3 + ((i - posMin) * 2);
            if (posX > 10) {
                posMin = i;
            }
            living.setPosition(posX, 4, 2);
            WorldAPI.getWorld().spawnEntityInWorld(living);
            living.onInitialSpawn(WorldAPI.getWorld().getDifficultyForLocation(living.getPosition()), null);
            if (lockEntity)
                Mojae.lockEntity(living);
        }
        posMin = 0;
        for (int i = 0; i < Integer.valueOf(spawnCount1); i++) {
            EntityLiving living = (EntityLiving) EntityList.createEntityByName(entity, WorldAPI.getWorld());
            double posX = 3 + ((i - posMin) * 2);
            if (posX > 10) {
                posMin = i;
            }
            living.setPosition(posX, 4, 19);
            WorldAPI.getWorld().spawnEntityInWorld(living);
            living.onInitialSpawn(WorldAPI.getWorld().getDifficultyForLocation(living.getPosition()), null);
            if (lockEntity)
                Mojae.lockEntity(living);
        }
    }
}
