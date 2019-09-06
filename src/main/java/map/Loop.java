package map;

import map.lopre2.jump1.EntityBuildBlock;
import map.lopre2.jump3.EntityBoatBuildBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import olib.api.NBTAPI;
import olib.api.WorldAPI;
import olib.effect.TickTask;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Loop {
    static double currentY, currentZ;

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
        WorldAPI.blockTick(worldObj, xx, x2, yy, y2, zz, z2, new TickTask.BlockXYZ() {
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
}
