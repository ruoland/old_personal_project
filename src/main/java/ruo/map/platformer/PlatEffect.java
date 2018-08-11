package ruo.map.platformer;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.config.ConfigCategory;
import ruo.cmplus.deb.DebAPI;
import ruo.map.lopre2.EntityPreBlock;
import ruo.map.lopre2.jump2.EntityBigBlock;
import ruo.map.platformer.chapter1.EntityPlatBlock;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.EntityDefaultNPC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlatEffect {
    private static EntitySelect selectYes, selectNo;

    public static void selectSpawn(EntityLivingBase living) {
        selectYes = new EntitySelect(living.worldObj);
        selectYes.setCustomNameTag("예");
        selectYes.setPosition(EntityAPI.lookX(living, -30, 2), living.posY, EntityAPI.lookZ(living, -20, 2));
        living.worldObj.spawnEntityInWorld(selectYes);

        selectNo = new EntitySelect(living.worldObj);
        selectNo.setCustomNameTag("아니오");
        selectNo.setPosition(EntityAPI.lookX(living, 20, 2), living.posY, EntityAPI.lookZ(living, 20, 2));
        living.worldObj.spawnEntityInWorld(selectNo);
    }

    public static void killSelect() {
        if (selectYes != null) {
            selectYes.setDead();
            selectNo.setDead();
        }
    }

    public static boolean isSelectYes() {
        if (selectYes != null) {
            return selectYes.isClick;
        }
        return false;
    }

    public static boolean isSelectNo() {
        if (selectYes != null) {
            return selectNo.isClick;
        }
        return false;
    }

    private static HashMap<Integer, PlatStage> stage = new HashMap<Integer, PlatStage>();
    private static ArrayList<RotationP> initEntityPosList = new ArrayList<>();
    private static ArrayList<EntityLiving> initEntityList = new ArrayList<>();

    private static int currentStage, coin;
    private static ArrayList<EntityNPC> curNPCList = new ArrayList<EntityNPC>();

    private static boolean canFireballSpawn;

    static {
        stage.put(1, new PlatStage1());
        stage.put(2, new PlatStage2());
        stage.put(3, new PlatStage3());
        stage.put(4, new PlatStage4());

    }

    public static EntityBigBlock spawnBigBlock(Block block, double x, double y, double z) {
        EntityBigBlock bigBlock = new EntityBigBlock(WorldAPI.getWorld());
        bigBlock.setPosition(x, y, z);
        bigBlock.setBlock(block);
        initEntityList.add(bigBlock);
        initEntityPosList.add(new RotationP(x, y, z));
        return bigBlock;
    }

    public static EntityPlatBlock spawnPlatBlock(Block block, double x, double y, double z, boolean isVelo) {
        EntityPlatBlock bigBlock = new EntityPlatBlock(WorldAPI.getWorld());
        bigBlock.setPosition(x + Math.random(), y, z + Math.random());
        bigBlock.setBlock(block);
        initEntityList.add(bigBlock);
        initEntityPosList.add(new RotationP(x, y, z));
        return bigBlock;
    }

    public static EntityLiving spawnEntity(EntityLiving npc, double x, double y, double z) {
        npc.setPosition(x, y, z);
        initEntityList.add(npc);
        initEntityPosList.add(new RotationP(x, y, z));
        return npc;
    }

    public static EntityNPC spawnNPC(String name, double[] xyz) {
        return spawnNPC(name, xyz[0], xyz[1], xyz[2]);
    }

    public static EntityNPC spawnNPC(String name, double x, double y, double z) {
        EntityNPC npc = new EntityNPC(WorldAPI.getWorld());
        npc.setCustomNameTag(name);
        npc.setPosition(x, y, z);
        initEntityList.add(npc);
        initEntityPosList.add(new RotationP(x, y, z));
        return npc;
    }

    /**
     * 블럭이나 엔피씨가 소환 될 위치에 있는 엔티티를 제거하고 리스트에 있는 엔티티를 소환함
     */
    public static void init() {
        initEntityPosList.clear();
        initEntityList.clear();;
        curNPCList.clear();
        killList();
        spawn();
    }

    public static String format(String category, String key, Object... objects) {
        ConfigCategory configCategory = DebAPI.getWorldConfig().getCategory(category);
        String value = DebAPI.getWorldConfig().get(category, key, "null").getString();
        if (value == null)
            value = "null";
        if (value.equalsIgnoreCase("null"))
            DebAPI.getWorldConfig().load();
        value = DebAPI.getWorldConfig().get(category, key, "null").getString();
        return String.format(value, objects);
    }

    public static boolean hasKey(String category, String key) {
        return DebAPI.getWorldConfig().hasKey(category, key);
    }

    private static void spawn() {
        for (int i = 0; i < initEntityPosList.size(); i++) {
            EntityLiving npc = initEntityList.get(i);
            npc.forceSpawn = true;
            WorldAPI.getWorld().spawnEntityInWorld(npc);
            npc.onInitialSpawn(null, null);
            if (npc instanceof EntityPreBlock)
                ((EntityPreBlock) npc).setTeleport(false);
        }
    }

    public static void stageEnd() {
        if (getCurrentStage() != null) {
            getCurrentStage().isEnd = true;
            killList();
            initEntityList.clear();
            initEntityPosList.clear();
            System.out.println("처리");
        }
    }

    public static void killList() {
        for (int i = 0; i < initEntityPosList.size(); i++) {
            RotationP pos = initEntityPosList.get(i);
            EntityLiving npc = initEntityList.get(i);
            if (npc instanceof EntityPreBlock || npc instanceof EntityPlatBlock)
                killBlock(pos.getX(), pos.getY(), pos.getZ());
            else
                killEntity(pos.getX(), pos.getY(), pos.getZ());
        }
    }

    private static void killBlock(double x, double y, double z) {
        List<EntityDefaultNPC> aabbList = EntityAPI.getEntity(WorldAPI.getWorld(), new AxisAlignedBB(x - 5, y - 5, z - 5, x + 5, y + 5, z + 5), EntityDefaultNPC.class);
        for (EntityDefaultNPC block : aabbList) {
            if (block instanceof EntityPreBlock || block instanceof EntityPlatBlock) {
                block.setDead();
            }
        }
    }

    private static void killEntity(double x, double y, double z) {
        List<EntityLivingBase> aabbList = EntityAPI.getEntity(WorldAPI.getWorld(), new AxisAlignedBB(x - 5, y - 5, z - 5, x + 5, y + 5, z + 5), EntityLiving.class);
        for (EntityLivingBase entity : aabbList) {
            if (entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer)) {
                entity.setDead();
            }
        }
    }

    public static void spawnPlatBlock(World world, int x, int y, int z, int x2, int y2, int z2, Block block, boolean canKnockback) {
        int minX = Math.min(x, x2);
        int maxX = Math.max(x, x2);
        int minY = Math.min(y, y2);
        int maxY = Math.max(y, y2);
        int minZ = Math.min(z, z2);
        int maxZ = Math.max(z, z2);
        for (x = minX; x <= maxX; x++) {
            for (y = minY; y <= maxY; y++) {
                for (z = minZ; z <= maxZ; z++) {
                    if (WorldAPI.getBlock(x, y, z).getBlock() != Blocks.AIR)
                        return;
                    spawnPlatBlock(block, x, y, z, canKnockback).setCanKnockBack(canKnockback);
                }
            }
        }
    }

    public void loadNPC() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("./platnpc" + currentStage + ".txt")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] lineSplit = line.split("-");
                String[] xyzSplit = lineSplit[1].split(",");
                double xyz[] = WorldAPI.parseDouble(xyzSplit[0],xyzSplit[1],xyzSplit[2]);
                curNPCList.add(spawnNPC(lineSplit[0], xyz));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EntityNPC getNPC(String name) {
        EntityNPC npc = (EntityNPC) EntityDefaultNPC.getNPC(name);
        return npc;
    }

    public static PlatStage getCurrentStage() {
        return stage.get(currentStage);
    }

    public static PlatStage getStage(int stageid) {
        return stage.get(stageid);
    }

    public static void setCurrentStage(int currentStage2) {
        currentStage = currentStage2;
    }

    public static void addCurrentStage(int currentStage2) {
        currentStage += currentStage2;
    }

    public static boolean isCanFireballSpawn() {
        return canFireballSpawn;
    }

    public static void setCanFireballSpawn(boolean canFireballSpawn2) {
        canFireballSpawn = canFireballSpawn2;
    }

    public static void missionComplete() {

    }

    public static void missionStart() {

    }

    public static int getCoin() {
        return coin;
    }

    public static void setCoin(int co) {
        coin = co;
    }

    public static void addCoin(int co) {
        coin += co;
    }

}
