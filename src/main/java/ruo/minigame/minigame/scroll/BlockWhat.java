package ruo.minigame.minigame.scroll;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ruo.minigame.fakeplayer.EntityFakePlayer;

public class BlockWhat extends Block {
    public BlockWhat(){
        super(Material.IRON);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
        if(entityIn instanceof EntityFakePlayer || entityIn instanceof EntityPlayer){
            EntityMushroom mushroom = new EntityMushroom(worldIn);
            mushroom.setPosition(pos.add(0,1,0));
            worldIn.spawnEntityInWorld(mushroom);
            mushroom.onInitialSpawn(null,null);
        }
    }
}
