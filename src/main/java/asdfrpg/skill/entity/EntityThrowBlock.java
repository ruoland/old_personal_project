package ruo.asdfrpg.skill.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.map.EntityDefaultBlock;

public class EntityThrowBlock extends EntityDefaultBlock {
    private EntityLivingBase target;
    public EntityThrowBlock(World worldIn) {
        super(worldIn);
    }

    public void setTarget(EntityLivingBase target) {
        this.target = target;
    }

    @Override
    public void targetArrive() {
        if (target != null) {
            EntityDefaultBlock defaultBlock = (EntityDefaultBlock) target;
            BlockPos pos = new BlockPos(WorldAPI.rand(1), WorldAPI.rand(1), WorldAPI.rand(1));
            defaultBlock.addBlock(getCurrentBlock(), pos);
            System.out.println("도착함" + pos);
        }
        this.setDead();

    }
}
