package ruo.minigame.minigame.minerun;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;

public class MapCreate {
    public void create(){
        World world = WorldAPI.getWorld();
        EntityPlayer player = WorldAPI.getPlayer();
        int lookX = (int) EntityAPI.lookX(WorldAPI.getPlayer().getHorizontalFacing(), 4);
        int lookZ = (int) EntityAPI.lookZ(WorldAPI.getPlayer().getHorizontalFacing(), 4);
        System.out.println(lookX+" - "+lookZ);
        setBlockX(lookX);
        setBlockZ(lookZ);

        for(int i = 0;i<3;i++){
            double forwardLX = EntityAPI.forwardLeftX(player, i, true);
            double forwardLZ = EntityAPI.forwardLeftZ(player, i, true);
            double forwardRX = EntityAPI.forwardRightX(player, i, true);
            double forwardRZ = EntityAPI.forwardRightZ(player, i, true);
            System.out.println("FLeftRightXZ"+i);

            world.setBlockState(new BlockPos(forwardLX, WorldAPI.getPlayer().posY, forwardLZ), Blocks.STONE.getDefaultState());
            world.setBlockState(new BlockPos(forwardRX, WorldAPI.getPlayer().posY, forwardRZ), Blocks.STONE.getDefaultState());
            setBlockX(new BlockPos(forwardLX, WorldAPI.getPlayer().posY, forwardLZ), lookX);
            setBlockZ(new BlockPos(forwardRX, WorldAPI.getPlayer().posY, forwardRZ), lookZ);
        }
        for(int i = -2;i<0;i++){
            double forwardLX = EntityAPI.forwardLeftX(player, i, true);
            double forwardLZ = EntityAPI.forwardLeftZ(player, i, true);
            double forwardRX = EntityAPI.forwardRightX(player, i, true);
            double forwardRZ = EntityAPI.forwardRightZ(player, i, true);
            world.setBlockState(new BlockPos(forwardLX, WorldAPI.getPlayer().posY, forwardLZ), Blocks.STONE.getDefaultState());
            world.setBlockState(new BlockPos(forwardRX, WorldAPI.getPlayer().posY, forwardRZ), Blocks.STONE.getDefaultState());
            setBlockX(new BlockPos(forwardLX, WorldAPI.getPlayer().posY, forwardLZ), lookX);
            setBlockZ(new BlockPos(forwardRX, WorldAPI.getPlayer().posY, forwardRZ), lookZ);
        }
    }
    public void setBlockX(BlockPos pos, int lookX){
        World world = WorldAPI.getWorld();
        EntityPlayer player = WorldAPI.getPlayer();
        if(lookX < 0){
            for(int i = 0;i>lookX;i--){
                world.setBlockState(pos.add(i,0,0), Blocks.STONE.getDefaultState());
                System.out.println("lookX"+i);
            }
        }else {
            for (int i = 0; i < lookX; i++) {
                world.setBlockState(pos.add(i,0,0), Blocks.STONE.getDefaultState());
                System.out.println("lookX" + i);
            }
        }
    }

    public void setBlockZ(BlockPos pos, int lookZ){
        World world = WorldAPI.getWorld();
        EntityPlayer player = WorldAPI.getPlayer();
        if(lookZ < 0){
            for(int i = 0;i>lookZ;i--){
                world.setBlockState(pos.add(0,0,i), Blocks.STONE.getDefaultState());
                System.out.println("lookZ"+i);
            }
        }else {
            for (int i = 0; i < lookZ; i++) {
                world.setBlockState(pos.add(0,0,i), Blocks.STONE.getDefaultState());
                System.out.println("lookZ" + i);
            }
        }
    }
    public void setBlockX(int lookX){
        World world = WorldAPI.getWorld();
        EntityPlayer player = WorldAPI.getPlayer();
        if(lookX < 0){
            for(int i = 0;i>lookX;i--){
                world.setBlockState(new BlockPos(player.posX+i, WorldAPI.getPlayer().posY, player.posZ), Blocks.STONE.getDefaultState());
                System.out.println("lookX"+i);
            }
        }else {
            for (int i = 0; i < lookX; i++) {
                world.setBlockState(new BlockPos(player.posX + i, WorldAPI.getPlayer().posY, player.posZ), Blocks.STONE.getDefaultState());
                System.out.println("lookX" + i);
            }
        }
    }

    public void setBlockZ(int lookZ){
        World world = WorldAPI.getWorld();
        EntityPlayer player = WorldAPI.getPlayer();
        if(lookZ < 0){
            for(int i = 0;i>lookZ;i--){
                world.setBlockState(new BlockPos(player.posX+i, WorldAPI.getPlayer().posY, player.posZ+i), Blocks.STONE.getDefaultState());
                System.out.println("lookZ"+i);
            }
        }else {
            for (int i = 0; i < lookZ; i++) {
                world.setBlockState(new BlockPos(player.posX + i, WorldAPI.getPlayer().posY, player.posZ+i), Blocks.STONE.getDefaultState());
                System.out.println("lookZ" + i);
            }
        }
    }
}
