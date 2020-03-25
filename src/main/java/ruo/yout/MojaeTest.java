package ruo.yout;

import cmplus.CMManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import olib.api.WorldAPI;
import olib.effect.TickRegister;
import olib.effect.TickTask;

public class MojaeTest {

    public static void removeWall() {
        WorldAPI.setBlock(WorldAPI.getWorld(), 491, 5, 516, 469, 4, 516, Blocks.AIR);
        WorldAPI.setBlock(WorldAPI.getWorld(), 491, 5, 526, 469, 4, 526, Blocks.AIR);
    }

    public static void attackDelay(boolean setZero) {
        if (setZero)
            MoJaeEvent.attackDelay = 0;
        else
            MoJaeEvent.attackDelay = -1;
    }

    public static void attack(String name, String name2) {
        Mojae.monterAttack.put(name, name2);
        Mojae.monterAttack.put(name2, name);
    }

    public static void attack(Class<EntityLivingBase> cla1, Class<EntityLivingBase> cla2) {
        Mojae.monterAttack.put(EntityList.getEntityStringFromClass(cla1), EntityList.getEntityStringFromClass(cla2));
        Mojae.monterAttack.put(EntityList.getEntityStringFromClass(cla2), EntityList.getEntityStringFromClass(cla1));
    }

    public static void removeAttack(String n, String n2) {
        Mojae.monterAttack.remove(n);
        Mojae.monterAttack.remove(n2);
    }

    public static void unlock(String name, String name2) {
        WorldAPI.command("unlockentity " + name);
        WorldAPI.command("unlockentity " + name2);
    }

    public static void blockSet(boolean isPurPur) {
        if (isPurPur) {
            WorldAPI.setBlock(WorldAPI.getWorld(), 18, 6, 15, 2, 4, 15, Blocks.PURPUR_BLOCK);
            WorldAPI.setBlock(WorldAPI.getWorld(), 18, 6, 5, 2, 4, 5, Blocks.PURPUR_BLOCK);
        } else if (!isPurPur) {
            WorldAPI.setBlock(WorldAPI.getWorld(), 18, 6, 15, 2, 4, 15, Blocks.AIR);
            WorldAPI.setBlock(WorldAPI.getWorld(), 18, 6, 5, 2, 4, 5, Blocks.AIR);
        }
    }

    public static void skelDelay0(boolean zero) {
        if (zero) {
            Mojae.skelDelay = zero ? 0 : -1;
        }
    }

    public static void mobHealth(boolean on) {
        if (on) {
            for (Entity entity : WorldAPI.getWorld().loadedEntityList) {
                if (entity instanceof EntityLivingBase) {
                    entity.setAlwaysRenderNameTag(true);
                    entity.setCustomNameTag("체력:" + ((EntityLivingBase) entity).getMaxHealth() + " / " + ((EntityLivingBase) entity).getHealth());
                }
            }
        } else {
            for (Entity entity : WorldAPI.getWorld().loadedEntityList) {
                if (entity instanceof EntityLivingBase) {
                    if (entity.getCustomNameTag().startsWith("체력:")) {
                        entity.setCustomNameTag("");
                        entity.setAlwaysRenderNameTag(false);
                    }
                }
            }
        }
    }

    public static void ui(boolean ui) {
        //UI = on이면 켜짐

        CMManager.setUI(RenderGameOverlayEvent.ElementType.HEALTH, ui);
        CMManager.setUI(RenderGameOverlayEvent.ElementType.FOOD, ui);
        CMManager.setUI(RenderGameOverlayEvent.ElementType.CROSSHAIRS, ui);
        CMManager.setUI(RenderGameOverlayEvent.ElementType.HOTBAR, ui);
        CMManager.setHand(ui);
    }

    public static void teamKill(boolean noTeamKill) {
        Mojae.noTeamKill = noTeamKill;
    }

    public static void monsterSpawn(String entity, String entity2, String spawnCount1, String spawnCount2, String lockEntity) {
        monsterSpawn(entity, entity2, Integer.parseInt(spawnCount1), Integer.parseInt(spawnCount2), Boolean.parseBoolean(lockEntity));
    }

    public static void monsterSpawn(String entity, String entity2, int spawnCount1, int spawnCount2, boolean lockEntity) {

        TickRegister.register(new TickTask(TickEvent.Type.SERVER, 1, true) {
            int posMin = 0;

            @Override
            public boolean stopCondition() {
                return absRunCount >= spawnCount2;
            }

            @Override
            public void endTick() {
                WorldAPI.addMessage("몬스터를 전부 소환했습니다");
            }

            @Override
            public void run(TickEvent.Type type) {
                EntityLiving living = (EntityLiving) EntityList.createEntityByName(entity2, WorldAPI.getWorld());
                double posX = 3 + ((absRunCount - posMin) * 2);
                if (posX > 15) {
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
            public void stopTick() {
                super.stopTick();
                WorldAPI.addMessage("몬스터를 전부 소환했습니다");
            }

            @Override
            public void run(TickEvent.Type type) {
                EntityLiving living = (EntityLiving) EntityList.createEntityByName(entity, WorldAPI.getWorld());
                double posX = 3 + ((absRunCount - posMin) * 2);
                if (posX > 15) {
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
}
