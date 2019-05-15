package map.escaperoom;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
플레이어가 밟으면 플레이어가  보는 방향으로 날아감
 */
public class BlockForward extends Block {
    public BlockForward(Material materialIn) {
        super(materialIn);
    }


    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        super.onEntityWalk(worldIn, pos, entityIn);
        Vec3d vec3d = entityIn.getLookVec();
        entityIn.addVelocity(vec3d.xCoord * 5, 0, vec3d.zCoord * 5);
    }
}
