package map.lopre2.nouse;

import cmplus.util.Sky;
import map.lopre2.jump3.EntityBoatBuildBlock;
import olib.api.NBTAPI;
import olib.api.WorldAPI;
import olib.effect.AbstractTick;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import map.lopre2.jump1.EntityBuildBlock;
import map.lopre2.jump1.EntityLavaBlock;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Loop {
    static double currentY, currentZ;

    public static void curve(World worldObj, int x, int y, int z, int x2, int y2, int z2) {
        WorldAPI.blockTick(worldObj, x, x2, y, y2, z, z2, new AbstractTick.BlockXYZ() {
            @Override
            public void run(Type type) {
                    EntityLavaBlock lavaBlock = new EntityLavaBlock(worldObj);
                    lavaBlock.setPosition(x, y - 1 + 0.2 + Math.random(), z);
                    lavaBlock.setTeleportLock(true);
                    lavaBlock.setBlock(Blocks.STONE);
                    lavaBlock.setTeleport(false);
                    worldObj.spawnEntityInWorld(lavaBlock);
                    System.out.println(x + " " + y + " " + z);
            }
        });
    }

    public static EntityBuildBlock blockSet(World worldObj, int xx, int yy, int zz, int x2, int y2, int z2) {
        EntityBuildBlock buildBlock = new EntityBuildBlock(worldObj);
        buildBlock.setPosition(xx, yy, zz);
        buildBlock.setTeleportLock(true);
        buildBlock.setBlock(Blocks.STONE);
        buildBlock.setTeleport(false);
        worldObj.spawnEntityInWorld(buildBlock);
        buildBlock.setBlock(xx,yy,zz,x2,y2,z2);
        return buildBlock;
    }
    public static EntityBoatBuildBlock moveBlockSet(World worldObj, int xx, int yy, int zz, int x2, int y2, int z2) {
        EntityBoatBuildBlock buildBlock = new EntityBoatBuildBlock(worldObj);
        buildBlock.setPosition(xx, yy, zz);
        buildBlock.setTeleportLock(false);
        buildBlock.setBlock(Blocks.STONE);
        buildBlock.setTeleport(false);
        worldObj.spawnEntityInWorld(buildBlock);
        buildBlock.setBlock(xx,yy,zz,x2,y2,z2);
        return buildBlock;
    }
    public static void saveBuildBlock(EntityBuildBlock buildBlock) {
        StringBuffer buildName = new StringBuffer("./buildblock/클릭한블럭.nbt");
        File clickBlock = new File(buildName.toString());
        if(clickBlock.isFile()){
            try {
                Desktop.getDesktop().open(new File("./buildblock"));
                WorldAPI.addMessage("빌드블럭이 이미 있습니다.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            NBTAPI nbtapi = new NBTAPI(buildName.toString());
            buildBlock.writeEntityToNBT(nbtapi.getNBT());
            nbtapi.saveNBT();
            WorldAPI.addMessage("빌드블럭을 저장했습니다. 이름을 바꾸세요.");


        }
    }
    public static void save(World worldObj, String name,int xx, int yy, int zz, int x2, int y2, int z2) {
        StringBuffer buildName = new StringBuffer("./buildblock/").append(name).append(".nbt");
        NBTAPI nbtapi = new NBTAPI(buildName.toString());
        NBTTagCompound compound = nbtapi.getNBT();
        ArrayList<BlockPos> blockPosList = new ArrayList();
        ArrayList<ItemStack> blockList = new ArrayList();
        WorldAPI.blockTick(worldObj, xx, x2, yy, y2, zz, z2, new AbstractTick.BlockXYZ() {
            @Override
            public void run(TickEvent.Type type) {
                worldObj.getBlockState(getPos()).getBlock();
                if (worldObj.getBlockState(getPos()).getBlock() != Blocks.AIR) {
                    blockPosList.add(new BlockPos(xx - x, yy - y, zz - z));

                    IBlockState state = worldObj.getBlockState(getPos());
                    Block block = state.getBlock();
                    blockList.add(new ItemStack(state.getBlock(), 1, block.getMetaFromState(state)));
                }
            }
        });
        compound.setInteger("BUILDSIZE", blockList.size());
        for (int i = 0; i < blockList.size(); i++) {
            ItemStack stack = blockList.get(i);
            BlockPos pos = blockPosList.get(i);
            compound.setTag(i + "-STACK", stack.serializeNBT());
            compound.setInteger(i + "-POSX", pos.getX());
            compound.setInteger(i + "-POSY", pos.getY());
            compound.setInteger(i + "-POSZ", pos.getZ());
        }
        nbtapi.saveNBT();
    }
    public static EntityBuildBlock read(World worldObj, String name, double x, double y, double z) {
        StringBuffer buildName = new StringBuffer("./buildblock/").append(name).append(".nbt");

        NBTAPI nbtapi = new NBTAPI(buildName.toString());
        NBTTagCompound compound = nbtapi.getNBT();
        EntityBuildBlock buildBlock = new EntityBuildBlock(worldObj);
        buildBlock.blockRead(compound);
        buildBlock.setPosition(x,y,z);
        buildBlock.setTeleportLock(true);
        buildBlock.setBlock(Blocks.STONE);
        buildBlock.setTeleport(false);
        worldObj.spawnEntityInWorld(buildBlock);
        return buildBlock;

    }
    public static void entityStairs(World worldObj, int maxCount, double posX, double posY, double posZ, double maxY) {
        currentY = 0;
        currentZ = 0;
        for (int i = 0; i < maxCount; i++) {
            EntityLavaBlock miniween = new EntityLavaBlock(worldObj);
            miniween.setTeleportLock(true);
            if (currentY == 0 && currentZ == 0) {
                miniween.setPosition(posX, posY + currentY, posZ + currentZ);
                worldObj.spawnEntityInWorld(miniween);
                currentZ++;
                continue;
            }
            if (currentY < maxY && WorldAPI.rand.nextInt(2) == 0) {
                currentY++;
                System.out.println("Y 증가함");
            }

            if (currentY == 0) currentY++;

            currentZ += WorldAPI.rand.nextInt(4) + Double.valueOf("0." + WorldAPI.rand.nextInt(10));
            miniween.setPosition(posX + WorldAPI.rand(4) / 10, posY - 1 + currentY, posZ + currentZ);
            worldObj.spawnEntityInWorld(miniween);
        }
    }

    public static void entityStairsForce2(World worldObj, double posX, double posY, double posZ, double maxY, double maxZ) {
        currentY = 0;
        currentZ = 0;
        double destinationX = posX;
        double destinationY = posY + maxY;
        double destinationZ = posZ + maxZ;
        entityStarisSpawn(worldObj, posX + WorldAPI.rand(4) / 10, posY - 1 + currentY, posZ + currentZ);
        entityStarisSpawn(worldObj, destinationX, destinationY, destinationZ);
        for (int i = 0; i < maxZ; i++) {
        }
    }

    private static void entityStarisSpawn(World worldObj, double posX, double posY, double posZ) {
        EntityLavaBlock miniween = new EntityLavaBlock(worldObj);
        miniween.setTeleportLock(true);
        miniween.setPosition(posX, posY, posZ);//시작 좌표
        worldObj.spawnEntityInWorld(miniween);
        miniween.onInitialSpawn(null, null);
        miniween.setTeleport(false);
    }

    /**
     * Gets the distance to the position.
     */
    public static double getDistance(double x, double y, double z, double x2, double y2, double z2) {
        double d0 = x - x2;
        double d1 = y - y2;
        double d2 = z - z2;
        return (double) MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public static void entityStairsForce(World worldObj, double posX, double posY, double posZ, double maxY, double maxZ) {
        currentY = 0;
        currentZ = 0;
        boolean forceStop = false;
        double destinationX = posX;
        double destinationY = posY + maxY;
        double destinationZ = posZ + maxZ;

        for (int i = 0; i < maxZ; i++) {
            EntityLavaBlock miniween = new EntityLavaBlock(worldObj);
            miniween.setTeleportLock(true);
            int ypath = MathHelper.floor_double(maxY - (posY + currentY));//목적 Y까지 남은 거리
            int zpath = MathHelper.floor_double((posZ + maxZ) - (posZ + currentZ));//목적Z까지 남은 거리
            double distance = getDistance(posX, posY + currentY, posZ + currentZ, destinationX, destinationY, destinationZ);
            System.out.println("distance" + distance);
            System.out.println("ypath " + ypath);
            System.out.println("zpath " + zpath);
            if (currentY == 0 && currentZ == 0) {
                miniween.setPosition(posX, posY + currentY, posZ + currentZ);
                worldObj.spawnEntityInWorld(miniween);
                currentZ++;
                continue;
            }
            if (ypath > 0) {//목적Y 보다 낮은 경우
                if (worldObj.rand.nextBoolean() || ypath > currentY) {//확률로 Y를 더하게 함
                    currentY++;
                    System.out.println("Y를 더함" + currentY + " " + (distance - 1 > ypath));
                }
            }

            double plusZ = WorldAPI.rand.nextInt(4) + Double.valueOf("0." + WorldAPI.rand.nextInt(10));
            ;//얼마나 전진 시킬지 값을 담는 변수를 생성함

            if (plusZ < 0.5) {//0.5보다 낮게 나오면 블럭이 겹침
                System.out.println("0.5 보다 낮아 0.5 더함" + plusZ + 0.5);
                plusZ += 0.5;
            }
            zpath = MathHelper.floor_double((posZ + maxZ) - (posZ + currentZ + plusZ));//목적지에 도달했는지 알기 위해 한번 더 목적Z를 계산함
            if (zpath < 0) {//목적지를 초과한 경우 깎아서 목적Z에 맞게 변환함
                plusZ = (posZ + zpath) - posZ + 0.5;
                System.out.println("zpath가 0보다 아래임 plusZ" + plusZ);
                currentZ += Math.abs(zpath);
                forceStop = true;

            }
            currentZ += plusZ;
            miniween.setPosition(posX + WorldAPI.rand(4) / 10, posY - 1 + currentY, posZ + currentZ);
            worldObj.spawnEntityInWorld(miniween);
            miniween.onInitialSpawn(null, null);
            miniween.setTeleport(false);
            if (forceStop)
                break;
        }
    }

    /**
     * 리메이크 시작
     */
    public static void start() {
        Sky.fogDistance(5);
    }

    public static void aa() {

    }
}
