package ruo.asdfrpg.skill;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ruo.minigame.api.PosHelper;
import ruo.minigame.api.SpawnDirection;

import java.util.Random;

public class SkillBlockThrowAttack extends Skill {

    @Override
    public void onEffect(SkillStack playerSkill, int data) {
        super.onEffect(playerSkill, data);
        EntityPlayer player = playerSkill.getPlayer();
        PosHelper posHelper = new PosHelper(player);
        EntityAsdfBlock asdfBlock = new EntityAsdfBlock(player.worldObj);
        asdfBlock.setPosition(posHelper.getXZ(SpawnDirection.FORWARD, 1, true).addVector(0, 5, 0));
        player.worldObj.spawnEntityInWorld(asdfBlock);
        for(int i = 0; i < 5;i++)
        getRandomBlock(player, asdfBlock);
    }

    public BlockPos getRandomBlock(EntityPlayer player, EntityAsdfBlock asdfBlock) {
        World world = player.worldObj;
        Random random = player.worldObj.rand;
        double x = player.posX + random.nextInt(24);
        double y = player.posY + random.nextInt(24);
        double z = player.posZ + random.nextInt(24);
        BlockPos blockPos = new BlockPos(x, y, z);
        if (!world.isAirBlock(blockPos)) {//블럭이 있는 경우 위에도 블럭이 있는지 검색함
            while (blockPos.getY() < 256) {
                if (!world.isAirBlock(blockPos)) {
                    break;
                }
                blockPos = blockPos.up(1);
            }
        } else if (world.isAirBlock(blockPos)) {//블럭이 없는 경우 아래에 있는 블럭을 찾음
            while (blockPos.getY() > 0) {
                if (!world.isAirBlock(blockPos)) {
                    break;
                }
                blockPos = blockPos.down(1);
            }
        }
        Block block = player.worldObj.getBlockState(blockPos).getBlock();
        System.out.println("블러어어억"+block+blockPos);
        EntityThrowBlock defaultBlock = new EntityThrowBlock(world);
        defaultBlock.setPosition(blockPos.add(0,2,0));
        if(player.isServerWorld())
        world.spawnEntityInWorld(defaultBlock);
        defaultBlock.setBlock(world.getBlockState(blockPos).getBlock());
        defaultBlock.setTarget(asdfBlock);

        return blockPos;
    }
}



