package ruo.minigame.minigame.minerun;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.PosHelper;
import ruo.minigame.api.Direction;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;

public class MapSetting {

    public void firstSetting(){
        WorldAPI.getWorld().setBlockState(new BlockPos(-188,64,567), Blocks.STONE.getDefaultState());
        spawnCreeper(-187.5, 65.0, 567.5);
        EntityAPI.position(-192.6, 58.0, 572.4,-187, 58, 572, 1, new AbstractTick.Position() {
            @Override
            public void runPosition() {
                WorldAPI.getWorld().setBlockToAir(new BlockPos(-188,64,567));
            }
        });
        spawnCreeper(-191.3, 60.0, 538.4);
        spawnCreeper(-189.4, 60.0, 534.7);

    }

    public void spawnCreeper(double x, double y, double z){
        EntityMRCreeper creeper = new EntityMRCreeper(WorldAPI.getWorld());
        creeper.setPosition(-191.5, 60.0, 538.9);
        WorldAPI.getWorld().spawnEntityInWorld(creeper);
    }
    private void create(){
        World world = WorldAPI.getWorld();
        EntityPlayer player = WorldAPI.getPlayer();
        PosHelper posHelper = new PosHelper(player);
        int lookX = (int) EntityAPI.lookX(WorldAPI.getPlayer().getHorizontalFacing(), 20);
        int lookZ = (int) EntityAPI.lookZ(WorldAPI.getPlayer().getHorizontalFacing(), 20);
        System.out.println(lookX+" - "+lookZ);
        setBlockX(lookX);
        setBlockZ(lookZ);

        for(int i = 0;i<3;i++){
            double forwardLX = posHelper.getX(Direction.LEFT, i, true);
            double forwardLZ = posHelper.getZ(Direction.LEFT, i, true);
            double forwardRX = posHelper.getX(Direction.RIGHT, i, true);
            double forwardRZ = posHelper.getZ(Direction.RIGHT, i, true);
            world.setBlockState(new BlockPos(forwardLX, WorldAPI.getPlayer().posY, forwardLZ), Blocks.STONE.getDefaultState());
            world.setBlockState(new BlockPos(forwardRX, WorldAPI.getPlayer().posY, forwardRZ), Blocks.STONE.getDefaultState());
            setBlockX(new BlockPos(forwardLX, WorldAPI.getPlayer().posY, forwardLZ), lookX);
            setBlockZ(new BlockPos(forwardRX, WorldAPI.getPlayer().posY, forwardRZ), lookZ);
        }
        for(int i = -2;i<0;i++){
            double forwardLX = posHelper.getX(Direction.LEFT, i, true);
            double forwardLZ = posHelper.getZ(Direction.LEFT, i, true);
            double forwardRX = posHelper.getX(Direction.RIGHT, i, true);
            double forwardRZ = posHelper.getZ(Direction.RIGHT, i, true);
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
