package ruo.asdfrpg.skill;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.EntityDefaultBlock;

public class EntityThrowBlock extends EntityDefaultBlock {
    public EntityThrowBlock(World worldIn) {
        super(worldIn);
    }

    @Override
    public void targetArrive() {
        if (getTarget() != null) {
            EntityDefaultBlock defaultBlock = (EntityDefaultBlock) getTarget();
            BlockPos pos = new BlockPos(WorldAPI.rand(1), WorldAPI.rand(1), WorldAPI.rand(1));
            defaultBlock.addBlock(getCurrentBlock(), pos);
            System.out.println("도착함" + pos);
        }
        this.setDead();

    }
}
