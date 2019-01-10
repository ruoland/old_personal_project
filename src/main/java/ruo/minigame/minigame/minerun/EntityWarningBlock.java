package ruo.minigame.minigame.minerun;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;

public class EntityWarningBlock extends EntityDefaultNPC {
    public EntityWarningBlock(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.REDSTONE_BLOCK);
        this.setCollision(false);
     }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.teleportSpawnPos();
    }
}
