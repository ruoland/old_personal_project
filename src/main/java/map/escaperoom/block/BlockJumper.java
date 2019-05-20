package map.escaperoom.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
플레이어가 밟으면 플레이어가 높이 올라가는 블럭
 */
public class BlockJumper extends Block {
    public BlockJumper(Material materialIn) {
        super(materialIn);
    }


    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        super.onEntityWalk(worldIn, pos, entityIn);
        entityIn.motionY += 0.7;
    }
}
