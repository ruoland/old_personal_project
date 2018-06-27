package ruo.map.lopre2.dummy;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import ruo.map.lopre2.EntityMoveBlock;

public class EntityBeltBlock extends EntityMoveBlock {
    public EntityBeltBlock(World world){
        super(world);
        setBlockMove(false);
        this.setBlock(Blocks.COAL_BLOCK);
    }
}
