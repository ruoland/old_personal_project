package ruo.asdfrpg.skill;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ruo.minigame.api.PosHelper;
import ruo.minigame.api.SpawnDirection;
import ruo.minigame.map.EntityDefaultBlock;

import java.util.Random;

public class SkillBlockThrowAttack extends Skill {

    @Override
    public void onEffect(SkillStack playerSkill, int data) {
        super.onEffect(playerSkill, data);
        EntityPlayer player = playerSkill.getPlayer();
        PosHelper posHelper = new PosHelper(player);
        EntityAsdfBlock asdfBlock = new EntityAsdfBlock(player.worldObj);
        asdfBlock.setPosition(posHelper.getXZ(SpawnDirection.FORWARD, 1, true).addVector(0, 3, 0));
        player.worldObj.spawnEntityInWorld(asdfBlock);
        getRandomBlock(player, asdfBlock);
        getRandomBlock(player, asdfBlock);
        getRandomBlock(player, asdfBlock);
    }

    public BlockPos getRandomBlock(EntityPlayer player, EntityAsdfBlock asdfBlock) {
        World world = player.worldObj;
        Random random = player.worldObj.rand;
        double x = player.posX + random.nextInt(24);
        double y = player.posY + random.nextInt(24);
        double z = player.posZ + random.nextInt(24);
        BlockPos blockPos = new BlockPos(x, y, z);
        if (!world.isBlockLoaded(blockPos)) {//블럭이 없는 경우 아래로 이동함
            while (blockPos.getY() > 0) {
                if (world.isBlockLoaded(blockPos)) {
                    break;
                }
                blockPos = blockPos.down(1);
            }
        } else if (world.isBlockLoaded(blockPos)) {
            while (blockPos.getY() < 256) {
                if (!world.isBlockLoaded(blockPos.up(1))) {
                    break;
                }
                blockPos = blockPos.up(1);
            }
        }
        Block block = player.worldObj.getBlockState(blockPos).getBlock();
        System.out.println("블러어어억"+block+blockPos);
        EntityDefaultBlock defaultBlock = new EntityDefaultBlock(world){
            @Override
            public void targetArrive() {
                this.setDead();
                asdfBlock.addBlock(block, asdfBlock.getPositionVector().subtract(this.getPositionVector()));
            }
        };
        defaultBlock.setPosition(blockPos);
        world.spawnEntityInWorld(defaultBlock);
        defaultBlock.setBlock(world.getBlockState(blockPos).getBlock());
        defaultBlock.setTarget(asdfBlock);
        return blockPos;
    }
}



