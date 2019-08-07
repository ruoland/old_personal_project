package map.lopre2.jump3;

import net.minecraft.entity.player.EntityPlayer;
import map.lopre2.nouse.Loop;
import map.lopre2.jump1.EntityLavaBlock;

public class JumpTool {



    public static void seesaw(EntityPlayer player, String name){
        Loop.read(player.worldObj,"rotateblock", player.posX, player.posY, player.posZ).setJumpName(name);
        double posX = player.posX;
        double posY = player.posY-1;
        double posZ = player.posZ-1;
        for(int x = 0; x < 6;x++){
            {
                EntityLavaBlock lavaBlock = new EntityLavaBlock(player.worldObj);
                lavaBlock.setJumpName(name + "-down");
                lavaBlock.setTeleportLock(true);
                lavaBlock.setPosition(posX, posY, posZ - x);
                player.worldObj.spawnEntityInWorld(lavaBlock);
                lavaBlock.setInv(true);
            }
            {
                EntityLavaBlock lavaBlock = new EntityLavaBlock(player.worldObj);
                lavaBlock.setJumpName(name + "-up");
                lavaBlock.setTeleportLock(true);
                lavaBlock.setPosition(posX, posY-5, posZ - x);
                player.worldObj.spawnEntityInWorld(lavaBlock);
                lavaBlock.setInv(true);
            }
        }
        for(int y = 1; y < 5;y++) {
            {
                EntityLavaBlock lavaBlock = new EntityLavaBlock(player.worldObj);
                lavaBlock.setJumpName(name + "-north");//왼쪽
                lavaBlock.setTeleportLock(true);
                lavaBlock.setPosition(posX, posY - y, posZ);
                player.worldObj.spawnEntityInWorld(lavaBlock);
                lavaBlock.setInv(true);
            }
            {
                EntityLavaBlock lavaBlock = new EntityLavaBlock(player.worldObj);
                lavaBlock.setJumpName(name + "-south");//오른쪽
                lavaBlock.setTeleportLock(true);
                lavaBlock.setPosition(posX, posY - y, posZ-5);
                player.worldObj.spawnEntityInWorld(lavaBlock);
                lavaBlock.setInv(true);
            }
        }

    }

    public static void rotateBlockSpawn(EntityPlayer player, String name){
        Loop.read(player.worldObj,"rotateblock", player.posX, player.posY, player.posZ).setJumpName(name);
        double posX = player.posX;
        double posY = player.posY-1;
        double posZ = player.posZ-1;
         for(int x = 0; x < 6;x++){
             {
                 EntityLavaBlock lavaBlock = new EntityLavaBlock(player.worldObj);
                 lavaBlock.setJumpName(name + "-down");
                 lavaBlock.setTeleportLock(true);
                 lavaBlock.setPosition(posX, posY, posZ - x);
                 player.worldObj.spawnEntityInWorld(lavaBlock);
                 lavaBlock.setInv(true);
             }
             {
                 EntityLavaBlock lavaBlock = new EntityLavaBlock(player.worldObj);
                 lavaBlock.setJumpName(name + "-up");
                 lavaBlock.setTeleportLock(true);
                 lavaBlock.setPosition(posX, posY-5, posZ - x);
                 player.worldObj.spawnEntityInWorld(lavaBlock);
                 lavaBlock.setInv(true);
             }
        }
        for(int y = 1; y < 5;y++) {
            {
                EntityLavaBlock lavaBlock = new EntityLavaBlock(player.worldObj);
                lavaBlock.setJumpName(name + "-north");//왼쪽
                lavaBlock.setTeleportLock(true);
                lavaBlock.setPosition(posX, posY - y, posZ);
                player.worldObj.spawnEntityInWorld(lavaBlock);
                lavaBlock.setInv(true);
            }
            {
                EntityLavaBlock lavaBlock = new EntityLavaBlock(player.worldObj);
                lavaBlock.setJumpName(name + "-south");//오른쪽
                lavaBlock.setTeleportLock(true);
                lavaBlock.setPosition(posX, posY - y, posZ-5);
                player.worldObj.spawnEntityInWorld(lavaBlock);
                lavaBlock.setInv(true);
            }
        }

    }

}
